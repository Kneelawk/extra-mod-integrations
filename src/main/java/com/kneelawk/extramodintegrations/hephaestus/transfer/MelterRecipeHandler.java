package com.kneelawk.extramodintegrations.hephaestus.transfer;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import slimeknights.tconstruct.smeltery.menu.MelterContainerMenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.screen.slot.Slot;

public class MelterRecipeHandler implements StandardRecipeHandler<MelterContainerMenu> {
  @Override
  public List<Slot> getInputSources(MelterContainerMenu handler) {
    List<Slot> slots = new ArrayList<>(getCraftingSlots(handler));
    // don't include fuel slot
    slots.addAll(handler.slots.subList(handler.getInputs().length + 1, handler.slots.size()));
    return slots;
  }

  @Override
  public List<Slot> getCraftingSlots(MelterContainerMenu handler) {
    return List.of(handler.getInputs());
  }

  @Override
  public boolean supportsRecipe(EmiRecipe recipe) {
    return recipe.getCategory() == HephaestusIntegration.MELTING_CATEGORY && recipe.supportsRecipeTree();
  }
}
