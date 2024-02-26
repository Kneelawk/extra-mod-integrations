package com.kneelawk.extramodintegrations.tconstruct.recipe;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;

import java.util.List;

public class AlloyEmiRecipe extends BasicEmiRecipe {
    public AlloyEmiRecipe(AlloyRecipe recipe) {
        super(TiCCategories.ALLOY, recipe.getId(), 172, 62);

        this.inputs = recipe.getDisplayInputs()
                .stream()
                .map(l -> l.stream().map(Util::convertFluid).toList())
                .map(EmiIngredient::of)
                .toList();
        this.outputs = List.of(Util.convertFluid(recipe.getOutput()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
