package com.kneelawk.exmi.isns;

import com.kneelawk.exmi.core.api.ConfigUtils;

public class ISNSConfig implements AutoCloseable {
    private static final String PATH = "exmi/irons_spellbooks.json";

    public int maxImbueRecipes = -1;

    public static int getMaxImbueRecipes() {
        try (ISNSConfig config = ConfigUtils.load(ISNSConfig.class, PATH)) {
            return config.maxImbueRecipes;
        }
    }

    @Override
    public void close() {
        ConfigUtils.write(this, PATH);
    }
}
