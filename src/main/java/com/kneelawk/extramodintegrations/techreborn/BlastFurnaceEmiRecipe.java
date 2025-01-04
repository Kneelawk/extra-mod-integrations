package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExMITextures;
import com.kneelawk.extramodintegrations.util.NinePatchWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class BlastFurnaceEmiRecipe extends TREmiRecipe<BlastFurnaceRecipe> {
    public BlastFurnaceEmiRecipe(BlastFurnaceRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.BLAST_FURNACE_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 4 + 18 + 2 + 18 + 4;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (50 - 18 * 2 - 2) / 2);
        widgets.addSlot(getInput(1), 16, (50 - 18 * 2 - 2) / 2 + 18 + 2);

        widgets.add(new NinePatchWidget(ExMITextures.SLOT_BG, 16 + 18 + 24, (50 - 26) / 2, 4 + 18 + 2 + 18 + 4, 26));
        widgets.addSlot(getOutput(0), 16 + 18 + 24 + 4, (50 - 18) / 2).drawBack(false).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 18 + 24 + 4 + 18 + 2, (50 - 18) / 2).drawBack(false).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 40, 0, 0);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (50 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16 + 18 + 2, 0);
        widgets.addText(gui("techreborn.heat", recipe.getHeat()).getVisualOrderText(), 16 + 18 + 2, 50 - 9, 0xFF3F3F3F,
            false);
    }
}
