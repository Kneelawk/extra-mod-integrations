package com.kneelawk.extramodintegrations;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtraModIntegrationsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("extra-mod-integrations");

    @Override
    public void onInitialize() {
        LOGGER.info("EMI Addon: Extra Mod Integrations!");
    }
}
