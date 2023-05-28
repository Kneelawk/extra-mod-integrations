package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.ModuleRecipe;

import com.kneelawk.extramodintegrations.util.UIUtils;

public class ModuleEmiRecipe extends IREmiRecipe<ModuleRecipe> {
    protected ModuleEmiRecipe(ModuleRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.MODULES_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 18 * 5;
    }

    @Override
    public int getDisplayHeight() {
        return 18 * 5;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int inputCount = inputs.size();
        double angleSegment = Math.PI * 2.0 / inputCount;

        for (int i = 0; i < inputCount; i++) {
            int segmentIndex = (i % 2 == 0 ? 1 : -1) * ((i + 1) / 2);
            double angle = segmentIndex * angleSegment;
            int x = 18 * 5 / 2 + (int) (18 * 2 * Math.sin(angle)) - 18 / 2;
            int y = 18 * 5 / 2 + (int) (18 * 2 * -Math.cos(angle)) - 18 / 2;
            widgets.addSlot(getInput(i), x, y);
        }

        widgets.addSlot(getOutput(0), (18 * 5 - 26) / 2, (18 * 5 - 26) / 2).large(true).recipeContext(this);

        UIUtils.cookTime(widgets, recipe.getTicks(), 0, 0);
    }
}
