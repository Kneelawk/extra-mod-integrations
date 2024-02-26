package com.kneelawk.extramodintegrations.tconstruct;

import dev.emi.emi.api.stack.EmiStack;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;

public class Util {
    public static EmiStack convertFluid(FluidStack fluidStack) {
        return EmiStack.of(fluidStack.getFluid(), fluidStack.getTag(), fluidStack.getAmount());
    }
}
