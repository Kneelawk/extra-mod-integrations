package com.kneelawk.exmi.chipped;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class CPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        ExMIPlugin.register(registry, "chipped", "Chipped", "com.kneelawk.exmi.chipped.CIntegration");
    }
}
