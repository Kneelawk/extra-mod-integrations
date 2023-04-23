package com.kneelawk.extramodintegrations.hephaestus.entity;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.awt.*;
import java.util.List;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.util.Identifier;

public class EntityMeltingEmiRecipe implements EmiRecipe {
  public static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/melting.png");
  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 150, 41, 24, 17);
  private static final EmiTexture tank = new EmiTexture(BACKGROUND_LOC, 150, 74, 16, 16);
  public static final EmiTexture icon = new EmiTexture(BACKGROUND_LOC, 174, 41, 16, 16);

  private final Identifier id;
  private final EmiIngredient entity;
  private final EmiIngredient spawnEgg;
  private final EmiStack output;
  private final int damage;

  public EntityMeltingEmiRecipe(EntityMeltingRecipe recipe) {
    id = recipe.getId();
    entity = EmiIngredient.of(recipe.getEntityInputs().stream().map(e -> new EntityEmiStack(e, 32)).toList());
    spawnEgg = EmiIngredient.of(recipe.getItemInputs().stream().map(EmiStack::of).toList());
    output = FluidEmiStack.of(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
    damage = recipe.getDamage();
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.ENTITY_MELTING_CATEGORY;
  }

  @Override
  public @Nullable Identifier getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(entity);
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    // this is just so if u press u on a spawn egg the recipe shows up
    return List.of(spawnEgg);
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(output);
  }

  @Override
  public int getDisplayWidth() {
    return 150;
  }

  @Override
  public int getDisplayHeight() {
    return 62;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(BACKGROUND_LOC, 0, 0, 150, 62, 0, 41);

    // this recipe doesn't have a time, so imo it doesnt make sense to have an animated arrow
//    widgets.addAnimatedTexture(arrow, 71, 21, 10000, true, false, false);

    // draw damage string next to the heart icon
    String damageStr = Float.toString(damage / 2f);
    widgets.addText(Text.literal(damageStr), 78, 8, Color.RED.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // inputs, filtered by spawn egg item
    widgets.addSlot(entity, 19, 11)
      .drawBack(false)
      .customBackground(null, 0, 0, 32, 32);

    // output
    widgets.add(new DynamicFluidSlotWidget(output, 115, 11, 16, 32, FluidValues.INGOT * 2))
      .recipeContext(this);

    // show fuels that are valid for this recipe
    EmiIngredient fuels = EmiIngredient.of(MeltingFuelHandler
      .getUsableFuels(1)
      .stream()
      .map(f -> FluidEmiStack.of(f.getFluid(), f.getAmount()))
      .toList());
    widgets.add(new DynamicFluidSlotWidget(fuels, 75, 43, 16, 16, 1))
            .overlay(tank);

  }
}
