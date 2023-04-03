package com.kneelawk.extramodintegrations.hephaestus.entity;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.awt.*;
import java.util.List;

public class EntityMeltingEmiRecipe implements EmiRecipe {
  public static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/melting.png");
  private static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 150, 41, 24, 17);
  public static final EmiTexture icon = new EmiTexture(BACKGROUND_LOC, 174, 41, 16, 16);

  private final EntityMeltingRecipe recipe;

  public EntityMeltingEmiRecipe(EntityMeltingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.ENTITY_MELTING_CATEGORY;
  }

  @Override
  public @Nullable ResourceLocation getId() {
    return recipe.getId();
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(EmiIngredient.of(recipe.getEntityInputs().stream().map(e -> new EntityEmiStack(e, 32)).toList()));
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(FluidEmiStack.of(recipe.getOutput().getFluid(), recipe.getOutput().getAmount()));
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

    widgets.addAnimatedTexture(arrow, 71, 21, 10000, true, false, false);

    // draw damage string next to the heart icon
    String damage = Float.toString(recipe.getDamage() / 2f);
    widgets.addText(new TextComponent(damage), 78, 8, Color.RED.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // inputs, filtered by spawn egg item
    widgets.addSlot(getInputs().get(0), 19, 11)
      .drawBack(false)
      .customBackground(null, 0, 0, 32, 32);
    // add spawn eggs as hidden inputs
    //widgets.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getItemInputs());

    // output
    widgets.add(new DynamicFluidSlotWidget(recipe.getOutput(), 115, 11, 16, 32, FluidValues.INGOT * 2))
      .recipeContext(this);

    // show fuels that are valid for this recipe
    EmiIngredient fuels = EmiIngredient.of(MeltingFuelHandler
      .getUsableFuels(1)
      .stream()
      .map(f -> FluidEmiStack.of(f.getFluid(), f.getAmount()))
      .toList());
    widgets.addSlot(fuels, 74, 42)
      .drawBack(false);

  }
}
