package com.kneelawk.exmi.reliquary;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class RPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        ExMIPlugin.register(registry, "reliquary", "Reliquary", "com.kneelawk.exmi.reliquary.RIntegration");
    }
}
