package com.kneelawk.extramodintegrations.util;

import java.util.List;

import dev.emi.emi.api.widget.WidgetHolder;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class UIUtils {
    public static final float FLUID_PATCH_WIDTH = 16f;
    public static final float FLUID_PATCH_HEIGHT = 16f;

    private static final String[] suffixes = {
        "metric.format.0", "metric.format.1", "metric.format.2", "metric.format.3", "metric.format.4",
        "metric.format.5", "metric.format.6", "metric.format.7", "metric.format.8", "metric.format.9"
    };

    public static OrderedText cookTime(int ticks) {
        float secs = ticks / 20f;
        return gui("cook_time", secs).asOrderedText();
    }

    public static void cookTime(WidgetHolder widgets, int ticks, int x, int y) {
        widgets.addText(cookTime(ticks), x, y, 0xFF3F3F3F, false);
    }

    public static void cookArrow(WidgetHolder widgets, int ticks, int x, int y) {
        widgets.addFillingArrow(x, y, ticks * 50).tooltip((x1, y1) -> List.of(TooltipComponent.of(cookTime(ticks))));
    }

    public static Text metricNumber(int number) {
        int power = MathHelper.clamp((int) Math.log10(number), 0, 9) / 3 * 3;
        double chopped = (double) number / Math.pow(10, power);
        return gui(suffixes[power], chopped);
    }

    public static void drawSlotHightlight(MatrixStack matrices, int x, int y, int w, int h) {
        matrices.push();
        matrices.translate(0, 0, 100);
        RenderSystem.colorMask(true, true, true, false);
        DrawableHelper.fill(matrices, x, y, x + w, y + h, -2130706433);
        RenderSystem.colorMask(true, true, true, true);
        matrices.pop();
    }

    public static void renderFluid(MatrixStack matrices, FluidVariant fluid, int x, int areaY,
                                   float areaHeight, float fluidHeight, float fluidWidth) {
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
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tess.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

        int fluidStripCount = (int) (fluidHeight / FLUID_PATCH_HEIGHT);
        for (int i = 0; i < fluidStripCount; i++) {
            buildFluidHorizontalStrip(bufferBuilder, model, sprite, (float) x,
                (float) areaY + areaHeight - FLUID_PATCH_HEIGHT * (i + 1), fluidWidth, FLUID_PATCH_HEIGHT, r,
                g, b);
        }
        float stripRemainder = fluidHeight % FLUID_PATCH_HEIGHT;
        buildFluidHorizontalStrip(bufferBuilder, model, sprite, (float) x,
            (float) areaY + areaHeight - FLUID_PATCH_HEIGHT * fluidStripCount - stripRemainder, fluidWidth,
            stripRemainder, r, g, b);

        tess.draw();
    }

    private static void buildFluidHorizontalStrip(BufferBuilder bufferBuilder, Matrix4f model, Sprite sprite, float x0,
                                                  float y0, float width, float height, float r, float g, float b) {
        int fluidPatchCount = (int) (width / FLUID_PATCH_WIDTH);
        for (int i = 0; i < fluidPatchCount; i++) {
            buildFluidPatch(bufferBuilder, model, sprite, x0 + FLUID_PATCH_WIDTH * i, y0, FLUID_PATCH_WIDTH, height, r,
                g, b);
        }
        float patchRemainder = width % FLUID_PATCH_WIDTH;
        buildFluidPatch(bufferBuilder, model, sprite, x0 + FLUID_PATCH_WIDTH * fluidPatchCount, y0, patchRemainder,
            height, r, g, b);
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
