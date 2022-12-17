package com.kneelawk.extramodintegrations.techreborn;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.config.EmiConfig;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import reborncore.common.fluid.container.FluidInstance;

public class TRFluidSlotWidget extends SlotWidget {
    public static final int WIDTH = 22;
    public static final int HEIGHT = 56;
    public static final float FLUID_AREA_WIDTH = 14f;
    public static final float FLUID_AREA_HEIGHT = 48f;
    public static final float FLUID_PATCH_HEIGHT = 16f;

    protected final float fluidHeight;
    protected final FluidVariant fluid;

    public TRFluidSlotWidget(FluidInstance fluid, int x, int y, long capacity) {
        super(new FluidEmiStack(fluid.getVariant(), fluid.getAmount().getRawValue()), x, y);
        fluidHeight = (float) ((double) stack.getAmount() / (double) capacity) * FLUID_AREA_HEIGHT;
        this.fluid = fluid.getVariant();
    }

    public TRFluidSlotWidget(FluidVariant fluid, long amount, int x, int y, long capacity) {
        super(EmiStack.of(fluid, amount), x, y);
        fluidHeight = (float) ((double) amount / (double) capacity) * FLUID_AREA_HEIGHT;
        this.fluid = fluid;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (drawBack) {
            TRTextures.TANK_BASE.render(matrices, x, y, delta);
        }

        renderFluid(matrices, fluid, x + 4, y + 4, fluidHeight);

        if (drawBack) {
            matrices.push();
            matrices.translate(0.0, 0.0, 200.0);
            TRTextures.TANK_GRADUATION.render(matrices, x + 3, y + 3, delta);
            matrices.pop();
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), matrices, x + 2, y + 4);
        }

        Bounds bounds = getBounds();
        if (EmiConfig.showHoverOverlay && bounds.contains(mouseX, mouseY)) {
            EmiRenderHelper.drawSlotHightlight(matrices, bounds.x() + 4, bounds.y() + 4, bounds.width() - 8,
                bounds.height() - 8);
        }
    }

    private static void renderFluid(MatrixStack matrices, FluidVariant fluid, int x, int y, float fluidHeight) {
        Sprite[] sprites = FluidVariantRendering.getSprites(fluid);
        if (sprites == null || sprites.length < 1 || sprites[0] == null) {
            return;
        }

        Sprite sprite = sprites[0];
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        Matrix4f model = matrices.peek().getPositionMatrix();
        int color = FluidVariantRendering.getColor(fluid);
        float r = (float) (color >> 16 & 0xFF) / 256.0F;
        float g = (float) (color >> 8 & 0xFF) / 256.0F;
        float b = (float) (color & 0xFF) / 256.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        int fluidPatchCount = (int) (fluidHeight / FLUID_PATCH_HEIGHT);
        for (int i = 0; i < fluidPatchCount; i++) {
            buildFluidPatch(bufferBuilder, model, sprite, (float) x,
                (float) y + FLUID_AREA_HEIGHT - FLUID_PATCH_HEIGHT * (i + 1), FLUID_AREA_WIDTH, FLUID_PATCH_HEIGHT, r,
                g, b);
        }
        float patchRemainder = (fluidHeight / FLUID_PATCH_HEIGHT - fluidPatchCount) * FLUID_PATCH_HEIGHT;
        buildFluidPatch(bufferBuilder, model, sprite, (float) x,
            (float) y + FLUID_AREA_HEIGHT - FLUID_PATCH_HEIGHT * fluidPatchCount - patchRemainder, FLUID_AREA_WIDTH,
            patchRemainder, r, g, b);
        EmiPort.draw(bufferBuilder);
    }

    private static void buildFluidPatch(BufferBuilder bufferBuilder, Matrix4f model, Sprite sprite, float x0, float y0,
                                        float width, float height, float r, float g, float b) {
        float x1 = x0 + width;
        float y1 = y0 + height;
        float uMax = sprite.getMaxU();
        float vMax = sprite.getMaxV();
        float spriteWidth = sprite.getMaxU() - sprite.getMinU();
        float spriteHeight = sprite.getMaxV() - sprite.getMinV();
        float uMin = uMax - spriteWidth * width / 16f;
        float vMin = vMax - spriteHeight * height / 16f;
        bufferBuilder.vertex(model, x0, y1, 1.0F).color(r, g, b, 1.0F).texture(uMin, vMax).next();
        bufferBuilder.vertex(model, x1, y1, 1.0F).color(r, g, b, 1.0F).texture(uMax, vMax).next();
        bufferBuilder.vertex(model, x1, y0, 1.0F).color(r, g, b, 1.0F).texture(uMax, vMin).next();
        bufferBuilder.vertex(model, x0, y0, 1.0F).color(r, g, b, 1.0F).texture(uMin, vMin).next();
    }
}
