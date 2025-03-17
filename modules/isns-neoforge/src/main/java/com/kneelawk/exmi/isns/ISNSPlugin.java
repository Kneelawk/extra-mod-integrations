package com.kneelawk.exmi.isns;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;

@EmiEntrypoint
public class ISNSPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        ExMIPlugin.register(emiRegistry, "irons_spellbooks", "Iron's Spells N Spellbooks", "com.kneelawk.exmi.isns.ISNSIntegration");
    }
}
