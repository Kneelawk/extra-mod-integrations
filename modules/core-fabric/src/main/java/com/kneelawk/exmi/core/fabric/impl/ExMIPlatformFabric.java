package com.kneelawk.exmi.core.fabric.impl;

import net.fabricmc.loader.api.FabricLoader;

import com.kneelawk.exmi.core.impl.ExMIPlatform;

public class ExMIPlatformFabric implements ExMIPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
