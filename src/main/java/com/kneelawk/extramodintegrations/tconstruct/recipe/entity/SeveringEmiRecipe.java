package com.kneelawk.extramodintegrations.tconstruct.recipe.entity;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe;

public class SeveringEmiRecipe extends BasicEmiRecipe {
    public SeveringEmiRecipe(SeveringRecipe recipe) {
        super(TiCCategories.SEVERING, recipe.getId(), 100, 38);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
