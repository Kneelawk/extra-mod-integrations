package com.kneelawk.extramodintegrations.hephaestus.partbuilder;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

public class PartBuilderEmiRecipe implements EmiRecipe {
  private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");

  private final Identifier id;
  private final EmiIngredient materialVariant;
  private final EmiIngredient patternItems;
  private final EmiIngredient pattern;
  private final EmiStack output;
  private final int cost;
  private final MaterialVariantId variantId;

  public PartBuilderEmiRecipe(IDisplayPartBuilderRecipe recipe) {
    // TODO: this is a bad id. get a better id from somewhere or make one up
//    id = recipe.getId();
    id = new Identifier(ExMIMod.MOD_ID,
            "tconstruct/part_builder/"
                    + recipe.getMaterial().getId().toString().replace(":", "/")
                    + "/"
                    + recipe.getPattern().toString().replace(":", "/")
    );
    variantId = recipe.getMaterial().getVariant();
    materialVariant = EmiIngredient.of(MaterialItemList.getItems(variantId).stream().map(EmiStack::of).toList());
    patternItems = EmiIngredient.of(recipe.getPatternItems().stream().map(EmiStack::of).toList());
    pattern = new PatternEmiStack(recipe.getPattern());
    output = EmiStack.of(recipe.getOutput(null));
    cost = recipe.getCost();
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.PART_BUILDER_CATEGORY;
  }

  @Override
  public @Nullable Identifier getId() {
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
    Text name = MaterialTooltipCache.getColoredDisplayName(variantId);
    widgets.addText(name, 3, 2, Objects.requireNonNullElse(name.getStyle().getColor(), ResourceColorManager.WHITE).getRgb(), true);
    Text cooling = Text.translatable("jei.tconstruct.part_builder.cost", cost);
    widgets.addText(cooling, 3, 35, Color.GRAY.getRGB(), false);
  }
}
