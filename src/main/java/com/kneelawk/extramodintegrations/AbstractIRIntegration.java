package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.util.ReflectionUtils;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractIRIntegration {
    @Nullable
    public static final AbstractIRIntegration INSTANCE;

    static {
        @Nullable AbstractIRIntegration instance;
        if (FabricLoader.getInstance().isModLoaded("indrev")) {
            try {
                instance =
                    ReflectionUtils.newInstance("com.kneelawk.extramodintegrations.industrialrevolution.IRIntegration");
            } catch (ClassNotFoundException e) {
                ExMIMod.LOGGER.warn("[Extra Mod Integrations] Attempted to load Industrial Revolution integration, but " +
                    "Extra Mod Integrations was compiled with Industrial Revolution integration disabled. " +
                    "EMI <-> Industrial Revolution integration will not work.");
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
