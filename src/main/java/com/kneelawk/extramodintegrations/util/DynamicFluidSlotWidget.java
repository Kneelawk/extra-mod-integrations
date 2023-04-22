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
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.config.EmiConfig;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import reborncore.common.fluid.container.FluidInstance;

public class DynamicFluidSlotWidget extends SlotWidget {
    public static final float FLUID_PATCH_WIDTH = 16f;
    public static final float FLUID_PATCH_HEIGHT = 16f;

    protected final int width;
    protected final int height;
    protected final long capacity;
    protected EmiTexture overlay;

    public DynamicFluidSlotWidget(FluidInstance fluid, int x, int y, int width, int height, long capacity) {
        this(FluidEmiStack.of(fluid.getFluid(), fluid.getAmount().getRawValue()), x, y, width, height, capacity);
    }

    public DynamicFluidSlotWidget(FluidVariant fluid, int amount, int x, int y, int width, int height, long capacity) {
        this(FluidEmiStack.of(fluid.getFluid(), amount), x, y, width, height, capacity);
    }

    public DynamicFluidSlotWidget(EmiIngredient stack, int x, int y, int width, int height, long capacity) {
        super(stack, x, y);

        if (stack.getEmiStacks().stream().anyMatch(s -> !(s instanceof FluidEmiStack))) {
            throw new IllegalArgumentException("Ingredient must be fluid " + stack);
        }

        this.width = width;
        this.height = height;
        this.capacity = capacity;
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
        float fluidWidth = (float) width;

        FluidEmiStack fluidEmiStack = null;
        if (stack instanceof FluidEmiStack s) {
            fluidEmiStack = s;
        } else if (stack instanceof ListEmiIngredient listEmiIngredient) {
            int elementIndex = (int) (System.currentTimeMillis() / 1000 % listEmiIngredient.getEmiStacks().size());
            fluidEmiStack = (FluidEmiStack) listEmiIngredient.getEmiStacks().get(elementIndex);
        } else if (stack instanceof TagEmiIngredient tagEmiIngredient) {
            if (tagEmiIngredient.getEmiStacks().size() > 0) {
                // modeled tags are complicated so i'm just rendering the first element of the tag
                fluidEmiStack = (FluidEmiStack) tagEmiIngredient.getEmiStacks().get(0);
            }
        }

        if (fluidEmiStack != null) {
            renderFluid(matrices, FluidVariant.of((Fluid) fluidEmiStack.getKey()), x, y, fluidWidth, ((float) fluidEmiStack.getAmount() / capacity) * height, height);
        }

        if (overlay != null) {
            matrices.pushPose();
            matrices.translate(0.0, 0.0, 50.0);
            overlay.render(matrices, x, y, delta);
            matrices.popPose();
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), matrices, x + 2, y + 4);
        }

        // TODO: only render these if the corresponding flag is set (where?)
        if (stack instanceof TagEmiIngredient) {
            EmiRender.renderTagIcon(stack, matrices, x, y + height - 4);
        } else if (stack instanceof ListEmiIngredient) {
            EmiRender.renderIngredientIcon(stack, matrices, x, y + height - 4);
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

        // make sure the fluid doesn't overflow, and render small quantities as 1 pixel tall
        fluidHeight = Float.max(1.0f, Float.min(fluidHeight, slotHeight));

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
        float uMin = sprite.getU0();
        float vMax = sprite.getV1();
        float spriteWidth = sprite.getU1() - sprite.getU0();
        float spriteHeight = sprite.getV1() - sprite.getV0();
        float uMax = uMin + spriteWidth * width / 16f;
        float vMin = vMax - spriteHeight * height / 16f;
        bufferBuilder.vertex(model, x0, y1, 1.0F).color(r, g, b, 1.0F).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(model, x1, y1, 1.0F).color(r, g, b, 1.0F).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(model, x1, y0, 1.0F).color(r, g, b, 1.0F).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(model, x0, y0, 1.0F).color(r, g, b, 1.0F).uv(uMin, vMin).endVertex();
    }
}
