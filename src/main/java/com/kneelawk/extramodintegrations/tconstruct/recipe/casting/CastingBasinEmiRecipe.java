package com.kneelawk.extramodintegrations.tconstruct.recipe.casting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.render.EmiTexture;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

public class CastingBasinEmiRecipe extends AbstractCastingEmiRecipe {
    private static final EmiTexture block = new EmiTexture(BACKGROUND_LOC, 117, 16, 16, 16);

    public CastingBasinEmiRecipe(IDisplayableCastingRecipe recipe) {
        super(TiCCategories.CASTING_BASIN, recipe, block);
    }
}
