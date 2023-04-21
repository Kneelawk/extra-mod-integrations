package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

public class AssemblingMachineEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public AssemblingMachineEmiRecipe(RebornRecipe recipe) {
        super(recipe);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.ASSEMBLING_MACHINE_CATEGORY;
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
        widgets.addSlot(getInput(0), 16, (50 - 18 * 2 - 2) / 2);
        widgets.addSlot(getInput(1), 16, (50 - 18 * 2 - 2) / 2 + 18 + 2);
        widgets.addSlot(getOutput(0), 16 + 18 + 24, (50 - 26) / 2).output(true).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 0);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (50 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16 + 18 + 2, 0);
    }
}
