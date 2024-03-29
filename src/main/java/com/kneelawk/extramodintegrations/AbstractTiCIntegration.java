package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTiCIntegration {
    public static final @Nullable AbstractTiCIntegration INSTANCE;

    static {
        if (FabricLoader.getInstance().isModLoaded("tconstruct")) {
            INSTANCE = ReflectionUtils.newIntegrationInstance("com.kneelawk.extramodintegrations.tconstruct.TiCIntegration",
                    "Hephaestus");
        } else {
            INSTANCE = null;
        }
    }

    protected abstract void registerImpl(EmiRegistry registry);
    protected abstract void initializeImpl(EmiInitRegistry registry);

    public static void register(EmiRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.registerImpl(registry);
        } else {
            ExMIMod.logSkipping("Hephaestus");
        }
    }

    public static void initialize(EmiInitRegistry registry) {
        if (INSTANCE != null) {
            INSTANCE.initializeImpl(registry);
        } else {
            ExMIMod.logSkipping("Hephaestus");
        }
    }

}
