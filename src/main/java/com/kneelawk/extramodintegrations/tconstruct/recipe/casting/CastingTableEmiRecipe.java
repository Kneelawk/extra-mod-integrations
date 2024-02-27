package com.kneelawk.extramodintegrations.tconstruct.recipe.casting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.render.EmiTexture;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

public class CastingTableEmiRecipe extends AbstractCastingEmiRecipe {
    private static final EmiTexture block = new EmiTexture(BACKGROUND_LOC, 117, 0, 16, 16);

    public CastingTableEmiRecipe(IDisplayableCastingRecipe recipe) {
        super(TiCCategories.CASTING_TABLE, recipe, block);
    }
}
