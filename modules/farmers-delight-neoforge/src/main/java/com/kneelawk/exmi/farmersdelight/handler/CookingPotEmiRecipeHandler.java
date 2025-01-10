/*
 * Copyright (c) 2020 vectorwing, the Farmer's Delight Refabricated authors
 * License available at https://github.com/MehVahdJukaar/FarmersDelightRefabricated/blob/b6690b2106abc6205021e40f9117c03f36323362/LICENSE
 */

package com.kneelawk.exmi.farmersdelight.handler;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

import net.minecraft.world.inventory.Slot;

import com.kneelawk.exmi.farmersdelight.FDRecipeCategories;

public class CookingPotEmiRecipeHandler implements StandardRecipeHandler<CookingPotMenu> {
    @Override
    public List<Slot> getInputSources(CookingPotMenu handler) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 0; i < 7; ++i) {
            slots.add(handler.getSlot(i));
        }

        for (int i = 9; i < 9 + 36; ++i) {
            slots.add(handler.getSlot(i));
        }

        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(CookingPotMenu handler) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 0; i < 7; ++i) {
            slots.add(handler.getSlot(i));
        }

        return slots;
    }

    @Override
    public @Nullable Slot getOutputSlot(CookingPotMenu handler) {
        return handler.slots.get(8);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == FDRecipeCategories.COOKING && recipe.supportsRecipeTree();
    }
}
