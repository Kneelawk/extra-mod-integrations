/*
 * Copyright (c) 2020 vectorwing, the Farmer's Delight Refabricated authors
 * License available at https://github.com/MehVahdJukaar/FarmersDelightRefabricated/blob/b6690b2106abc6205021e40f9117c03f36323362/LICENSE
 */

package com.kneelawk.exmi.farmersdelight;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;

import net.minecraft.resources.ResourceLocation;

import static com.kneelawk.exmi.farmersdelight.FDIntegration.res;

public class FDRecipeCategories {
    private static final ResourceLocation SIMPLIFIED_TEXTURES = res("textures/gui/emi/simplified.png");

    public static final EmiRecipeCategory COOKING =
        new EmiRecipeCategory(res("cooking"), FDRecipeWorkstations.COOKING_POT, simplifiedRenderer(0, 0));
    public static final EmiRecipeCategory CUTTING =
        new EmiRecipeCategory(res("cutting"), FDRecipeWorkstations.CUTTING_BOARD, simplifiedRenderer(16, 0));
    public static final EmiRecipeCategory DECOMPOSITION =
        new EmiRecipeCategory(res("decomposition"), FDRecipeWorkstations.ORGANIC_COMPOST, simplifiedRenderer(32, 0));

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) -> {
            draw.blit(SIMPLIFIED_TEXTURES, x, y, u, v, 16, 16, 48, 16);
        };
    }
}
