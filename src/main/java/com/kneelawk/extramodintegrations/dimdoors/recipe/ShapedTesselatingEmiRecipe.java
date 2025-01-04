package com.kneelawk.extramodintegrations.dimdoors.recipe;

import com.google.common.collect.Lists;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.dimdev.dimdoors.recipe.ShapedTesselatingRecipe;

import java.util.List;

public class ShapedTesselatingEmiRecipe extends AbstractTesselatingEmiRecipe {
    public ShapedTesselatingEmiRecipe(ShapedTesselatingRecipe recipe) {
        super(padIngredients(recipe), EmiStack.of(recipe.getResultItem(null)), recipe.weavingTime(), recipe.getId(), false);
    }

    private static List<EmiIngredient> padIngredients(ShapedTesselatingRecipe recipe) {
        List<EmiIngredient> list = Lists.newArrayList();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
                    list.add(EmiStack.EMPTY);
                } else {
                    list.add(EmiIngredient.of(recipe.getIngredients().get(i++)));
                }
            }
        }
        return list;
    }
}
