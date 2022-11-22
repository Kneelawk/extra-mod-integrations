package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.OrderedText;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class UIUtils {
    public static OrderedText cookTime(int ticks) {
        float secs = ticks / 20f;
        return gui("cook_time", secs).asOrderedText();
    }

    public static void cookTime(WidgetHolder widgets, int ticks, int x, int y) {
        widgets.addText(cookTime(ticks), x, y, 0xFF3F3F3F, false);
    }
}
