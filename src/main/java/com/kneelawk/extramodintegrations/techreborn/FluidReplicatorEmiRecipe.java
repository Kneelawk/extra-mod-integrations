package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;

import java.util.List;

public class FluidReplicatorEmiRecipe extends TREmiRecipe<FluidReplicatorRecipe> {
    private final List<EmiStack> fluidOutput;

    public FluidReplicatorEmiRecipe(FluidReplicatorRecipe recipe) {
        super(recipe);
        FluidInstance instance = recipe.getFluidInstance();
        fluidOutput = List.of(EmiStack.of(instance.getVariant(), instance.getAmount().getRawValue()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegrationImpl.FLUID_REPLICATOR_CATEGORY;
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

        widgets.addTexture(TRTextures.TANK_BASE, 16 + 18 + 24, 0);
        widgets.add(new DynamicFluidSlotWidget(recipe.getFluidInstance(), 16 + 18 + 24 + 4, 4, 22 - 8, 56 - 8, 16 * 1000 * 81))
            .recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 400, 0, 3);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (56 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16, 0);
    }
}
