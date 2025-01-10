package com.kneelawk.exmi.actuallyadditions;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class AAPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        ExMIPlugin.register(registry, "actuallyadditions", "Actually Additions",
            "com.kneelawk.exmi.actuallyadditions.AAIntegration");
    }
}
