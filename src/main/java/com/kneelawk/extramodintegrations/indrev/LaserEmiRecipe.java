package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.LaserRecipe;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class LaserEmiRecipe extends IREmiRecipe<LaserRecipe> {
    protected LaserEmiRecipe(LaserRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.LASER_CATEGORY;
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
        widgets.addSlot(getOutput(0), 7 + 18 + 36, 7).output(true).recipeContext(this);

        widgets.addFillingArrow(7 + 18 + 6, (40 - 16) / 2, 5 * 60 * 1000);
        widgets.addText(ExMIMod.gui("indrev.energy", UIUtils.metricNumber(recipe.getTicks())), 0, 0, 0xFF3F3F3F, false);
    }
}
