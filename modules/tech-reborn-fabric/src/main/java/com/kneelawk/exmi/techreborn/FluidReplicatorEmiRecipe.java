package com.kneelawk.exmi.techreborn;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.recipe.recipes.FluidReplicatorRecipe;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.core.api.util.LongHolder;
import com.kneelawk.exmi.core.api.util.UIUtils;

public class FluidReplicatorEmiRecipe extends TREmiRecipe<FluidReplicatorRecipe> {
    private final List<EmiStack> fluidOutput;
    private final LongHolder capacityHolder;

    public FluidReplicatorEmiRecipe(RecipeHolder<FluidReplicatorRecipe> recipe, LongHolder capacityHolder) {
        super(recipe);
        this.capacityHolder = capacityHolder;
        FluidInstance instance = recipe.value().fluid();
        long amount = instance.getAmount().getRawValue();
        fluidOutput = List.of(EmiStack.of(instance.fluid(), instance.fluidVariant().getComponents(), amount));

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

        widgets.add(new TRFluidSlotWidget(recipe.fluid(), 16 + 18 + 24, 0, capacityHolder.getValue()))
            .recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 400, 0, 3);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (56 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.time(), 16, 0);
    }
}
