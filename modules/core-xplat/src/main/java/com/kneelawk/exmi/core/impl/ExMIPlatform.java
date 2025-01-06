package com.kneelawk.exmi.core.impl;

import com.kneelawk.exmi.core.api.util.ReflectionUtils;

public interface ExMIPlatform {
    ExMIPlatform INSTANCE =
        ReflectionUtils.firstNewInstance(ExMIPlatform.class, "com.kneelawk.exmi.core.fabric.impl.ExMIPlatformFabric",
            "com.kneelawk.exmi.core.neoforge.impl.ExMIPlatformNeoForge");

    boolean isModLoaded(String modId);
}
