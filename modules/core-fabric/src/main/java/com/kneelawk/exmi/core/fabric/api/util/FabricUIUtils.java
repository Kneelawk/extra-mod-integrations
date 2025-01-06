package com.kneelawk.exmi.core.fabric.api.util;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import com.mojang.blaze3d.vertex.PoseStack;

import com.kneelawk.exmi.core.api.util.UIUtils;

public class FabricUIUtils {
    public static void renderFluid(PoseStack matrices, FluidVariant fluid, int x, int areaY,
                                   float areaHeight, float fluidHeight, float fluidWidth) {
        UIUtils.renderFluid(matrices, FluidVariantRendering.getSprites(fluid), FluidVariantRendering.getColor(fluid), x,
            areaY, areaHeight, fluidHeight, fluidWidth);
    }
}
