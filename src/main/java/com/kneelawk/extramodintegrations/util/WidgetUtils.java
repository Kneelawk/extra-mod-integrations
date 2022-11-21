package com.kneelawk.extramodintegrations.util;

import net.minecraft.text.OrderedText;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class WidgetUtils {
    public static OrderedText cookTime(int ticks) {
        return gui("cook_time", ticks).asOrderedText();
    }
}
