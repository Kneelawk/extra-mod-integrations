package com.kneelawk.exmi.core.api;

import dev.emi.emi.api.EmiRegistry;

import com.kneelawk.exmi.core.api.util.ReflectionUtils;
import com.kneelawk.exmi.core.impl.ExMIEnableConfig;
import com.kneelawk.exmi.core.impl.ExMIPlatform;

/**
 * Interface implemented by optionally loadable EMI integration plugins.
 */
public interface ExMIPlugin {
    /**
     * Where ExMI plugins actually register their things.
     *
     * @param registry the EMI registry.
     */
    void register(EmiRegistry registry);

    /**
     * Optionally loads and registers an ExMI plugin based on if its target mod is loaded.
     *
     * @param registry       the EMI registry.
     * @param modId          the mod id of the target mod to check.
     * @param modDisplayName the display name of the target mod for use in errors.
     * @param implClass      the integration implementation class.
     */
    static void register(EmiRegistry registry, String modId, String modDisplayName, String implClass) {
        if (ExMIPlatform.INSTANCE.isModLoaded(modId) && ExMIEnableConfig.checkIntegration(modId)) {
            ExMILog.LOG.info("[Extra Mod Integrations] Loading {} Integration...", modDisplayName);
            ExMIPlugin plugin = ReflectionUtils.newIntegrationInstance(ExMIPlugin.class, implClass, modDisplayName);
            if (plugin != null) {
                plugin.register(registry);
                ExMILog.LOG.info("[Extra Mod Integrations] {} Integration Loaded.", modDisplayName);
            }
        } else {
            ExMILog.LOG.info("[Extra Mod Integrations] Skipping {} Integration...", modDisplayName);
        }
    }
}
