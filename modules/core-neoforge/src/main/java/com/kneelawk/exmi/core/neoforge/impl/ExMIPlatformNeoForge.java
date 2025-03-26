package com.kneelawk.exmi.core.neoforge.impl;

import java.nio.file.Path;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import com.kneelawk.exmi.core.impl.ExMIPlatform;

public class ExMIPlatformNeoForge implements ExMIPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Path getConfig(String path) {
        return FMLLoader.getGamePath().resolve("config").resolve(path);
    }
}
