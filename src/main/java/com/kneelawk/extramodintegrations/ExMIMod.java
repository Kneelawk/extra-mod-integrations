package com.kneelawk.extramodintegrations;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExMIMod implements ClientModInitializer {
    public static final String MOD_ID = "extra-mod-integrations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("EMI Addon: Extra Mod Integrations!");
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Text tt(String prefix, String path, Object... args) {
        return new TranslatableText(prefix + "." + MOD_ID + "." + path, args);
    }

    public static Text gui(String path, Object... args) {
        return tt("gui", path, args);
    }

    public static Text tooltip(String path, Object... args) {
        return tt("tooltip", path, args);
    }
}
