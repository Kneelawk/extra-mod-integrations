package com.kneelawk.exmi.core.api.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.kneelawk.exmi.core.api.ExMILog;

/**
 * General reflection utilities.
 */
public class ReflectionUtils {
    /**
     * Loads a class that implements or extends another class.
     *
     * @param interfaceClass the interface or superclass of the implementation class.
     * @param implClass      the implementation class to load.
     * @param <T>            the type of interface class to return.
     * @return the new instance of the loaded class.
     */
    public static <T> @NotNull T newInstance(Class<T> interfaceClass, String implClass) {
        try {
            return interfaceClass.cast(Class.forName(implClass).getConstructor().newInstance());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Unable to get instance: " + implClass, e);
        }
    }

    /**
     * Tries to load a class that implements or extends another class.
     *
     * @param interfaceClass the interface or superclass of the implementation class.
     * @param implClass      the implementation class to load.
     * @param <T>            the type of interface class to return.
     * @return the new instance of the loaded class if the class could be loaded.
     */
    public static <T> @Nullable T tryNewInstance(Class<T> interfaceClass, String implClass) {
        try {
            return interfaceClass.cast(Class.forName(implClass).getConstructor().newInstance());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Attempts to load implementations of a class until one loads successfully.
     *
     * @param interfaceClass the interface or superclass of the implementation classes.
     * @param implClasses    the list of implementation classes to attempt to load.
     * @param <T>            the type of interface class to return.
     * @return a new instance of the first implementation class that loaded successfully.
     */
    public static <T> @NotNull T firstNewInstance(Class<T> interfaceClass, String... implClasses) {
        for (String implClass : implClasses) {
            T instance = tryNewInstance(interfaceClass, implClass);
            if (instance != null) return instance;
        }

        throw new RuntimeException("Unable to find a suitable implementation of " + interfaceClass + " out of " +
            Arrays.toString(implClasses));
    }

    /**
     * Loads an integration implementation class.
     *
     * @param interfaceClass the interface or superclass of the implementation class.
     * @param implClass      the implementation to load.
     * @param modDisplayName the display name of the mod the implementation class is associated with.
     * @param <T>            the type of interface class to return.
     * @return the new instance of the integration implementation class.
     */
    public static <T> @Nullable T newIntegrationInstance(Class<T> interfaceClass, String implClass,
                                                         String modDisplayName) {
        try {
            return ReflectionUtils.newInstance(interfaceClass, implClass);
        } catch (Exception e) {
            ExMILog.LOG.warn(
                "[Extra Mod Integrations] Failed to load {} integration. EMI <-> {} integration will not work.",
                modDisplayName, modDisplayName, e);
            return null;
        }
    }
}
