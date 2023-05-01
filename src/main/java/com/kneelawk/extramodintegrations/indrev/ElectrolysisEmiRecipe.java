package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.ElectrolysisRecipe;

import com.kneelawk.extramodintegrations.util.LongHolder;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class ElectrolysisEmiRecipe extends IRFluidEmiRecipe<ElectrolysisRecipe> {
    protected ElectrolysisEmiRecipe(ElectrolysisRecipe recipe, LongHolder capacityHolder) {
        super(recipe, capacityHolder);
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
        widgets.add(new IRFluidSlotWidget(getInputFluid(0), 0, 1, getSlotCapacity()));
        widgets.add(new IRFluidSlotWidget(getOutputFluid(0), 16 + 27, 1, getSlotCapacity()));
        widgets.add(new IRFluidSlotWidget(getOutputFluid(1), 16 + 45, 1, getSlotCapacity()));

        UIUtils.cookArrow(widgets, recipe.getTicks(), 16 + 2, (44 - 16) / 2);
    }
}
