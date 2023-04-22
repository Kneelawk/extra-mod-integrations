package com.kneelawk.extramodintegrations.hephaestus.melting;

import com.kneelawk.extramodintegrations.hephaestus.AlloyEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.util.List;

public class FoundryEmiRecipe extends AbstractMeltingEmiRecipe {
  private final List<EmiStack> outputs;
  public FoundryEmiRecipe(MeltingRecipe recipe) {
    super(recipe);
    outputs = recipe.getOutputWithByproducts()
            .stream()
            .map(e -> e.get(0))
            .map(s -> FluidEmiStack.of(s.getFluid(), s.getAmount()))
            .toList();
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.FOUNDRY_CATEGORY;
  }

  @Override
  public List<EmiStack> getOutputs() {
    return outputs;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    super.addWidgets(widgets);

    // output fluid(s)
    AlloyEmiRecipe.drawVariableFluids(widgets, 96, 4, 32, 32, outputs, FluidValues.METAL_BLOCK)
            .forEach(s -> s.recipeContext(this));

    // fuel
    EmiIngredient fuels = EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature)
            .stream().map(s -> FluidEmiStack.of(s.getFluid(), s.getAmount()))
            .toList());
    widgets.add(new DynamicFluidSlotWidget(fuels, 4, 4, 12, 32, 1));
  }
}
