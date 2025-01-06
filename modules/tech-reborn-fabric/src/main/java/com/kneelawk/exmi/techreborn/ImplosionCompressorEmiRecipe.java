package com.kneelawk.exmi.techreborn;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.core.api.util.UIUtils;

public class ImplosionCompressorEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public ImplosionCompressorEmiRecipe(RecipeHolder<RebornRecipe> recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.IMPLOSION_COMPRESSOR_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 18 * 2;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (50 - 18 * 2 - 2) / 2);
        widgets.addSlot(getInput(1), 16, (50 - 18 * 2 - 2) / 2 + 18 + 2);

        widgets.addSlot(getOutput(0), 16 + 18 + 24, (50 - 18) / 2).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 18 + 24 + 18, (50 - 18) / 2).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 64, 0, 0);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (50 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.time(), 16 + 18 + 2, 0);
    }
}
