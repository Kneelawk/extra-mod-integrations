package com.kneelawk.extramodintegrations.indrev;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.CondenserRecipe;

import com.kneelawk.extramodintegrations.util.LongHolder;
import com.kneelawk.extramodintegrations.util.UIUtils;

public class CondenserEmiRecipe extends IRFluidEmiRecipe<CondenserRecipe> {
    protected CondenserEmiRecipe(CondenserRecipe recipe, LongHolder capacityHolder) {
        super(recipe, capacityHolder);
        checkInputCount(1);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return IRIntegration.CONDENSER_CATEGORY;
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
        widgets.addSlot(getOutput(0), 16 + 36, (44 - 26) / 2).large(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 16 + 6, (44 - 16) / 2);
    }
}
