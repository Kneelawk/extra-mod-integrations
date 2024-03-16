package com.kneelawk.extramodintegrations.tconstruct.handler;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;
import slimeknights.tconstruct.tables.menu.CraftingStationContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class CraftingStationHandler implements StandardRecipeHandler<CraftingStationContainerMenu> {
    @Override
    public List<Slot> getInputSources(CraftingStationContainerMenu handler) {
        List<Slot> slots = new ArrayList<>(getCraftingSlots(handler));

        int totalSize = handler.slots.size();
        int sideInventoryEnd = totalSize - 36;

        slots.addAll(handler.slots.subList(sideInventoryEnd, totalSize));
        // disabled due to dupe bug
//        slots.addAll(handler.slots.subList(10, sideInventoryEnd));

        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(CraftingStationContainerMenu handler) {
        return handler.slots.subList(0, 9);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }
}
