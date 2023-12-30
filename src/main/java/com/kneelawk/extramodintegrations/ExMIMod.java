package com.kneelawk.extramodintegrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Text tt(String prefix, String path, Object... args) {
        return Text.translatable(prefix + "." + MOD_ID + "." + path, args);
    }

    public static Text gui(String path, Object... args) {
        return tt("gui", path, args);
    }

    public static Text tooltip(String path, Object... args) {
        return tt("tooltip", path, args);
    }
}
