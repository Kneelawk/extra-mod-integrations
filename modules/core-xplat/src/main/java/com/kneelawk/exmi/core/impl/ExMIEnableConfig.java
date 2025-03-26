package com.kneelawk.exmi.core.impl;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.kneelawk.exmi.core.api.ConfigUtils;

public class ExMIEnableConfig implements AutoCloseable {
    private static final String PATH = "exmi/enable_config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public HashMap<String, Boolean> enabledIntegrations = new HashMap<>();

    public static boolean checkIntegration(String modId) {
        try (ExMIEnableConfig config = ConfigUtils.load(ExMIEnableConfig.class, PATH)) {
            if (config.enabledIntegrations.containsKey(modId)) {
                return config.enabledIntegrations.get(modId);
            } else {
                config.enabledIntegrations.put(modId, true);
                return true;
            }
        }
    }

    @Override
    public void close() {
        ConfigUtils.write(this, PATH);
    }
}
