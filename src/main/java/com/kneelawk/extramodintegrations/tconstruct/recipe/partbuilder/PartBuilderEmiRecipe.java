package com.kneelawk.extramodintegrations.tconstruct.recipe.partbuilder;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.partbuilder.IPartBuilderRecipe;

public class PartBuilderEmiRecipe extends BasicEmiRecipe {
    public PartBuilderEmiRecipe(IPartBuilderRecipe recipe) {
        super(TiCCategories.PART_BUILDER, recipe.getId(), 121, 46);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
