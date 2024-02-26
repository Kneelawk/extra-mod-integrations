package com.kneelawk.extramodintegrations.tconstruct.recipe.casting;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

public class CastingEmiRecipe extends BasicEmiRecipe {
    public CastingEmiRecipe(EmiRecipeCategory category, ICastingRecipe recipe) {
        super(category, recipe.getId(), 117, 54);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
