package com.kneelawk.extramodintegrations.hephaestus.casting;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipe;

import java.awt.*;
import java.util.List;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

public abstract class AbstractCastingEmiRecipe implements EmiRecipe {
  protected static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/casting.png");
  private static final EmiTexture castConsumed = new EmiTexture(BACKGROUND_LOC, 141, 32, 13, 11);
  private static final EmiTexture castKept = new EmiTexture(BACKGROUND_LOC, 141, 43, 13, 11);
  private static final EmiTexture tankOverlay = new EmiTexture(BACKGROUND_LOC, 133, 0, 32, 32);
  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 117, 32, 24, 17);

  private final EmiTexture block;
  private final Identifier id;
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
      // TODO: this is a bad id. get a better id from somewhere or make one up
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
  public @Nullable Identifier getId() {
    return id;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    // background
    widgets.addTexture(BACKGROUND_LOC, 0, 0, 117, 54, 0, 0);

    // casting table/basin block
    widgets.addTexture(block, 38, 35);

    // arrows etc
    List<TooltipComponent> cooling = List.of(TooltipComponent.of(EmiPort.ordered(EmiPort.translatable(
            "jei.tconstruct.time", coolingTime / 20f
    ))));
    widgets.addAnimatedTexture(arrow, 58, 18, coolingTime * 50, true, false, false)
            .tooltip((x, y) -> cooling);

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
