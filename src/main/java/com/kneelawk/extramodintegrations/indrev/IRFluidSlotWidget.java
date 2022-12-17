package com.kneelawk.extramodintegrations.indrev;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.widget.Bounds;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.util.math.MatrixStack;

import com.kneelawk.extramodintegrations.util.CustomFluidSlotWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class IRFluidSlotWidget extends CustomFluidSlotWidget {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 43;
    public static final float FLUID_AREA_WIDTH = 14f;
    public static final float FLUID_AREA_HEIGHT = 41f;

    public IRFluidSlotWidget(@Nullable FluidVariant fluid, long amount, int x, int y,
                             long capacity) {
        super(fluid, amount, x, y, capacity);
    }

    public IRFluidSlotWidget(@Nullable ResourceAmount<FluidVariant> res,
                             int x, int y, long capacity) {
        super(res, x, y, capacity);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (drawBack) {
            IRTextures.TANK_BOTTOM.render(matrices, x, y, delta);
        }

        if (fluid != null) {
            UIUtils.renderFluid(matrices, fluid, x + 1, y + 1, FLUID_AREA_HEIGHT, fluidFullness * FLUID_AREA_HEIGHT,
                FLUID_AREA_WIDTH);
        }

        if (drawBack) {
            RenderSystem.enableBlend();
            matrices.push();
            matrices.translate(0.0, 0.0, 50.0);
            IRTextures.TANK_TOP.render(matrices, x, y, delta);
            matrices.pop();
            RenderSystem.disableBlend();
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), matrices, x, y);
        }

        Bounds bounds = getBounds();
        // TODO: detect user config slot-hover-overlays
        if (bounds.contains(mouseX, mouseY)) {
            UIUtils.drawSlotHightlight(matrices, bounds.x() + 1, bounds.y() + 1, bounds.width() - 2,
                bounds.height() - 2);
        }
    }
}
