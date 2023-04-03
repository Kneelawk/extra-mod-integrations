package com.kneelawk.extramodintegrations.hephaestus.melting;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
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

public class MeltingEmiRecipe extends AbstractMeltingEmiRecipe {
private EmiTexture solidFuel = new EmiTexture(BACKGROUND_LOC, 164, 0, 18, 20);

  public MeltingEmiRecipe(MeltingRecipe recipe) {
    super(recipe);
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.MELTING_CATEGORY;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    super.addWidgets(widgets);

    // solid fuel slot
    int temperature = recipe.getTemperature();
    if (temperature <= FuelModule.SOLID_TEMPERATURE) {
      widgets.addTexture(solidFuel, 1, 19);
    }

    // input
    widgets.addSlot(getInputs().get(0), 23, 17).drawBack(false);

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
    widgets.add(new DynamicFluidSlotWidget(recipe.getOutput(), 96, 4, 32, 32, FluidValues.METAL_BLOCK))
            .overlay(tankOverlay)
            .recipeContext(this);

    // show fuels that are valid for this recipe
    int fuelHeight = 32;
    // solid fuel
    if (recipe.getTemperature() <= FuelModule.SOLID_TEMPERATURE) {
      fuelHeight = 15;
      EmiIngredient solidFuels = EmiIngredient.of(MeltingFuelHandler.SOLID_FUELS.get()
          .stream().map(EmiStack::of).toList());
      widgets.addSlot(solidFuels, 1, 21).drawBack(false);
    }

    // liquid fuel
    EmiIngredient liquidFuels = EmiIngredient.of(MeltingFuelHandler.getUsableFuels(recipe.getTemperature())
      .stream().map(s -> FluidEmiStack.of(s.getFluid(), s.getAmount())).toList());
    widgets.addSlot(liquidFuels, 4, 4).customBackground(null, 0, 0, 12, fuelHeight).drawBack(false);
  }
}
