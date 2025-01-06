package com.kneelawk.exmi.techreborn;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TRConstants {
    public static final String MOD_ID = "extra_mod_integrations_tech_reborn";

    public static MutableComponent tt(String prefix, String suffix, Object... args) {
        return Component.translatable(prefix + "." + MOD_ID + "." + suffix, args);
    }

    public static MutableComponent gui(String suffix, Object... args) {
        return tt("gui", suffix, args);
    }

    public static MutableComponent tooltip(String suffix, Object... args) {
        return tt("tooltip", suffix, args);
    }
}
