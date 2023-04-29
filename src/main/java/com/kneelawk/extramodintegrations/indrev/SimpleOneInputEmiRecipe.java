package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.IRRecipe;

import com.kneelawk.extramodintegrations.util.UIUtils;

public class SimpleOneInputEmiRecipe extends IREmiRecipe<IRRecipe> {
    private final EmiRecipeCategory category;

    protected SimpleOneInputEmiRecipe(IRRecipe recipe, EmiRecipeCategory category) {
        super(recipe);
        this.category = category;
        checkInputCount(1);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public int getDisplayWidth() {
        return 6 + 18 + 36 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 32;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 3, (32 - 18) / 2);
        widgets.addSlot(getOutput(0), 3 + 18 + 36, 3).output(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 3 + 18 + 6, (32 - 16) / 2);
    }
}
