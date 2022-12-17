package com.kneelawk.extramodintegrations.indrev;

import java.util.List;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import me.steven.indrev.gui.screenhandlers.IRGuiScreenHandler;

import net.minecraft.screen.slot.Slot;

public class SimpleRecipeHandler<T extends IRGuiScreenHandler> implements StandardRecipeHandler<T> {
    private final EmiRecipeCategory category;
    private final List<Integer> inputSlots;
    private final int inventoryStart;

    public SimpleRecipeHandler(EmiRecipeCategory category, List<Integer> inputSlots, int inventoryStart) {
        this.category = category;
        this.inputSlots = inputSlots;
        this.inventoryStart = inventoryStart;
    }

    @Override
    public List<Slot> getInputSources(T handler) {
        List<Slot> slots = Lists.newArrayList();
        inputSlots.stream().map(handler::getSlot).forEach(slots::add);
        IntStream.range(inventoryStart, inventoryStart + 36).mapToObj(handler::getSlot).forEach(slots::add);
        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(T handler) {
        return inputSlots.stream().map(handler.slots::get).toList();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == category && recipe.supportsRecipeTree();
    }
}
