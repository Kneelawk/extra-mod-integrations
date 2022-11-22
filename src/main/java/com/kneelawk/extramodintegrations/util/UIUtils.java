package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.OrderedText;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class UIUtils {
    public static OrderedText cookTime(int ticks) {
        return gui("cook_time", ticks).asOrderedText();
    }

    public static void cookTime(WidgetHolder widgets, int ticks, int x, int y) {
        widgets.addText(cookTime(ticks), x, y, 0xFF3F3F3F, false);
    }
}
