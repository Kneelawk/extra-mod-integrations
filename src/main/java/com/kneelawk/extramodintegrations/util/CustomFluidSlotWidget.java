package com.kneelawk.extramodintegrations.util;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;

public class CustomFluidSlotWidget extends SlotWidget {
    protected final float fluidFullness;
    protected final @Nullable FluidVariant fluid;

    public CustomFluidSlotWidget(@Nullable FluidVariant fluid, long amount, int x, int y, long capacity) {
        super(fluid == null ? EmiStack.EMPTY : EmiStack.of(fluid.getFluid(), fluid.getNbt(), amount), x, y);
        fluidFullness = Math.min((float) ((double) amount / (double) capacity), 1.0f);
        this.fluid = fluid;
    }

    public CustomFluidSlotWidget(@Nullable ResourceAmount<FluidVariant> res, int x, int y, long capacity) {
        this(res == null ? null : res.resource(), res == null ? 0 : res.amount(), x, y, capacity);
    }
}
