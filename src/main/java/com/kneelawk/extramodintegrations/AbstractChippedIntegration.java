package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractChippedIntegration {
    @Nullable
    public static final AbstractChippedIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("chipped")) {
            INSTANCE =
                    ReflectionUtils.newIntegrationInstance("com.kneelawk.extramodintegrations.chipped.ChippedIntegration",
                            "Chipped");
        } else {
            INSTANCE = null;
        }
    }

    protected abstract void registerImpl(EmiRegistry registry);

    public static void register(EmiRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.registerImpl(registry);
        } else {
            ExMIMod.logSkipping("Chipped");
        }
    }

}
