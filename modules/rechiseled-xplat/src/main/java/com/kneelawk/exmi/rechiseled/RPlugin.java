package com.kneelawk.exmi.rechiseled;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class RPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        ExMIPlugin.register(registry, "rechiseled", "Rechiseled", "com.kneelawk.exmi.rechiseled.RIntegration");
    }
}
