package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

public class GrinderEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public GrinderEmiRecipe(RebornRecipe recipe) {
        super(recipe);
        checkInputCount(1);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegrationImpl.GRINDER_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (50 - 18) / 2);
        widgets.addSlot(getOutput(0), 16 + 18 + 24, (50 - 26) / 2).output(true).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 1, 0, 0);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (50 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16, 0);
    }
}
