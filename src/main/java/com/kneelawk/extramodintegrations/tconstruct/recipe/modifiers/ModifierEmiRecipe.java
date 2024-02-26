package com.kneelawk.extramodintegrations.tconstruct.recipe.modifiers;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;

public class ModifierEmiRecipe extends BasicEmiRecipe {
    public ModifierEmiRecipe(ITinkerStationRecipe recipe) {
        super(TiCCategories.MODIFIERS, recipe.getId(), 128, 77);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
