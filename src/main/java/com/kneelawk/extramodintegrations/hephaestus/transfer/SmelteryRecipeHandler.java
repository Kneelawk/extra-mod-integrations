package com.kneelawk.extramodintegrations.hephaestus.transfer;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;
import slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class SmelteryRecipeHandler implements StandardRecipeHandler<HeatingStructureContainerMenu> {
  @Override
  public List<Slot> getInputSources(HeatingStructureContainerMenu handler) {
    List<Slot> slots = new ArrayList<>(getCraftingSlots(handler));
    slots.addAll(handler.slots);
    return slots;
  }

  @Override
  public List<Slot> getCraftingSlots(HeatingStructureContainerMenu handler) {
    return handler.getSideInventory().slots;
  }

  @Override
  public boolean supportsRecipe(EmiRecipe recipe) {
    EmiRecipeCategory category = recipe.getCategory();
    return recipe.supportsRecipeTree()
      && (category == HephaestusIntegrationImpl.MELTING_CATEGORY);
  }
}
