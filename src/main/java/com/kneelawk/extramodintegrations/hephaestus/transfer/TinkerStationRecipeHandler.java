package com.kneelawk.extramodintegrations.hephaestus.transfer;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import slimeknights.tconstruct.tables.menu.TinkerStationContainerMenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.screen.slot.Slot;

public class TinkerStationRecipeHandler implements StandardRecipeHandler<TinkerStationContainerMenu> {
  @Override
  public List<Slot> getInputSources(TinkerStationContainerMenu handler) {
    // crafting slots first
    List<Slot> slots = new ArrayList<>(getCraftingSlots(handler));

    // add inventory, armour and offhand slots, skipping over output slot
    int start = handler.getInputSlots().size() + 1;
    slots.addAll(handler.slots.subList(start, handler.slots.size()));

    return slots;
  }

  @Override
  public List<Slot> getCraftingSlots(TinkerStationContainerMenu handler) {
    return handler.getInputSlots();
  }

  @Override
  public Slot getOutputSlot(TinkerStationContainerMenu handler) {
    return handler.slots.get(handler.slots.size() - 1);
  }

  @Override
  public boolean supportsRecipe(EmiRecipe recipe) {
    return recipe.getCategory() == HephaestusIntegration.MODIFIER_CATEGORY && recipe.supportsRecipeTree();
  }
}
