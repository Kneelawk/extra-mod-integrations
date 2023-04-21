package com.kneelawk.extramodintegrations.indrev;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.recipes.machines.IRRecipe;

import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.ExMIMod;

public abstract class IREmiRecipe<R extends IRRecipe> implements EmiRecipe {
    protected final R recipe;
    protected final Identifier id;
    protected final List<EmiIngredient> inputs;
    protected final List<EmiStack> outputs;

    protected IREmiRecipe(R recipe) {
        this.recipe = recipe;
        id = recipe.getIdentifier();
        inputs =
            Arrays.stream(recipe.getInput()).map(entry -> EmiIngredient.of(entry.getIngredient(), entry.getCount()))
                .toList();
        outputs = Arrays.stream(recipe.getOutputs())
            .map(entry -> EmiStack.of(entry.getStack()).setChance((float) entry.getChance())).toList();
    }

    @Override
    public @NotNull Identifier getId() {
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
            ExMIMod.LOGGER.warn("Expected recipe {} ({}) to have {} inputs but instead it has {}", id, recipe, count,
                inputs.size());
        }
    }

    protected final void checkOutputCount(int count) {
        if (outputs.size() != count) {
            ExMIMod.LOGGER.warn("Expected recipe {} ({}) to have {} outputs but instead it has {}", id, recipe, count,
                outputs.size());
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
