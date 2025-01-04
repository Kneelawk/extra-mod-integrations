package com.kneelawk.extramodintegrations.appeng;

import dev.emi.emi.api.render.EmiTexture;
import net.minecraft.resources.ResourceLocation;

public class AE2Textures {
    public static final EmiTexture INSCRIBER_BACKGROUND = new EmiTexture(gui("inscriber.png"), 44, 15, 97, 64);

    private static ResourceLocation gui(String texture) {
        return new ResourceLocation("ae2", "textures/guis/" + texture);
    }
}
