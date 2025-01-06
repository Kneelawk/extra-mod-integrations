package com.kneelawk.exmi.core.api;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Constants for the ExMI mod.
 */
public class ExMIConstants {
    /**
     * ExMI Core's mod id.
     */
    public static final String MOD_ID = "extra_mod_integrations_core";

    /**
     * Creates a new {@link ResourceLocation} with an ExMI namespace.
     *
     * @param path the path for the resource location.
     * @return the new resource location.
     */
    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    /**
     * Creates a new translatable component.
     *
     * @param prefix the component key prefix.
     * @param suffix the component key suffix.
     * @param args   any args to the component.
     * @return the built component.
     */
    public static MutableComponent tt(String prefix, String suffix, Object... args) {
        return Component.translatable(prefix + "." + MOD_ID + "." + suffix, args);
    }

    /**
     * Creates a new 'gui' translatable component.
     *
     * @param suffix the gui component key suffix.
     * @param args   any args to the component.
     * @return the built component.
     */
    public static MutableComponent gui(String suffix, Object... args) {
        return tt("gui", suffix, args);
    }

    /**
     * Creates a new 'tooltip' translatable component.
     *
     * @param suffix the tooltip component key suffix.
     * @param args   any args to the component.
     * @return the built component.
     */
    public static MutableComponent tooltip(String suffix, Object... args) {
        return tt("tooltip", suffix, args);
    }
}
