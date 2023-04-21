package com.kneelawk.extramodintegrations;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.EmiRegistry;

import net.fabricmc.loader.api.FabricLoader;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;

public abstract class AbstractTRIntegration {
    @Nullable
    public static final AbstractTRIntegration INSTANCE;

    static {
        @Nullable AbstractTRIntegration instance;
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            try {
                instance = ReflectionUtils.newInstance("com.kneelawk.extramodintegrations.techreborn.TRIntegration");
            } catch (ClassNotFoundException e) {
                ExMIMod.LOGGER.warn("[Extra Mod Integrations] Attempted to load Tech Reborn integration, but " +
                    "Extra Mod Integrations was compiled with Tech Reborn integration disabled. " +
                    "EMI <-> Tech Reborn integration will not work.");
                instance = null;
            }
        } else {
            instance = null;
        }
        INSTANCE = instance;
    }

    protected abstract void registerImpl(EmiRegistry registry);

    public static void register(EmiRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.registerImpl(registry);
        }
    }
}
