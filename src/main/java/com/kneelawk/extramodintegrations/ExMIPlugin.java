package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.techreborn.TRIntegration;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

public class ExMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        TRIntegration.register(emiRegistry);
    }
}
