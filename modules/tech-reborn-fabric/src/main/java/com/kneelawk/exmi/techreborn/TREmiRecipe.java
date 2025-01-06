package com.kneelawk.exmi.techreborn;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import reborncore.common.crafting.RebornRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.core.api.ExMILog;

public abstract class TREmiRecipe<R extends RebornRecipe> implements EmiRecipe {
    protected final R recipe;
    protected final @Nullable ResourceLocation id;
    protected final List<EmiIngredient> inputs;
    protected final List<EmiStack> outputs;

    public TREmiRecipe(RecipeHolder<? extends R> recipe) {
        this.recipe = recipe.value();
        id = recipe.id();
        inputs =
            recipe.value().ingredients().stream().map(ing -> EmiIngredient.of(ing.ingredient(), ing.count())).toList();
        outputs = recipe.value().outputs().stream().map(EmiStack::of).toList();
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    protected final void checkInputCount(int count) {
        if (inputs.size() != count) {
            ExMILog.LOG.warn("[ExMI Tech Reborn] Expected recipe {} ({}) to have {} inputs but instead it has {}", id,
                recipe, count, inputs.size());
        }
    }

    protected final void checkOutputCount(int count) {
        if (outputs.size() != count) {
            ExMILog.LOG.warn("[ExMI Tech Reborn] Expected recipe {} ({}) to have {} outputs but instead it has {}", id,
                recipe, count, outputs.size());
        }
    }

    protected EmiIngredient getInput(int index) {
        if (index >= inputs.size()) {
            return EmiStack.EMPTY;
        } else {
            return inputs.get(index);
        }
    }

    protected EmiStack getOutput(int index) {
        if (index >= outputs.size()) {
            return EmiStack.EMPTY;
        } else {
            return outputs.get(index);
        }
    }
}
