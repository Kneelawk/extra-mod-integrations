package com.kneelawk.extramodintegrations.hephaestus.casting;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipe;

import java.awt.*;
import java.util.List;

public abstract class AbstractCastingEmiRecipe implements EmiRecipe {
  protected static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/casting.png");
  private static final EmiTexture castConsumed = new EmiTexture(BACKGROUND_LOC, 141, 32, 13, 11);
  private static final EmiTexture castKept = new EmiTexture(BACKGROUND_LOC, 141, 43, 13, 11);
  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 117, 32, 24, 17);

  protected IDisplayableCastingRecipe recipe;
  private final EmiTexture block;

  public AbstractCastingEmiRecipe(IDisplayableCastingRecipe recipe, EmiTexture block) {
    this.recipe = recipe;
    this.block = block;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(
      // fluid input
      EmiIngredient.of(recipe.getFluids().stream().map(f -> FluidEmiStack.of(f.getFluid(), f.getAmount())).toList()),
      // cast items
      recipe.isConsumed()
        ? EmiIngredient.of(recipe.getCastItems().stream().map(EmiStack::of).toList())
        : EmiStack.EMPTY);
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    return List.of(!recipe.isConsumed()
      ? EmiIngredient.of(recipe.getCastItems().stream().map(EmiStack::of).toList())
      : EmiStack.EMPTY);
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(EmiStack.of(recipe.getOutput()));
  }

  private EmiIngredient getCastItem() {
    return recipe.isConsumed()
      ? getInputs().get(1)
      : getCatalysts().get(0);
  }

  @Override
  public int getDisplayWidth() {
    return 117;
  }

  @Override
  public int getDisplayHeight() {
    return 54;
  }

  @Override
  public @Nullable ResourceLocation getId() {
    if (recipe instanceof ContainerFillingRecipe r)
      return r.getId();
    else if (recipe instanceof slimeknights.tconstruct.library.recipe.casting.AbstractCastingRecipe r)
      return r.getId();
    else return null;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    // background
    widgets.addTexture(BACKGROUND_LOC, 0, 0, 117, 54, 0, 0);

    // casting table/basin block
    widgets.addTexture(block, 38, 35);

    // arrows etc
    widgets.addAnimatedTexture(arrow, 58, 18, recipe.getCoolingTime() * 50, true, false, false);
    if (recipe.hasCast()) {
      widgets.addTexture(recipe.isConsumed() ? castConsumed : castKept, 63, 39)
        .tooltip((x, y) ->
          List.of(ClientTooltipComponent.create(new TranslatableComponent(
            recipe.isConsumed()
              ? "jei.tconstruct.casting.cast_consumed"
              : "jei.tconstruct.casting.cast_kept")
            .getVisualOrderText())));
    }
    int coolingTime = recipe.getCoolingTime() / 20;
    Component cooling = new TranslatableComponent("jei.tconstruct.time", coolingTime);
    widgets.addText(cooling, 72, 2, Color.GRAY.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // items
    if (!getCastItem().isEmpty()) {
      widgets.addSlot(getCastItem(), 37, 18).drawBack(false).catalyst(!recipe.isConsumed());
    }
    widgets.addSlot(getOutputs().get(0), 88, 13).drawBack(false).output(true).recipeContext(this);

    // fluids
    // tank fluids
    long capacity = FluidValues.METAL_BLOCK;
    widgets.addSlot(getInputs().get(0), 3, 3).drawBack(false)
      .customBackground(null, 0, 0, 32, 32);
    // pouring fluid
    int h = 11;
    if (!recipe.hasCast()) {
      h += 16;
    }
    widgets.addSlot(getInputs().get(0), 43, 8).drawBack(false)
      .customBackground(null, 0, 0, 6, h);
  }
}
