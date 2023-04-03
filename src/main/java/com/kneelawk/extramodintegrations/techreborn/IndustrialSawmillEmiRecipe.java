package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe;

import java.util.List;
import java.util.stream.Stream;

public class IndustrialSawmillEmiRecipe extends TREmiRecipe<IndustrialSawmillRecipe> {
    private final List<EmiIngredient> inputsWithFluids;

    public IndustrialSawmillEmiRecipe(IndustrialSawmillRecipe recipe) {
        super(recipe);
        FluidInstance instance = recipe.getFluidInstance();
        inputsWithFluids = Stream.concat(inputs.stream(),
            Stream.of(EmiStack.of(instance.getVariant(), instance.getAmount().getRawValue()))).toList();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegrationImpl.INDUSTRIAL_SAWMILL_CATEGORY;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputsWithFluids;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 22 + 2 + 18 + 24 + 18;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16 + 22 + 2, (56 - 18) / 2);

        widgets.add(new DynamicFluidSlotWidget(recipe.getFluidInstance(), 16, 0, 22, 56, 16 * 1000 * 81))
                .underlay(TRTextures.TANK_BASE)
                .overlay(TRTextures.TANK_GRADUATION);

        widgets.addSlot(getOutput(0), 16 + 22 + 2 + 18 + 24, (56 - 18 * 3) / 2).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 22 + 2 + 18 + 24, (56 - 18 * 3) / 2 + 18).recipeContext(this);
        widgets.addSlot(getOutput(2), 16 + 22 + 2 + 18 + 24, (56 - 18 * 3) / 2 + 18 * 2).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 3);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 22 + 2 + 18 + 4, (56 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16 + 22 + 2, 0);
    }
}
