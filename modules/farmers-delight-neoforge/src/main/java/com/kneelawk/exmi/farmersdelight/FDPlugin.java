package com.kneelawk.exmi.farmersdelight;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class FDPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        ExMIPlugin.register(registry, "farmersdelight", "Farmer's Delight", "com.kneelawk.exmi.farmersdelight.FDIntegration");
    }
}
