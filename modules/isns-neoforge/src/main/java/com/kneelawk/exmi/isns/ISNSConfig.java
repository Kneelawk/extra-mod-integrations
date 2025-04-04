package com.kneelawk.exmi.isns;

import java.util.stream.Stream;

import com.kneelawk.exmi.core.api.ConfigUtils;

public class ISNSConfig implements AutoCloseable {
    private static final String PATH = "exmi/irons_spellbooks.json";

    public int maxGatheredItems = 1000;
    public int maxGatheredPotions = 1000;
    public int maxGatheredPotionIngredients = 1000;
    public int maxImbueRecipes = 5000;
    public int maxUpgradeRecipes = 5000;

    public static <T> Stream<T> limitMaxGatheredItems(Stream<T> stream) {
        try (ISNSConfig config = ConfigUtils.load(ISNSConfig.class, PATH)) {
            return limit(stream, config.maxGatheredItems);
        }
    }

    public static <T> Stream<T> limitMaxGatheredPotions(Stream<T> stream) {
        try (ISNSConfig config = ConfigUtils.load(ISNSConfig.class, PATH)) {
            return limit(stream, config.maxGatheredPotions);
        }
    }

    public static <T> Stream<T> limitMaxGatheredPotionIngredients(Stream<T> stream) {
        try (ISNSConfig config = ConfigUtils.load(ISNSConfig.class, PATH)) {
            return limit(stream, config.maxGatheredPotionIngredients);
        }
    }

    public static <T> Stream<T> limitMaxImbueRecipes(Stream<T> stream) {
        try (ISNSConfig config = ConfigUtils.load(ISNSConfig.class, PATH)) {
            return limit(stream, config.maxImbueRecipes);
        }
    }

    public static <T> Stream<T> limitMaxUpgradeRecipes(Stream<T> stream) {
        try (ISNSConfig config = ConfigUtils.load(ISNSConfig.class, PATH)) {
            return limit(stream, config.maxUpgradeRecipes);
        }
    }

    public static <T> Stream<T> limit(Stream<T> stream, int by) {
        if (by < 0) {
            return stream;
        } else {
            return stream.limit(by);
        }
    }

    @Override
    public void close() {
        ConfigUtils.write(this, PATH);
    }
}
