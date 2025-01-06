package com.kneelawk.exmi.techreborn;

import java.util.List;
import java.util.stream.Stream;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.recipe.recipes.IndustrialSawmillRecipe;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.core.api.util.LongHolder;
import com.kneelawk.exmi.core.api.util.UIUtils;

public class IndustrialSawmillEmiRecipe extends TREmiRecipe<IndustrialSawmillRecipe> {
    private final List<EmiIngredient> inputsWithFluids;
    private final LongHolder capacityHolder;

    public IndustrialSawmillEmiRecipe(RecipeHolder<IndustrialSawmillRecipe> recipe, LongHolder capacityHolder) {
        super(recipe);
        this.capacityHolder = capacityHolder;
        FluidInstance instance = recipe.value().fluid();
        long amount = instance.getAmount().getRawValue();
        inputsWithFluids = Stream.concat(inputs.stream(),
            Stream.of(EmiStack.of(instance.fluid(), instance.fluidVariant().getComponents(), amount))).toList();

        if (amount > capacityHolder.getValue()) {
            capacityHolder.setValue(amount);
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.INDUSTRIAL_SAWMILL_CATEGORY;
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

        widgets.add(new TRFluidSlotWidget(recipe.fluid(), 16, 0, capacityHolder.getValue()));

        widgets.addSlot(getOutput(0), 16 + 22 + 2 + 18 + 24, (56 - 18 * 3) / 2).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 22 + 2 + 18 + 24, (56 - 18 * 3) / 2 + 18).recipeContext(this);
        widgets.addSlot(getOutput(2), 16 + 22 + 2 + 18 + 24, (56 - 18 * 3) / 2 + 18 * 2).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 3);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 22 + 2 + 18 + 4, (56 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.time(), 16 + 22 + 2, 0);
    }
}
