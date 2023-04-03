package com.kneelawk.extramodintegrations.hephaestus.transfer;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;
import slimeknights.tconstruct.tables.menu.TinkerStationContainerMenu;

import java.util.ArrayList;
import java.util.List;

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
    // i hate this
    // FIXME: recipe filling works for tinker's anvil, but not tinker station
    List<Slot> inputSlots = new ArrayList<>(handler.getInputSlots().subList(2, handler.getInputSlots().size()));
    inputSlots.add(handler.getInputSlots().get(1));
    return inputSlots;
  }

  @Override
  public Slot getOutputSlot(TinkerStationContainerMenu handler) {
    return handler.slots.get(handler.slots.size() - 1);
  }

  @Override
  public boolean supportsRecipe(EmiRecipe recipe) {
    return recipe.getCategory() == HephaestusIntegrationImpl.MODIFIER_CATEGORY && recipe.supportsRecipeTree();
  }
}
