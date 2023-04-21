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
        return 8 + 18 + 36 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 34;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 4, 4 + (26 - 18) / 2);
        widgets.addSlot(getOutput(0), 4 + 18 + 36, 4).output(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 4 + 18 + 6, 4 + (26 - 18) / 2);
    }
}
