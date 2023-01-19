package com.kneelawk.extramodintegrations.industrialrevolution;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class IRIntegration {
    @Nullable
    public static final IRIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("indrev")) {
            INSTANCE =
                ReflectionUtils.newInstance("com.kneelawk.extramodintegrations.industrialrevolution.IRIntegrationImpl");
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
