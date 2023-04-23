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
import net.minecraft.client.gui.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

public class AlloyEmiRecipe implements EmiRecipe {

  private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/alloy.png");

  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 172, 0, 24, 17);
  private static final EmiTexture tank = new EmiTexture(BACKGROUND_LOC, 172, 17, 16, 16);
  private final Identifier id;
  private final List<EmiIngredient> inputs;
  private final EmiStack output;
  private final int temperature;

  public AlloyEmiRecipe(AlloyRecipe recipe) {
    id = recipe.getId();
    inputs = recipe.getDisplayInputs().stream()
            .map(l -> EmiIngredient.of(l.stream()
                    .map(f -> FluidEmiStack.of(f.getFluid(), f.getAmount()))
                    .toList()))
            .toList();
    output = FluidEmiStack.of(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
    temperature = recipe.getTemperature();
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.ALLOY_CATEGORY;
  }

  @Override
  public @Nullable Identifier getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return inputs;
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(output);
  }

  @Override
  public int getDisplayWidth() {
    return 172;
  }

  @Override
  public int getDisplayHeight() {
    return 62;
  }

  /**
   * @return the fluid slots
   */
  public static List<DynamicFluidSlotWidget> drawVariableFluids(WidgetHolder widgets, int x, int y, int totalWidth, int height, List<? extends EmiIngredient> fluids, long minAmount) {
    int count = fluids.size();
    if (fluids.isEmpty()) throw new IllegalArgumentException("drawVariableFluids with zero fluids? what?");

    // first, find maximum used amount in the recipe so relations are correct
    long maxAmount = Long.max(minAmount, fluids.stream().mapToLong(EmiIngredient::getAmount).max().orElse(0));

    List<DynamicFluidSlotWidget> slots = new LinkedList<>();
    int w = totalWidth / count;
    int max = count - 1;
    // next, draw all fluids but the first
    for (int i = 1; i < fluids.size(); i++) {
      int fluidX = x + i * w;
      slots.add(widgets.add(new DynamicFluidSlotWidget(fluids.get(i), fluidX, y, w, height, maxAmount)));
    }
    // for the first, the width is the full remaining width
    slots.add(0, widgets.add(new DynamicFluidSlotWidget(fluids.get(0), x, y, totalWidth - (w * max), height, maxAmount)));
    return slots;

  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(new EmiTexture(BACKGROUND_LOC, 0, 0, 172, 62), 0, 0);

    // this recipe doesn't have a time, so imo it doesnt make sense to have an animated arrow
//    widgets.addAnimatedTexture(arrow, 90, 21, 10000, true, false, false);

    // temperature info
    Text temp = Text.translatable("jei.tconstruct.temperature", temperature);
    widgets.addText(temp, 102, 5, Color.GRAY.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // inputs
    drawVariableFluids(widgets, 19, 11, 48, 32, inputs, output.getAmount());

    // output
    long maxAmount = Long.max(output.getAmount(), inputs.stream().mapToLong(EmiIngredient::getAmount).max().orElse(0));
    widgets.add(new DynamicFluidSlotWidget(output, 137, 11, 16, 32, maxAmount))
            .recipeContext(this);

    // fuel
    EmiIngredient fuel = EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature)
            .stream()
            .map(f -> EmiStack.of(f.getFluid(), f.getAmount()))
            .toList());
    widgets.add(new DynamicFluidSlotWidget(fuel, 94, 43, 16, 16, 1))
            .overlay(tank);
  }

}
