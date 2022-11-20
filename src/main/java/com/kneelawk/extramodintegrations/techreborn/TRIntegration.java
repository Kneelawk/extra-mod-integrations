package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class TRIntegration {
    @Nullable
    public static final TRIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            INSTANCE = ReflectionUtils.newInstance("com.kneelawk.extramodintegrations.techreborn.TRIntegrationImpl");
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
