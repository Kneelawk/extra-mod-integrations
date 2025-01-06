package com.kneelawk.exmi.core.impl;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMI;

@EmiEntrypoint
public class ExMICorePlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ExMI.FLUID_FROM_CONTAINER_CATEGORY);
        registry.addCategory(ExMI.FLUID_INTO_CONTAINER_CATEGORY);
    }
}
