package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;

public class MeltingEmiRecipe extends BasicEmiRecipe {
    public MeltingEmiRecipe(IMeltingRecipe recipe, EmiRecipeCategory category) {
        super(category, recipe.getId(), 132, 40);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
