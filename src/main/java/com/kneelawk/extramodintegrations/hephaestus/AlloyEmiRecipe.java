package com.kneelawk.extramodintegrations.hephaestus;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.awt.*;
import java.util.List;

public class AlloyEmiRecipe implements EmiRecipe {

  private static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/alloy.png");

  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 172, 0, 24, 17);
  private final AlloyRecipe recipe;

  public AlloyEmiRecipe(AlloyRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.ALLOY_CATEGORY;
  }

  @Override
  public @Nullable ResourceLocation getId() {
    return recipe.getId();
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return recipe.getDisplayInputs().stream()
      .map(l -> EmiIngredient.of(l.stream()
        .map(f -> FluidEmiStack.of(f.getFluid(), f.getAmount()))
        .toList()))
      .toList();
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(FluidEmiStack.of(recipe.getOutput().getFluid(), recipe.getOutput().getAmount()));
  }

  @Override
  public int getDisplayWidth() {
    return 172;
  }

  @Override
  public int getDisplayHeight() {
    return 62;
  }

  private long drawVariableFluids(WidgetHolder widgets, int x, int y, int totalWidth, int height, List<EmiIngredient> fluids, long minAmount) {
    int count = fluids.size();
    long maxAmount = minAmount;
    if (count > 0) {
      // first, find maximum used amount in the recipe so relations are correct
      for(EmiIngredient ingredient : fluids) {
        for(EmiStack input : ingredient.getEmiStacks()) {
          if (input.getAmount() > maxAmount) {
            maxAmount = input.getAmount();
          }
        }
      }
      // next, draw all fluids but the last
      int w = totalWidth / count;
      int max = count - 1;
      for (int i = 0; i < max; i++) {
        int fluidX = x + i * w;
        widgets.addSlot(fluids.get(i).copy().setAmount(maxAmount), fluidX, y)
          .drawBack(false)
          .customBackground(null, 0, 0, w, height);
      }
      // for the last, the width is the full remaining width
      int fluidX = x + max * w;
      widgets.addSlot(fluids.get(max).copy().setAmount(maxAmount), fluidX, y)
        .drawBack(false)
        .customBackground(null, 0, 0, totalWidth - (w * max), height);
    }
    return maxAmount;

  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(new EmiTexture(BACKGROUND_LOC, 0, 0, 172, 62), 0, 0);

    widgets.addAnimatedTexture(arrow, 90, 21, 10000, true, false, false);
    // temperature info
    Component temp = new TranslatableComponent("jei.tconstruct.temperature", recipe.getTemperature());
    widgets.addText(temp, 102, 5, Color.GRAY.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // inputs
    long maxAmount = drawVariableFluids(widgets, 19, 11, 48, 32, getInputs(), recipe.getOutput().getAmount());

    // output
    widgets.add(new DynamicFluidSlotWidget(recipe.getOutput(), 137, 11, 16, 32, maxAmount))
            .recipeContext(this);

    // fuel
    EmiIngredient fuel = EmiIngredient.of(MeltingFuelHandler.getUsableFuels(recipe.getTemperature())
            .stream()
            .map(f -> EmiStack.of(f.getFluid(), f.getAmount()))
            .toList());
    widgets.addSlot(fuel, 94, 43)
      .drawBack(false);
  }

}
