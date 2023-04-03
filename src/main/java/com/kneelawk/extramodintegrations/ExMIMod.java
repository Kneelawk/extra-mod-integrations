package com.kneelawk.extramodintegrations;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExMIMod implements ClientModInitializer {
    public static final String MOD_ID = "extra-mod-integrations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("EMI Addon: Extra Mod Integrations!");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static Component tt(String prefix, String path, Object... args) {
        return new TranslatableComponent(prefix + "." + MOD_ID + "." + path, args);
    }

    public static Component gui(String path, Object... args) {
        return tt("gui", path, args);
    }

    public static Component tooltip(String path, Object... args) {
        return tt("tooltip", path, args);
    }
}
