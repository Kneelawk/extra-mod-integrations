package com.kneelawk.extramodintegrations.hephaestus.partbuilder;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.ResourceColorManager;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.partbuilder.IDisplayPartBuilderRecipe;
import slimeknights.tconstruct.plugin.jei.partbuilder.MaterialItemList;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class PartBuilderEmiRecipe implements EmiRecipe {
  private static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");

  private final ResourceLocation id;
  private final EmiIngredient materialVariant;
  private final EmiIngredient patternItems;
  private final EmiIngredient pattern;
  private final EmiStack output;
  private final int cost;
  private final MaterialVariantId variantId;

  public PartBuilderEmiRecipe(IDisplayPartBuilderRecipe recipe) {
    id = recipe.getId();
    variantId = recipe.getMaterial().getVariant();
    materialVariant = EmiIngredient.of(MaterialItemList.getItems(variantId).stream().map(EmiStack::of).toList());
    patternItems = EmiIngredient.of(recipe.getPatternItems().stream().map(EmiStack::of).toList());
    pattern = new PatternEmiStack(recipe.getPattern());
    output = EmiStack.of(recipe.getResultItem());
    cost = recipe.getCost();
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.PART_BUILDER_CATEGORY;
  }

  @Override
  public @Nullable ResourceLocation getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(materialVariant, patternItems);
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    return List.of(pattern);
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(output);
  }

  @Override
  public int getDisplayWidth() {
    return 121;
  }

  @Override
  public int getDisplayHeight() {
    return 46;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(new EmiTexture(BACKGROUND_LOC, 0, 117, 121, 46), 0, 0);

    // items
    widgets.addSlot(materialVariant, 24, 15).drawBack(false);
    widgets.addSlot(patternItems, 3, 15).drawBack(false);
    // patterns
    widgets.addSlot(pattern, 45, 15).drawBack(false);
    // TODO: material input?

    // output
    widgets.addSlot(output, 91, 10).drawBack(false).output(true).recipeContext(this);

    // texts
    Component name = MaterialTooltipCache.getColoredDisplayName(variantId);
    widgets.addText(name, 3, 2, Objects.requireNonNullElse(name.getStyle().getColor(), ResourceColorManager.WHITE).getValue(), true);
    Component cooling = new TranslatableComponent("jei.tconstruct.part_builder.cost", cost);
    widgets.addText(cooling, 3, 35, Color.GRAY.getRGB(), false);
  }
}
