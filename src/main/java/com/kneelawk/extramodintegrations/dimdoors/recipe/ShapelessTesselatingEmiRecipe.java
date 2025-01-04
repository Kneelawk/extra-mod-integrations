package com.kneelawk.extramodintegrations.dimdoors.recipe;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.dimdev.dimdoors.recipe.TesselatingShapelessRecipe;

public class ShapelessTesselatingEmiRecipe extends AbstractTesselatingEmiRecipe {
    public ShapelessTesselatingEmiRecipe(TesselatingShapelessRecipe recipe) {
        super(recipe.getIngredients().stream().map(EmiIngredient::of).toList(), EmiStack.of(recipe.getResultItem(null)), recipe.weavingTime(), recipe.getId(), true);
    }
}
