package com.kneelawk.extramodintegrations.hephaestus.casting;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

public class CastingTableEmiRecipe extends AbstractCastingEmiRecipe {

  public CastingTableEmiRecipe(IDisplayableCastingRecipe recipe) {
    super(recipe, new EmiTexture(BACKGROUND_LOC, 117, 0, 16, 16));
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.CASTING_TABLE_CATEGORY;
  }
}
