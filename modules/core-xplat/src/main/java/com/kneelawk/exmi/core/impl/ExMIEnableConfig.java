package com.kneelawk.exmi.core.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.kneelawk.exmi.core.api.ExMILog;

public class ExMIEnableConfig implements AutoCloseable {
    private static final String PATH = "exmi/enable_config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public HashMap<String, Boolean> enabledIntegrations = new HashMap<>();

    public static boolean checkIntegration(String modId) {
        try (ExMIEnableConfig config = load()) {
            if (config.enabledIntegrations.containsKey(modId)) {
                return config.enabledIntegrations.get(modId);
            } else {
                config.enabledIntegrations.put(modId, true);
                return true;
            }
        }
    }

    public static ExMIEnableConfig load() {
        Path configPath = ExMIPlatform.INSTANCE.getConfig(PATH);
        try {
            if (!Files.exists(configPath)) {
                ExMILog.LOG.info("Creating new ExMI enable config.");
                return new ExMIEnableConfig();
            }
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                return gson.fromJson(reader, ExMIEnableConfig.class);
            }
        } catch (IOException e) {
            ExMILog.LOG.warn("Error loading ExMI enable config. Re-creating it from scratch...", e);
            return new ExMIEnableConfig();
        }
    }

    @Override
    public void close() {
        Path configPath = ExMIPlatform.INSTANCE.getConfig(PATH);
        try {
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                gson.toJson(this, writer);
                writer.flush();
            }
        } catch (IOException e) {
            ExMILog.LOG.error("Error writing ExMI enable config", e);
        }
    }
}
