package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

public class CentrifugeEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public CentrifugeEmiRecipe(RebornRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegrationImpl.CENTRIFUGE_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 18 * 3 + 2;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (56 - 18 * 2 - 2) / 2);
        widgets.addSlot(getInput(1), 16, (56 - 18 * 2 - 2) / 2 + 18 + 2);

        widgets.addSlot(getOutput(0), 16 + 18 + 24, (56 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 18 + 24 + 18 + 1, 0).recipeContext(this);
        widgets.addSlot(getOutput(2), 16 + 18 + 24 + 18 * 2 + 2, (56 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(3), 16 + 18 + 24 + 18 + 1, 56 - 18).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 3);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (56 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16, 0);
    }
}
