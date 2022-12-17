package com.kneelawk.extramodintegrations.indrev;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.recipes.machines.IRFluidRecipe;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;

import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.util.LongHolder;

public abstract class IRFluidEmiRecipe<R extends IRFluidRecipe> implements EmiRecipe {
    protected final R recipe;
    protected final Identifier id;
    protected final List<EmiIngredient> inputs;
    protected final List<EmiIngredient> inputItems;
    protected final List<EmiStack> outputs;
    protected final List<EmiStack> outputItems;
    protected final LongHolder capacityHolder;

    protected IRFluidEmiRecipe(R recipe, LongHolder capacityHolder) {
        this.recipe = recipe;
        id = recipe.getIdentifier();
        inputItems =
            Arrays.stream(recipe.getInput()).map(entry -> EmiIngredient.of(entry.getIngredient(), entry.getCount()))
                .toList();
        inputs = Stream.concat(
            inputItems.stream(),
            Arrays.stream(recipe.getFluidInput()).map(res -> EmiStack.of(res.resource(), res.amount()))
        ).toList();
        outputItems = Arrays.stream(recipe.getOutputs())
            .map(entry -> EmiStack.of(entry.getStack()).setChance((float) entry.getChance())).toList();
        outputs = Stream.concat(
            outputItems.stream(),
            Arrays.stream(recipe.getFluidOutput()).map(res -> EmiStack.of(res.resource(), res.amount()))
        ).toList();

        this.capacityHolder = capacityHolder;
        maxFluidVolumes(capacityHolder);
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

    protected @Nullable ResourceAmount<FluidVariant> getInputFluid(int index) {
        ResourceAmount<FluidVariant>[] fluidInputs = recipe.getFluidInput();
        if (index >= fluidInputs.length) {
            return null;
        } else {
            return fluidInputs[index];
        }
    }

    protected EmiIngredient getInputItem(int index) {
        if (index >= inputItems.size()) {
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

    protected @Nullable ResourceAmount<FluidVariant> getOutputFluid(int index) {
        ResourceAmount<FluidVariant>[] fluidOutputs = recipe.getFluidOutput();
        if (index >= fluidOutputs.length) {
            return null;
        } else {
            return fluidOutputs[index];
        }
    }

    protected EmiStack getOutputItem(int index) {
        if (index >= outputItems.size()) {
            return EmiStack.EMPTY;
        } else {
            return outputItems.get(index);
        }
    }

    protected long getSlotCapacity() {
        return capacityHolder.getValue();
    }

    private IRFluidEmiRecipe<R> maxFluidVolumes(LongHolder volumeHolder) {
        ResourceAmount<FluidVariant>[] fluidInputs = recipe.getFluidInput();
        ResourceAmount<FluidVariant>[] fluidOutputs = recipe.getFluidOutput();
        long fluidVolume = volumeHolder.getValue();

        for (int i = 0; i < fluidInputs.length; i++) {
            ResourceAmount<FluidVariant> fluid = fluidInputs[i];
            if (fluid.amount() > fluidVolume) {
                fluidVolume = fluid.amount();
            }
        }

        for (int i = 0; i < fluidOutputs.length; i++) {
            ResourceAmount<FluidVariant> fluid = fluidOutputs[i];
            if (fluid.amount() > fluidVolume) {
                fluidVolume = fluid.amount();
            }
        }

        if (fluidVolume > volumeHolder.getValue()) {
            volumeHolder.setValue(fluidVolume);
        }

        return this;
    }
}
