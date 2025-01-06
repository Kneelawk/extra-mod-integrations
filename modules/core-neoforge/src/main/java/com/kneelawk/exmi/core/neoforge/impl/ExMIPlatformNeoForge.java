package com.kneelawk.exmi.core.neoforge.impl;

import net.neoforged.fml.ModList;

import com.kneelawk.exmi.core.impl.ExMIPlatform;

public class ExMIPlatformNeoForge implements ExMIPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
