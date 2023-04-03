package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import com.kneelawk.extramodintegrations.techreborn.TRIntegration;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.Items;

import static com.kneelawk.extramodintegrations.ExMIMod.id;

public class ExMIPlugin implements EmiPlugin {
    private static final EmiStack BUCKET_STACK = EmiStack.of(Items.BUCKET);
    private static final EmiStack WATER_BUCKET_STACK = EmiStack.of(Items.WATER_BUCKET);

    public static final EmiRecipeCategory FLUID_FROM_CONTAINER_CATEGORY =
        new EmiRecipeCategory(id("fluid_from_container"), BUCKET_STACK, ExMITextures.FLUID_FROM_CAN,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory FLUID_INTO_CONTAINER_CATEGORY =
        new EmiRecipeCategory(id("fluid_into_container"), WATER_BUCKET_STACK, ExMITextures.FLUID_INTO_CAN,
            EmiRecipeSorting.compareInputThenOutput());

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FLUID_FROM_CONTAINER_CATEGORY);
        registry.addCategory(FLUID_INTO_CONTAINER_CATEGORY);

        TRIntegration.register(registry);
        HephaestusIntegration.register(registry);
    }
}
