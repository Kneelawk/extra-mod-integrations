package com.kneelawk.extramodintegrations.hephaestus.casting;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
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
  private static final EmiTexture tankOverlay = new EmiTexture(BACKGROUND_LOC, 133, 0, 32, 32);
  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 117, 32, 24, 17);

  private final EmiTexture block;
  private final ResourceLocation id;
  private final EmiIngredient fluidInput;
  private final EmiStack output;
  private final EmiIngredient castItem;
  private final int coolingTime;
  private final boolean isConsumed;
  private final boolean hasCast;

  public AbstractCastingEmiRecipe(IDisplayableCastingRecipe recipe, EmiTexture block) {
    this.block = block;

    // TODO this doesn't work, so container filling recipes don't have ids :(
    if (recipe instanceof ContainerFillingRecipe r)
      id = r.getId();
    else if (recipe instanceof slimeknights.tconstruct.library.recipe.casting.AbstractCastingRecipe r)
      id = r.getId();
    else id = null;

    hasCast = recipe.hasCast();
    isConsumed = recipe.isConsumed();
    castItem = hasCast ? EmiIngredient.of(recipe.getCastItems().stream().map(EmiStack::of).toList()) : EmiStack.EMPTY;
    fluidInput = EmiIngredient.of(recipe.getFluids().stream().map(f -> FluidEmiStack.of(f.getFluid(), f.getAmount())).toList());
    output = EmiStack.of(recipe.getOutput());
    coolingTime = recipe.getCoolingTime();
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(fluidInput, isConsumed ? castItem : EmiStack.EMPTY);
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    return List.of(isConsumed ? EmiStack.EMPTY : castItem);
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(output);
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
    return id;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    // background
    widgets.addTexture(BACKGROUND_LOC, 0, 0, 117, 54, 0, 0);

    // casting table/basin block
    widgets.addTexture(block, 38, 35);

    // arrows etc
    widgets.addAnimatedTexture(arrow, 58, 18, coolingTime * 50, true, false, false);
    if (hasCast) {
      widgets.addTexture(isConsumed ? castConsumed : castKept, 63, 39)
        .tooltip((x, y) ->
          List.of(ClientTooltipComponent.create(new TranslatableComponent(
            isConsumed
              ? "jei.tconstruct.casting.cast_consumed"
              : "jei.tconstruct.casting.cast_kept")
            .getVisualOrderText())));
    }
    Component cooling = new TranslatableComponent("jei.tconstruct.time", coolingTime / 20);
    widgets.addText(cooling, 72, 2, Color.GRAY.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // items
    if (!castItem.isEmpty()) {
      widgets.addSlot(castItem, 37, 18).drawBack(false).catalyst(!isConsumed);
    }
    widgets.addSlot(output, 88, 13).drawBack(false).output(true).recipeContext(this);

    // fluids
    // tank fluids
    widgets.add(new DynamicFluidSlotWidget(fluidInput, 3, 3, 32, 32, FluidValues.METAL_BLOCK))
            .overlay(tankOverlay)
            .drawBack(false);
    // pouring fluid
    int h = 11;
    if (!hasCast) {
      h += 16;
    }
    widgets.add(new DynamicFluidSlotWidget(fluidInput.copy().setAmount(1), 43, 8, 6, h, 1)).drawBack(false);
  }
}
