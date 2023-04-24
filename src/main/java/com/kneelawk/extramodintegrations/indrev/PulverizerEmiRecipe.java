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
        return IRIntegration.PULVERIZER_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 36 + 53;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 0, (44 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(0), 16 + 36, (44 - 26) / 2).output(true).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 36 + 26, (44 - 26) / 2).output(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 16 + 6, (44 - 18) / 2);
    }
}
