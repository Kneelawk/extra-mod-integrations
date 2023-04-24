package com.kneelawk.extramodintegrations.indrev;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.SawmillRecipe;

public class SawmillEmiRecipe extends IREmiRecipe<SawmillRecipe> {
    protected SawmillEmiRecipe(SawmillRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.SAWMILL_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 36 + 28;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 0, (75 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(0), 16 + 36, (75 - 18) / 4 - 10).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 36, (75 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(2), 16 + 36, (75 - 18) / 4 * 3 + 10).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 16 + 6, (75 - 18) / 2);
    }
}
