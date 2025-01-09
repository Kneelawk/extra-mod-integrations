package com.kneelawk.exmi.techreborn;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class TRPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        ExMIPlugin.register(emiRegistry, "techreborn", "Tech Reborn", "com.kneelawk.exmi.techreborn.TRIntegration");
    }
}
