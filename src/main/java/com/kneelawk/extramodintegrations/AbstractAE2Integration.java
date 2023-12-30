package com.kneelawk.extramodintegrations;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.EmiRegistry;

import net.fabricmc.loader.api.FabricLoader;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;

public abstract class AbstractAE2Integration {
    public static final @Nullable AbstractAE2Integration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("ae2")) {
            INSTANCE = ReflectionUtils.newIntegrationInstance("com.kneelawk.extramodintegrations.appeng.AE2Integration",
                "Applied Energistics 2");
        } else {
            INSTANCE = null;
        }
    }

    protected abstract void registerImpl(EmiRegistry registry);

    public static void register(EmiRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.registerImpl(registry);
        } else {
            ExMIMod.logSkipping("Applied Energistics 2");
        }
    }
}
