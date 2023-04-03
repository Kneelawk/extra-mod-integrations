package com.kneelawk.extramodintegrations.hephaestus.transfer;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;
import slimeknights.tconstruct.tables.menu.CraftingStationContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class CraftingStationRecipeHandler implements StandardRecipeHandler<CraftingStationContainerMenu> {
  @Override
  public List<Slot> getInputSources(CraftingStationContainerMenu handler) {
    // crafting table slots first
    List<Slot> slots = new ArrayList<>(getCraftingSlots((handler)));
    // player inventory slots
    int totalSize = handler.slots.size();
    int sideInventoryEnd = totalSize - 36;
    slots.addAll(handler.slots.subList(sideInventoryEnd, totalSize));
    // side inventory
    slots.addAll(handler.slots.subList(10, sideInventoryEnd));
    return slots;
  }

  @Override
  public List<Slot> getCraftingSlots(CraftingStationContainerMenu handler) {
    return handler.slots.subList(0, 9);
  }

  @Override
  public Slot getOutputSlot(CraftingStationContainerMenu handler) {
    // FIXME: craft to inventory and craft to cursor take from the top left slot of the crafting grid instead of the result slot
    return handler.getResultSlot();
  }

  @Override
  public boolean supportsRecipe(EmiRecipe recipe) {
    return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
  }
}
