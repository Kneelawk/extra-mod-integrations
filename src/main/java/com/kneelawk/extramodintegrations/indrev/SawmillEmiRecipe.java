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
        return 18 + 36 + 18 * 2;
    }

    @Override
    public int getDisplayHeight() {
        return 18 * 2;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 0, 18 / 2);
        widgets.addSlot(getOutput(0), 18 + 36, 0).recipeContext(this);
        widgets.addSlot(getOutput(1), 18 + 36 + 18, 0).recipeContext(this);
        widgets.addSlot(getOutput(2), 18 + 36, 18).recipeContext(this);
        widgets.addSlot(getOutput(3), 18 + 36 + 18, 18).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 18 + 6, (18 * 2 - 16) / 2);
    }
}
