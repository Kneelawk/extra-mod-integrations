package com.kneelawk.exmi.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class SimpleRecipeHandler<T extends AbstractContainerMenu> implements StandardRecipeHandler<T> {
    private final EmiRecipeCategory category;
    private final int recipeSlotStart;
    private final int recipeSlotCount;
    private final int inventorySlotStart;
    private final int inventorySlotCount;
    private final OptionalInt outputSlot;

    public SimpleRecipeHandler(EmiRecipeCategory category, int recipeSlotStart, int recipeSlotCount,
                               int inventorySlotStart, int inventorySlotCount) {
        this(category, recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount, OptionalInt.empty());
    }

    public SimpleRecipeHandler(EmiRecipeCategory category, int recipeSlotStart, int recipeSlotCount,
                               int inventorySlotStart, int inventorySlotCount, int outputSlot) {
        this(category, recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount,
            OptionalInt.of(outputSlot));
    }

    public SimpleRecipeHandler(EmiRecipeCategory category, int recipeSlotStart, int recipeSlotCount,
                               int inventorySlotStart, int inventorySlotCount, OptionalInt outputSlot) {
        this.category = category;
        this.recipeSlotStart = recipeSlotStart;
        this.recipeSlotCount = recipeSlotCount;
        this.inventorySlotStart = inventorySlotStart;
        this.inventorySlotCount = inventorySlotCount;
        this.outputSlot = outputSlot;
    }

    @Override
    public List<Slot> getInputSources(T handler) {
        List<Slot> slots = new ArrayList<>();

        for (int i = recipeSlotStart; i < recipeSlotCount + recipeSlotStart; i++) {
            slots.add(handler.getSlot(i));
        }

        for (int i = inventorySlotStart; i < inventorySlotCount + inventorySlotStart; i++) {
            slots.add(handler.getSlot(i));
        }

        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(T handler) {
        List<Slot> slots = new ArrayList<>();

        for (int i = recipeSlotStart; i < recipeSlotCount + recipeSlotStart; i++) {
            slots.add(handler.getSlot(i));
        }

        return slots;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == category && recipe.supportsRecipeTree();
    }

    @Override
    public @Nullable Slot getOutputSlot(T handler) {
        if (outputSlot.isPresent()) {
            return handler.getSlot(outputSlot.getAsInt());
        } else {
            return null;
        }
    }
}
