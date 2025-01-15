package com.kneelawk.exmi.chipped;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiIngredientRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class ChippedEmiRecipe extends EmiIngredientRecipe {
    private final ResourceLocation id;
    private final EmiIngredient ingredient;
    private final List<EmiStack> stacks;

    public ChippedEmiRecipe(ResourceLocation id, Ingredient ingredient) {
        this.id = id;
        this.ingredient = EmiIngredient.of(ingredient);
        this.stacks = this.ingredient.getEmiStacks();
    }

    @Override
    public List<EmiStack> getOutputs() {
        return stacks;
    }

    @Override
    protected EmiIngredient getIngredient() {
        return ingredient;
    }

    @Override
    protected List<EmiStack> getStacks() {
        return stacks;
    }

    @Override
    protected EmiRecipe getRecipeContext(EmiStack stack, int offset) {
        return new ChippedEmiResolutionRecipe(ingredient, stack);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CIntegration.WORKBENCH;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }
}
