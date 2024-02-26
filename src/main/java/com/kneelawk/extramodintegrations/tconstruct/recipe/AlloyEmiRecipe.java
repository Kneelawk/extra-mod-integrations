package com.kneelawk.extramodintegrations.tconstruct.recipe;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;

public class AlloyEmiRecipe extends BasicEmiRecipe {
    public AlloyEmiRecipe(AlloyRecipe recipe) {
        super(TiCCategories.ALLOY, recipe.getId(), 172, 62);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
