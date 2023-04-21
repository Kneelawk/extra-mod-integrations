package com.kneelawk.extramodintegrations;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.EmiRegistry;

import net.fabricmc.loader.api.FabricLoader;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;

public abstract class AbstractIRIntegration {
    @Nullable
    public static final AbstractIRIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("indrev")) {
            INSTANCE =
                ReflectionUtils.newIntegrationInstance(
                    "com.kneelawk.extramodintegrations.industrialrevolution.IRIntegration", "Industrial Revolution");
        } else {
            INSTANCE = null;
        }
    }

    protected abstract void registerImpl(EmiRegistry registry);

    public static void register(EmiRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.registerImpl(registry);
        }
    }
}
