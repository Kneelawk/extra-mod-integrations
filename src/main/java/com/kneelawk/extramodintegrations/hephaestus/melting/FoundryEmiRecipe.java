package com.kneelawk.extramodintegrations.hephaestus.melting;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegrationImpl;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;

public class FoundryEmiRecipe extends AbstractMeltingEmiRecipe {
  public FoundryEmiRecipe(MeltingRecipe recipe) {
    super(recipe);
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegrationImpl.FOUNDRY_CATEGORY;
  }
}
