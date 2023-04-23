package com.kneelawk.extramodintegrations.hephaestus.transfer;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import com.kneelawk.extramodintegrations.hephaestus.partbuilder.PatternEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.tables.block.entity.table.PartBuilderBlockEntity;
import slimeknights.tconstruct.tables.menu.PartBuilderContainerMenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.screen.slot.Slot;

public class PartBuilderRecipeHandler implements StandardRecipeHandler<PartBuilderContainerMenu> {
  @Override
  public List<Slot> getInputSources(PartBuilderContainerMenu handler) {
    List<Slot> slots = new ArrayList<>(getCraftingSlots(handler));

    // skip over output slot
    slots.addAll(handler.slots.subList(3, handler.slots.size()));

    return slots;
  }

  @Override
  public List<Slot> getCraftingSlots(PartBuilderContainerMenu handler) {
    return List.of(handler.getInputSlot(), handler.getPatternSlot());
  }

  @Override
  public Slot getOutputSlot(PartBuilderContainerMenu handler) {
    return handler.getOutputSlot();
  }

  @Override
  public boolean supportsRecipe(EmiRecipe recipe) {
    return recipe.getCategory() == HephaestusIntegration.PART_BUILDER_CATEGORY && recipe.supportsRecipeTree();
  }

  @Override
  public boolean craft(EmiRecipe recipe, EmiCraftContext<PartBuilderContainerMenu> context) {
    boolean result = StandardRecipeHandler.super.craft(recipe, context);
    EmiIngredient catalyst = recipe.getCatalysts().get(0);
    PartBuilderContainerMenu menu = context.getScreenHandler();
    PartBuilderBlockEntity tile = context.getScreenHandler().getTile();
    if (catalyst instanceof PatternEmiStack patternStack) {
      Pattern pattern = patternStack.getPattern();
      int index = tile.getSortedButtons().indexOf(pattern);
      menu.setProperty(0, index);
    }
    return result;
  }
}
