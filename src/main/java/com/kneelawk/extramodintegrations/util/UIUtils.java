package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class UIUtils {
    private static final String[] suffixes = {
        "metric.format.0", "metric.format.1", "metric.format.2", "metric.format.3", "metric.format.4",
        "metric.format.5", "metric.format.6", "metric.format.7", "metric.format.8", "metric.format.9"
    };

    public static FormattedCharSequence cookTime(int ticks) {
        float secs = ticks / 20f;
        return gui("cook_time", secs).getVisualOrderText();
    }

    public static void cookTime(WidgetHolder widgets, int ticks, int x, int y) {
        widgets.addText(cookTime(ticks), x, y, 0xFF3F3F3F, false);
    }

    public static Component metricNumber(int number) {
        int power = Mth.clamp((int) Math.log10(number), 0, 9);
        double chopped = (double) number / Math.pow(10, power);
        return gui(suffixes[power], chopped);
    }
}
