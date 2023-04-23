package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHephaestusIntegration {
    @Nullable
    public static final AbstractHephaestusIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("tconstruct")) {
            INSTANCE =
                ReflectionUtils.newIntegrationInstance("com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration",
                        "Hephaestus");
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
