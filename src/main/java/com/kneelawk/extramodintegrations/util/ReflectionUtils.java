package com.kneelawk.extramodintegrations.util;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String name) {
        try {
            return (T) Class.forName(name).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException("Unable to get instance: " + name, e);
        }
    }
}
