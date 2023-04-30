package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.SmelterRecipe;

import com.kneelawk.extramodintegrations.util.LongHolder;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class SmelterEmiRecipe extends IRFluidEmiRecipe<SmelterRecipe> {
    protected SmelterEmiRecipe(SmelterRecipe recipe, LongHolder capacityHolder) {
        super(recipe, capacityHolder);
        checkInputCount(1);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.SMELTER_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 18 + 36 + 16;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 0, (44 - 18) / 2);
        UIUtils.cookArrow(widgets, recipe.getTicks(), 18 + 6, (44 - 16) / 2);
        widgets.add(new IRFluidSlotWidget(getOutputFluid(0), 18 + 36, 1, getSlotCapacity())).recipeContext(this);
    }
}
