package com.kneelawk.extramodintegrations.hephaestus;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;

import java.util.List;

public class MoldingEmiRecipe implements EmiRecipe {
  private static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/casting.png");
  private static final EmiTexture table = new EmiTexture(BACKGROUND_LOC, 117, 0, 16, 16);
  private static final EmiTexture basin = new EmiTexture(BACKGROUND_LOC, 117, 16, 16, 16);
  private static final EmiTexture downArrow = new EmiTexture(BACKGROUND_LOC, 70, 55, 6, 6);
  private static final EmiTexture upArrow = new EmiTexture(BACKGROUND_LOC, 76, 55, 6, 6);

  private final MoldingRecipe recipe;

  public MoldingEmiRecipe(MoldingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.MOLDING_CATEGORY;
  }

  @Override
  public @Nullable ResourceLocation getId() {
    return recipe.getId();
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(
      EmiIngredient.of(recipe.getMaterial()),
      recipe.isPatternConsumed() ? EmiIngredient.of(recipe.getPattern()) : EmiStack.EMPTY
    );
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    return List.of(!recipe.isPatternConsumed() ? EmiIngredient.of(recipe.getPattern()) : EmiStack.EMPTY);
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(EmiStack.of(recipe.getResultItem()));
  }

  private EmiIngredient getPattern() {
    return recipe.isPatternConsumed() ? getInputs().get(1) : getCatalysts().get(0);
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
    EmiTexture block = recipe.getType() == TinkerRecipeTypes.MOLDING_BASIN.get() ? basin : table;
    widgets.addTexture(block, 3, 40);

    // if no mold, we "pickup" the item, so draw no table
    if (!getPattern().isEmpty()) {
      widgets.addTexture(block, 51, 40);
      widgets.addTexture(downArrow, 8, 17);
    } else {
      widgets.addTexture(upArrow, 8, 17);
    }

    // basic input output
    widgets.addSlot(getInputs().get(0), 2, 23).drawBack(false);
    widgets.addSlot(getOutputs().get(0), 50, 23).drawBack(false).recipeContext(this);

    // if we have a mold, we are pressing into the table, so draw pressed item on input and output
    EmiIngredient pattern = getPattern();
    if (!pattern.isEmpty())
      widgets.addSlot(pattern, 2, 0).drawBack(false).catalyst(!recipe.isPatternConsumed());
  }
}
