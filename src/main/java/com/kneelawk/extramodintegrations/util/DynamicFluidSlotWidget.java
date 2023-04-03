package com.kneelawk.extramodintegrations.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.config.EmiConfig;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import reborncore.common.fluid.container.FluidInstance;

public class DynamicFluidSlotWidget extends SlotWidget {
    public static final float FLUID_AREA_WIDTH = 32f;
    public static final float FLUID_PATCH_WIDTH = 16f;
    public static final float FLUID_AREA_HEIGHT = 48f;
    public static final float FLUID_PATCH_HEIGHT = 16f;

    protected final float fluidWidth;
    protected final float fluidHeight;
    protected final FluidVariant fluid;
    protected final int width;
    protected final int height;
    protected EmiTexture underlay;
    protected EmiTexture overlay;

    public DynamicFluidSlotWidget(FluidStack fluid, int x, int y, int width, int height, long capacity) {
        this(fluid.getType(), fluid.getAmount(), x, y, width, height, capacity);
    }

    public DynamicFluidSlotWidget(FluidInstance fluid, int x, int y, int width, int height, long capacity) {
        this(fluid.getVariant(), fluid.getAmount().getRawValue(), x, y, width, height, capacity);
    }

    public DynamicFluidSlotWidget(FluidVariant fluid, long amount, int x, int y, int width, int height, long capacity) {
        super(EmiStack.of(fluid.getFluid(), amount), x, y);
        this.width = width;
        this.height = height;
        fluidHeight = (float) ((double) amount / (double) capacity) * height;
        fluidWidth = (float) width;
        this.fluid = fluid;
    }

    public DynamicFluidSlotWidget underlay(EmiTexture tex) {
        underlay = tex;
        return this;
    }

    public DynamicFluidSlotWidget overlay(EmiTexture tex) {
        overlay = tex;
        return this;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (underlay != null) {
            underlay.render(matrices, x, y, delta);
        }

        renderFluid(matrices, fluid, x, y, fluidWidth, fluidHeight, height);

        if (overlay != null) {
            matrices.pushPose();
            matrices.translate(0.0, 0.0, 50.0);
            overlay.render(matrices, x, y, delta);
            matrices.popPose();
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), matrices, x + 2, y + 4);
        }

        Bounds bounds = getBounds();
        if (EmiConfig.showHoverOverlay && bounds.contains(mouseX, mouseY)) {
            EmiRenderHelper.drawSlotHightlight(matrices, bounds.x(), bounds.y(), bounds.width(),
                bounds.height());
        }
    }

    private static void renderFluid(PoseStack matrices, FluidVariant fluid, int x, int y, float slotWidth, float fluidHeight, float slotHeight) {
        TextureAtlasSprite[] sprites = FluidVariantRendering.getSprites(fluid);
        if (sprites == null || sprites.length < 1 || sprites[0] == null) {
            return;
        }

        TextureAtlasSprite sprite = sprites[0];
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, sprite.atlas().location());
        Matrix4f model = matrices.last().pose();
        int color = FluidVariantRendering.getColor(fluid);
        float r = (float) (color >> 16 & 0xFF) / 256.0F;
        float g = (float) (color >> 8 & 0xFF) / 256.0F;
        float b = (float) (color & 0xFF) / 256.0F;
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        int fluidPatchCountY = (int) (fluidHeight / FLUID_PATCH_HEIGHT);
        int fluidPatchCountX = (int) (slotWidth / FLUID_PATCH_WIDTH);
        for (int i = 0; i < fluidPatchCountY; i++) {
            for (int j = 0; j < fluidPatchCountX; j++) {
                buildFluidPatch(bufferBuilder, model, sprite,
                        (float) x + FLUID_PATCH_WIDTH * j,
                        (float) y + slotHeight - FLUID_PATCH_HEIGHT * (i + 1),
                        FLUID_PATCH_WIDTH,
                        FLUID_PATCH_HEIGHT,
                        r, g, b);
            }
        }
        float patchRemainderHeight = (fluidHeight / FLUID_PATCH_HEIGHT - fluidPatchCountY) * FLUID_PATCH_HEIGHT;
        float patchRemainderWidth = (slotWidth / FLUID_PATCH_WIDTH - fluidPatchCountX) * FLUID_PATCH_WIDTH;
        // top remainders
        for (int i = 0; i < fluidPatchCountX; i++) {
            buildFluidPatch(bufferBuilder, model, sprite,
                    (float) x + FLUID_PATCH_WIDTH * i,
                    (float) y + slotHeight - FLUID_PATCH_HEIGHT * fluidPatchCountY - patchRemainderHeight,
                    FLUID_PATCH_WIDTH,
                    patchRemainderHeight,
                    r, g, b);
        }
        // right remainders
        for (int i = 0; i < fluidPatchCountY; i++) {
            buildFluidPatch(bufferBuilder, model, sprite,
                    (float) x + FLUID_PATCH_WIDTH * fluidPatchCountX,
                    (float) y + slotHeight - FLUID_PATCH_HEIGHT * (i + 1),
                    patchRemainderWidth,
                    FLUID_PATCH_HEIGHT,
                    r, g, b);
        }
        // top-right remainder
        buildFluidPatch(bufferBuilder, model, sprite,
                (float) x + FLUID_PATCH_WIDTH * fluidPatchCountX,
                (float) y + slotHeight - FLUID_PATCH_HEIGHT * fluidPatchCountY - patchRemainderHeight,
                patchRemainderWidth,
                patchRemainderHeight,
                r, g, b);
        EmiPort.draw(bufferBuilder);
    }

    private static void buildFluidPatch(BufferBuilder bufferBuilder, Matrix4f model, TextureAtlasSprite sprite, float x0, float y0,
                                        float width, float height, float r, float g, float b) {
        float x1 = x0 + width;
        float y1 = y0 + height;
        float uMax = sprite.getU1();
        float vMax = sprite.getV1();
        float spriteWidth = sprite.getU1() - sprite.getU0();
        float spriteHeight = sprite.getV1() - sprite.getV0();
        float uMin = uMax - spriteWidth * width / 16f;
        float vMin = vMax - spriteHeight * height / 16f;
        bufferBuilder.vertex(model, x0, y1, 1.0F).color(r, g, b, 1.0F).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(model, x1, y1, 1.0F).color(r, g, b, 1.0F).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(model, x1, y0, 1.0F).color(r, g, b, 1.0F).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(model, x0, y0, 1.0F).color(r, g, b, 1.0F).uv(uMin, vMin).endVertex();
    }
}
