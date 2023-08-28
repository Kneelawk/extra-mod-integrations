package com.kneelawk.extramodintegrations.techreborn;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.widget.Bounds;
import reborncore.common.fluid.container.FluidInstance;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import net.minecraft.client.gui.DrawContext;

import com.kneelawk.extramodintegrations.util.CustomFluidSlotWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class TRFluidSlotWidget extends CustomFluidSlotWidget {
    public static final int WIDTH = 22;
    public static final int HEIGHT = 56;
    public static final float FLUID_AREA_WIDTH = 14f;
    public static final float FLUID_AREA_HEIGHT = 48f;

    public TRFluidSlotWidget(FluidInstance fluid, int x, int y, long capacity) {
        super(fluid.getVariant(), fluid.getAmount().getRawValue(), x, y, capacity);
    }

    public TRFluidSlotWidget(FluidVariant fluid, long amount, int x, int y, long capacity) {
        super(fluid, amount, x, y, capacity);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (drawBack) {
            TRTextures.TANK_BASE.render(context, x, y, delta);
        }

        if (fluid != null) {
            UIUtils.renderFluid(context.getMatrices(), fluid, x + 4, y + 4, FLUID_AREA_HEIGHT,
                fluidFullness * FLUID_AREA_HEIGHT, FLUID_AREA_WIDTH);
        }

        if (drawBack) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0, 0.0, 50.0);
            TRTextures.TANK_GRADUATION.render(context, x + 3, y + 3, delta);
            context.getMatrices().pop();
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), context, x + 2, y + 4);
        }

        Bounds bounds = getBounds();
        // TODO: detect user config slot-hover-overlays
        if (bounds.contains(mouseX, mouseY)) {
            UIUtils.drawSlotHightlight(context, bounds.x() + 4, bounds.y() + 4, bounds.width() - 8,
                bounds.height() - 8);
        }
    }
}
