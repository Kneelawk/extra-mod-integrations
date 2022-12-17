package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.LongHolder;
import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;

import java.util.List;

public class FluidReplicatorEmiRecipe extends TREmiRecipe<FluidReplicatorRecipe> {
    private final List<EmiStack> fluidOutput;
    private final LongHolder capacityHolder;

    public FluidReplicatorEmiRecipe(FluidReplicatorRecipe recipe, LongHolder capacityHolder) {
        super(recipe);
        this.capacityHolder = capacityHolder;
        FluidInstance instance = recipe.getFluidInstance();
        long amount = instance.getAmount().getRawValue();
        fluidOutput = List.of(EmiStack.of(instance.getVariant(), amount));

        if (amount > capacityHolder.getValue()) {
            capacityHolder.setValue(amount);
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.FLUID_REPLICATOR_CATEGORY;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return fluidOutput;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 22;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (56 - 18) / 2);

        widgets.add(new TRFluidSlotWidget(recipe.getFluidInstance(), 16 + 18 + 24, 0, capacityHolder.getValue()))
            .recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 400, 0, 3);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (56 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16, 0);
    }
}
