package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.techreborn.TRIntegration;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.util.Identifier;

import static com.kneelawk.extramodintegrations.ExMIMod.id;

public class ExMIPlugin implements EmiPlugin {
    public static final Identifier SIMPLIFIED_ICONS = id("textures/gui/simplified_icons.png");

    @Override
    public void register(EmiRegistry emiRegistry) {
        TRIntegration.register(emiRegistry);
    }
}
