package com.kneelawk.extramodintegrations.hephaestus.melting;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;
import slimeknights.tconstruct.smeltery.block.entity.module.FuelModule;

import java.util.List;

public class MeltingEmiRecipe extends AbstractMeltingEmiRecipe {
  private static final EmiTexture solidFuel = new EmiTexture(BACKGROUND_LOC, 164, 0, 18, 20);

  private final EmiStack output;

  public MeltingEmiRecipe(MeltingRecipe recipe) {
    super(recipe);
    output = EmiStack.of(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.MELTING_CATEGORY;
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(output);
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    super.addWidgets(widgets);

    // solid fuel slot
    if (temperature <= FuelModule.SOLID_TEMPERATURE) {
      widgets.addTexture(solidFuel, 1, 19);
    }

    // output
//    IMeltingContainer.OreRateType oreType = recipe.getOreType();
//    IRecipeSlotTooltipCallback tooltip;
//    if (oreType == IMeltingContainer.OreRateType.METAL) {
//      tooltip = METAL_ORE_TOOLTIP;
//    } else if (oreType == IMeltingContainer.OreRateType.GEM) {
//      tooltip = GEM_ORE_TOOLTIP;
//    } else {
//      tooltip = MeltingCategory.MeltingFluidCallback.INSTANCE;
//    }
    widgets.add(new DynamicFluidSlotWidget(output, 96, 4, 32, 32, FluidValues.METAL_BLOCK))
            .overlay(tankOverlay)
            .recipeContext(this);

    // show fuels that are valid for this recipe
    int fuelHeight = 32;
    // solid fuel
    if (temperature <= FuelModule.SOLID_TEMPERATURE) {
      fuelHeight = 15;
      EmiIngredient solidFuels = EmiIngredient.of(MeltingFuelHandler.SOLID_FUELS.get()
          .stream().map(EmiStack::of).toList());
      widgets.addSlot(solidFuels, 1, 21).drawBack(false);
    }

    // liquid fuel
    EmiIngredient liquidFuels = EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature)
      .stream().map(s -> FluidEmiStack.of(s.getFluid(), s.getAmount())).toList());
    widgets.add(new DynamicFluidSlotWidget(liquidFuels, 4, 4, 12, fuelHeight, 1));
  }
}
