package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.render.EmiTexture;
import net.minecraft.resources.ResourceLocation;

public class IRTextures {
    public static final EmiTexture TANK_BOTTOM = new EmiTexture(gui("tank_bottom.png"), 0, 0, 16, 43, 16, 43, 16, 43);
    public static final EmiTexture TANK_TOP = new EmiTexture(gui("tank_top.png"), 0, 0, 16, 43, 16, 43, 16, 43);

    private static ResourceLocation gui(String texture) {
        return new ResourceLocation("indrev", "textures/gui/" + texture);
    }
}
