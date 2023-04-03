package com.kneelawk.extramodintegrations.techreborn;

import dev.emi.emi.api.render.EmiTexture;
import net.minecraft.resources.ResourceLocation;

public class TRTextures {
    public static final ResourceLocation REBORNCORE_GUI_ELEMENTS =
        new ResourceLocation("reborncore", "textures/gui/guielements.png");

    public static final EmiTexture ENERGY_BAR_EMPTY = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 126, 150, 14, 50);
    public static final EmiTexture ENERGY_BAR_FULL = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 140, 150, 14, 50);

    public static final EmiTexture TANK_BASE = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 194, 26, 22, 56);
    public static final EmiTexture TANK_GRADUATION = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 194, 82, 16, 50);

    public static final EmiTexture ARROW_RIGHT_EMPTY = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 58, 150, 16, 10);
    public static final EmiTexture ARROW_RIGHT_FULL = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 74, 150, 16, 10);
    public static final EmiTexture ARROW_LEFT_EMPTY = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 74, 160, 16, 10);
    public static final EmiTexture ARROW_LEFT_FULL = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 58, 160, 16, 10);
    public static final EmiTexture ARROW_UP_EMPTY = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 58, 170, 10, 16);
    public static final EmiTexture ARROW_UP_FULL = new EmiTexture(REBORNCORE_GUI_ELEMENTS, 68, 170, 10, 16);
}
