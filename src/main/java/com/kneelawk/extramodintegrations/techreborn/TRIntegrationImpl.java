package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExtraModIntegrationsMod;
import dev.emi.emi.api.EmiRegistry;

@SuppressWarnings("unused")
public class TRIntegrationImpl extends TRIntegration {
    @Override
    void registerImpl(EmiRegistry registry) {
        ExtraModIntegrationsMod.LOGGER.info("[Extra Mod Integrations] Loading TechReborn Integration...");
    }
}
