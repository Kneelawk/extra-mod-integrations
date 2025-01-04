package com.kneelawk.extramodintegrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ExMIMod implements ClientModInitializer {
    public static final String MOD_ID = "extra-mod-integrations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("EMI Addon: Extra Mod Integrations!");
    }

    public static void logLoading(String displayName) {
        LOGGER.info("[Extra Mod Integrations] Loading {} Integration...", displayName);
    }

    public static void logSkipping(String displayName) {
        LOGGER.info("[Extra Mod Integrations] Skipping integration for {} (mod not found)", displayName);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static Component tt(String prefix, String path, Object... args) {
        return Component.translatable(prefix + "." + MOD_ID + "." + path, args);
    }

    public static Component gui(String path, Object... args) {
        return tt("gui", path, args);
    }

    public static Component tooltip(String path, Object... args) {
        return tt("tooltip", path, args);
    }
}
