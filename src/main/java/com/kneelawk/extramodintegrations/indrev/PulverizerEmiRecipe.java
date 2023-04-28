package com.kneelawk.extramodintegrations.indrev;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.PulverizerRecipe;

public class PulverizerEmiRecipe extends IREmiRecipe<PulverizerRecipe> {
    protected PulverizerEmiRecipe(PulverizerRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.PULVERIZE_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 14 + 18 + 36 + 18 * 2;
    }

    @Override
    public int getDisplayHeight() {
        return 32;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 7, (32 - 18) / 2);
        widgets.addSlot(getOutput(0), 7 + 18 + 36, (32 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(1), 7 + 18 + 36 + 18, (32 - 18) / 2).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 7 + 18 + 6, (32 - 16) / 2);
    }
}
