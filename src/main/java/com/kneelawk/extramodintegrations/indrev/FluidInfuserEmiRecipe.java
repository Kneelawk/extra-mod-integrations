package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.FluidInfuserRecipe;

import com.kneelawk.extramodintegrations.util.LongHolder;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class FluidInfuserEmiRecipe extends IRFluidEmiRecipe<FluidInfuserRecipe> {
    protected FluidInfuserEmiRecipe(FluidInfuserRecipe recipe, LongHolder capacityHolder) {
        super(recipe, capacityHolder);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.FLUID_INFUSE_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 17 + 18 + 36 + 18 + 17;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.add(new IRFluidSlotWidget(getInputFluid(0), 0, 1, getSlotCapacity()));
        widgets.addSlot(getInputItem(0), 17, (44 - 18) / 2);
        UIUtils.cookArrow(widgets, recipe.getTicks(), 17 + 18 + 6, (44 - 16) / 2);
        widgets.addSlot(getOutputItem(0), 17 + 18 + 36, (44 - 18) / 2).recipeContext(this);
        widgets.add(new IRFluidSlotWidget(getOutputFluid(0), 17 + 18 + 36 + 18 + 1, 1, getSlotCapacity()))
            .recipeContext(this);
    }
}
