package com.kneelawk.extramodintegrations.tconstruct.recipe;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;

public class MoldingEmiRecipe extends BasicEmiRecipe {
    public MoldingEmiRecipe(MoldingRecipe recipe) {
        super(TiCCategories.MOLDING, recipe.getId(), 70, 57);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
