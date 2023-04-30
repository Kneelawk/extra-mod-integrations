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
        return 18 + 36 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 26 * 2;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 0, (26 - 18) / 2);
        widgets.addSlot(getOutput(0), 18 + 36, 0).output(true).recipeContext(this);
        widgets.addSlot(getOutput(1), 18 + 36 + 4, 26 + 4).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 18 + 6, (26 - 16) / 2);
    }
}
