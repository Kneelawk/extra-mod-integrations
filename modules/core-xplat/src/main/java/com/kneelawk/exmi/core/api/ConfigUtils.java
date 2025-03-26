package com.kneelawk.exmi.core.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.kneelawk.exmi.core.impl.ExMIPlatform;

public class ConfigUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static <T> T load(Class<T> configClass, String path) {
        Path configPath = ExMIPlatform.INSTANCE.getConfig(path);
        try {
            if (!Files.exists(configPath)) {
                ExMILog.LOG.info("Creating new {} config.", path);
                return configClass.getConstructor().newInstance();
            }
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                return gson.fromJson(reader, configClass);
            }
        } catch (IOException e) {
            ExMILog.LOG.warn("Error loading {} config. Re-creating it from scratch...", path, e);
            try {
                return configClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void write(Object config, String path) {
        Path configPath = ExMIPlatform.INSTANCE.getConfig(path);
        try {
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                gson.toJson(config, writer);
                writer.flush();
            }
        } catch (IOException e) {
            ExMILog.LOG.error("Error writing {} config", path, e);
        }
    }
}
