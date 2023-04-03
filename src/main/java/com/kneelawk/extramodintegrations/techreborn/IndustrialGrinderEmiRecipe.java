package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.IndustrialGrinderRecipe;

import java.util.List;
import java.util.stream.Stream;

public class IndustrialGrinderEmiRecipe extends TREmiRecipe<IndustrialGrinderRecipe> {
    private final List<EmiIngredient> inputsWithFluids;

    public IndustrialGrinderEmiRecipe(IndustrialGrinderRecipe recipe) {
        super(recipe);
        FluidInstance instance = recipe.getFluidInstance();
        inputsWithFluids = Stream.concat(inputs.stream(),
            Stream.of(EmiStack.of(instance.getVariant(), instance.getAmount().getRawValue()))).toList();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegrationImpl.INDUSTRIAL_GRINDER_CATEGORY;
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
        return 18 * 4;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16 + 22 + 2, 18 * 3 / 2);
        widgets.add(new DynamicFluidSlotWidget(recipe.getFluidInstance(), 16, (18 * 4 - 56) / 2, 22, 56, 16 * 1000 * 81))
                .underlay(TRTextures.TANK_BASE)
                .overlay(TRTextures.TANK_GRADUATION);

        for (int i = 0; i < 4; i++) {
            widgets.addSlot(getOutput(i), 16 + 22 + 2 + 18 + 24, i * 18).recipeContext(this);
        }

        TRUIUtils.energyBar(widgets, recipe, 10, 0, (18 * 4 - 50) / 2);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 22 + 2 + 18 + 4, (18 * 4 - 10) / 2);
    }
}
