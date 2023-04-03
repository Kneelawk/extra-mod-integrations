package com.kneelawk.extramodintegrations.hephaestus;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class HephaestusIntegration {
    @Nullable
    public static final HephaestusIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("tconstruct")) {
            INSTANCE = ReflectionUtils.newInstance("com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl");
        } else {
            INSTANCE = null;
        }
    }

    abstract void registerImpl(EmiRegistry registry);

    public static void register(EmiRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.registerImpl(registry);
        }
    }
}
