package com.kneelawk.extramodintegrations.tconstruct.recipe.modifiers;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.worktable.IModifierWorktableRecipe;

public class ModifierWorktableEmiRecipe extends BasicEmiRecipe {
    public ModifierWorktableEmiRecipe(IModifierWorktableRecipe recipe) {
        super(TiCCategories.MODIFIER_WORKTABLE, recipe.getId(), 121, 35);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
