package com.kneelawk.exmi.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the common ExMI log.
 */
public class ExMILog {
    /**
     * The common ExMI log.
     */
    public static final Logger LOG = LoggerFactory.getLogger(ExMIConstants.MOD_ID);

    public static void logLoading(String displayName) {
        LOG.info("[Extra Mod Integrations] Loading {} Integration...", displayName);
    }
}
