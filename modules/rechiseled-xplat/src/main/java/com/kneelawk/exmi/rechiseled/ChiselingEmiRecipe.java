package com.kneelawk.exmi.rechiseled;

import java.util.List;
import java.util.stream.Stream;

import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiIngredientRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiResolutionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.resources.ResourceLocation;

public class ChiselingEmiRecipe extends EmiIngredientRecipe {
    private final ResourceLocation id;
    private final EmiIngredient ingredient;
    private final List<EmiStack> stacks;

    public ChiselingEmiRecipe(ChiselingRecipe recipe) {
        id = recipe.getRecipeId().withPrefix("/");
        stacks = recipe.getEntries().stream().flatMap(e -> {
            if (e.hasRegularItem() && e.hasConnectingItem()) {
                return Stream.of(e.getRegularItem(), e.getConnectingItem());
            } else if (e.hasRegularItem()) {
                return Stream.of(e.getRegularItem());
            } else if (e.hasConnectingItem()) {
                return Stream.of(e.getConnectingItem());
            } else {
                return Stream.of();
            }
        }).map(EmiStack::of).toList();
        ingredient = EmiIngredient.of(stacks);
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
    public List<EmiStack> getOutputs() {
        return stacks;
    }

    @Override
    protected EmiRecipe getRecipeContext(EmiStack stack, int offset) {
        return new EmiResolutionRecipe(ingredient, stack);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RIntegration.CHISELING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }
}
