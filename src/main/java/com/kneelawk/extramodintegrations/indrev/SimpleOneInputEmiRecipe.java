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
        return 14 + 18 + 36 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 7, (40 - 18) / 2);
        widgets.addSlot(getOutput(0), 7 + 18 + 36, 7).large(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 7 + 18 + 6, (40 - 16) / 2);
    }
}
