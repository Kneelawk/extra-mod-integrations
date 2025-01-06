package com.kneelawk.exmi.core.api;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.world.item.Items;

import static com.kneelawk.exmi.core.api.ExMIConstants.rl;

/**
 * ExMI utility fields.
 */
public class ExMI {
    private static final EmiStack BUCKET_STACK = EmiStack.of(Items.BUCKET);
    private static final EmiStack WATER_BUCKET_STACK = EmiStack.of(Items.WATER_BUCKET);

    /**
     * Recipe category for draining a fluid container.
     */
    public static final EmiRecipeCategory FLUID_FROM_CONTAINER_CATEGORY =
        new EmiRecipeCategory(rl("fluid_from_container"), BUCKET_STACK, ExMITextures.FLUID_FROM_CAN,
            EmiRecipeSorting.compareOutputThenInput());
    /**
     * Recipe category for filling a fluid container.
     */
    public static final EmiRecipeCategory FLUID_INTO_CONTAINER_CATEGORY =
        new EmiRecipeCategory(rl("fluid_into_container"), WATER_BUCKET_STACK, ExMITextures.FLUID_INTO_CAN,
            EmiRecipeSorting.compareInputThenOutput());
}
