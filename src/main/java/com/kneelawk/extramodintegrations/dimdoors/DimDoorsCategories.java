package com.kneelawk.extramodintegrations.dimdoors;

import com.kneelawk.extramodintegrations.util.NamedEmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.dimdev.dimdoors.block.ModBlocks;

public class DimDoorsCategories {
    public static final EmiRecipeCategory TESSELATING = new NamedEmiRecipeCategory(
            new ResourceLocation("dimdoors", "tesselating"),
            EmiStack.of(ModBlocks.TESSELATING_LOOM.get()),
            Component.translatable("category.dimdoors.tesselating")
    );
    public static final EmiRecipeCategory DECAYS_INTO = new NamedEmiRecipeCategory(
            new ResourceLocation("dimdoors", "decays_into"),
            EmiStack.of(ModBlocks.UNRAVELED_FENCE.get()),
            Component.translatable("category.dimdoors.decays_into")
    );
}
