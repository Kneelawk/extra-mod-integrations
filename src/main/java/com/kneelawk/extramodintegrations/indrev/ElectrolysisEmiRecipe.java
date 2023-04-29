package com.kneelawk.extramodintegrations.indrev;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.ElectrolysisRecipe;

public class ElectrolysisEmiRecipe extends IRFluidEmiRecipe<ElectrolysisRecipe>{
    protected ElectrolysisEmiRecipe(ElectrolysisRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.ELECTROLYSIS_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 36 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.add(new IRFluidSlotWidget(getFluidInput(0), 0, 1, 8 * 100 * 81));
        widgets.add(new IRFluidSlotWidget(getFluidOutput(0), 16 + 27, 1, 8 * 100 * 81));
        widgets.add(new IRFluidSlotWidget(getFluidOutput(1), 16 + 45, 1, 8 * 100 * 81));

        UIUtils.cookArrow(widgets, recipe.getTicks(), 16 + 2, (44 - 16) / 2);
    }
}
