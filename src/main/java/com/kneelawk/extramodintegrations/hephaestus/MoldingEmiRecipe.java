package com.kneelawk.extramodintegrations.hephaestus;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;

import java.util.List;
import net.minecraft.util.Identifier;

public class MoldingEmiRecipe implements EmiRecipe {
  private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/casting.png");
  private static final EmiTexture table = new EmiTexture(BACKGROUND_LOC, 117, 0, 16, 16);
  private static final EmiTexture basin = new EmiTexture(BACKGROUND_LOC, 117, 16, 16, 16);
  private static final EmiTexture downArrow = new EmiTexture(BACKGROUND_LOC, 70, 55, 6, 6);
  private static final EmiTexture upArrow = new EmiTexture(BACKGROUND_LOC, 76, 55, 6, 6);

  private final Identifier id;
  private final EmiIngredient material;
  private final EmiStack output;
  private final EmiIngredient pattern;
  private final boolean isPatternConsumed;
  private final EmiTexture block;
  public MoldingEmiRecipe(MoldingRecipe recipe) {
    id = recipe.getId();
    isPatternConsumed = recipe.isPatternConsumed();
    pattern = EmiIngredient.of(recipe.getPattern());
    material = EmiIngredient.of(recipe.getMaterial());
    output = EmiStack.of(recipe.getOutput(null));
    block = recipe.getType() == TinkerRecipeTypes.MOLDING_BASIN.get() ? basin : table;
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.MOLDING_CATEGORY;
  }

  @Override
  public @Nullable Identifier getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(material, isPatternConsumed ? pattern : EmiStack.EMPTY);
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    return List.of(!isPatternConsumed ? pattern : EmiStack.EMPTY);
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(output);
  }

  @Override
  public int getDisplayWidth() {
    return 70;
  }

  @Override
  public int getDisplayHeight() {
    return 57;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(new EmiTexture(BACKGROUND_LOC, 0, 55, 70, 57), 0, 0);

    // draw the main block
    widgets.addTexture(block, 3, 40);

    // if no mold, we "pickup" the item, so draw no table
    if (!pattern.isEmpty()) {
      widgets.addTexture(block, 51, 40);
      widgets.addTexture(downArrow, 8, 17);
    } else {
      widgets.addTexture(upArrow, 8, 17);
    }

    // basic input output
    widgets.addSlot(material, 2, 23).drawBack(false);
    widgets.addSlot(output, 50, 23).drawBack(false).recipeContext(this);

    if (!pattern.isEmpty())
      widgets.addSlot(pattern, 2, 0).drawBack(false).catalyst(!isPatternConsumed);
  }
}
