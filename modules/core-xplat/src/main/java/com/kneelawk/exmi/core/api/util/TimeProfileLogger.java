package com.kneelawk.exmi.core.api.util;

import java.time.Duration;
import java.time.Instant;

import com.kneelawk.exmi.core.api.ExMILog;

/**
 * Logger for profiling.
 * 
 * @deprecated do not use in production code
 */
@Deprecated(forRemoval = true)
public class TimeProfileLogger implements AutoCloseable {
    private final String name;
    private final Instant start;

    public TimeProfileLogger(String name) {
        this.name = name;
        start = Instant.now();
    }

    @Override
    public void close() {
        Instant end = Instant.now();
        Duration time = Duration.between(start, end);
        ExMILog.LOG.info("[Extra Mod Integrations] >> {} took {}ms", name, time.toMillis());
    }
}
