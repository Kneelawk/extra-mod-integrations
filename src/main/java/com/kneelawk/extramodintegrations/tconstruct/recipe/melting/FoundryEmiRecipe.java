package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;

public class FoundryEmiRecipe extends AbstractMeltingEmiRecipe {
    private final int time;
    private final int temperature;
    private final IMeltingContainer.OreRateType oreRateType;

    public FoundryEmiRecipe(MeltingRecipe recipe) {
        super(TiCCategories.FOUNDRY, recipe.getId());

        this.time = recipe.getTime();
        this.temperature = recipe.getTemperature();
        this.oreRateType = recipe.getOreType();
    }

    @Override
    protected int getTime() {
        return time;
    }

    @Override
    protected int getTemperature() {
        return temperature;
    }

    @Override
    protected IMeltingContainer.OreRateType getOreType() {
        return oreRateType;
    }
}
