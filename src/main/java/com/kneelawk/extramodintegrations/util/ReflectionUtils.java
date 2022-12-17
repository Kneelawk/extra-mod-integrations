package com.kneelawk.extramodintegrations.util;

import java.lang.reflect.InvocationTargetException;

import org.jetbrains.annotations.Nullable;

import com.kneelawk.extramodintegrations.ExMIMod;

public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String name) throws ClassNotFoundException {
        try {
            return (T) Class.forName(name).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Unable to get instance: " + name, e);
        }
    }

    public static <T> @Nullable T newIntegrationInstance(String name, String modDisplayName) {
        try {
            return ReflectionUtils.newInstance(name);
        } catch (ClassNotFoundException e) {
            ExMIMod.LOGGER.warn("[Extra Mod Integrations] Attempted to load " + modDisplayName + " integration, but " +
                "Extra Mod Integrations was compiled with " + modDisplayName + " integration disabled. " +
                "EMI <-> " + modDisplayName + " integration will not work.");
            return null;
        }
    }
}
