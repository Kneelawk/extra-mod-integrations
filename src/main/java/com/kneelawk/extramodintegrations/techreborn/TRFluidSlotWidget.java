package com.kneelawk.extramodintegrations.techreborn;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import reborncore.common.fluid.container.FluidInstance;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import net.minecraft.client.util.math.MatrixStack;

import com.kneelawk.extramodintegrations.util.UIUtils;

public class TRFluidSlotWidget extends SlotWidget {
    public static final int WIDTH = 22;
    public static final int HEIGHT = 56;
    public static final float FLUID_AREA_WIDTH = 14f;
    public static final float FLUID_AREA_HEIGHT = 48f;

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

        UIUtils.renderFluid(matrices, fluid, x + 4, y + 4, FLUID_AREA_HEIGHT, fluidHeight, FLUID_AREA_WIDTH);

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
        // TODO: detect user config slot-hover-overlays
        if (bounds.contains(mouseX, mouseY)) {
            UIUtils.drawSlotHightlight(matrices, bounds.x() + 4, bounds.y() + 4, bounds.width() - 8,
                bounds.height() - 8);
        }
    }
}
