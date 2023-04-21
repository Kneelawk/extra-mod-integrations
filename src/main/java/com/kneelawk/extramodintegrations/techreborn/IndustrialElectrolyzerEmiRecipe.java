package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExMITextures;
import com.kneelawk.extramodintegrations.util.NinePatchWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

public class IndustrialElectrolyzerEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public IndustrialElectrolyzerEmiRecipe(RebornRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.INDUSTRIAL_ELECTROLYZER_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 * 5 + 2 * 2;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, 1 + 26 + 4);
        widgets.addSlot(getInput(1), 16 + 18 + 2, 1 + 26 + 4);

        int outputWidth = 18 * 4 + 2 * 3 + 4 * 2;
        int outputX = 16 + (18 * 5 + 2 * 2 - outputWidth) / 2;
        widgets.add(new NinePatchWidget(ExMITextures.SLOT_BG, outputX, 1, 18 * 4 + 2 * 3 + 4 * 2, 26));
        widgets.addSlot(getOutput(0), outputX + 4, 1 + 4).drawBack(false).recipeContext(this);
        widgets.addSlot(getOutput(1), outputX + 4 + 18 + 2, 1 + 4).drawBack(false).recipeContext(this);
        widgets.addSlot(getOutput(2), outputX + 4 + 18 * 2 + 2 * 2, 1 + 4).drawBack(false).recipeContext(this);
        widgets.addSlot(getOutput(3), outputX + 4 + 18 * 3 + 2 * 3, 1 + 4).drawBack(false).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 0);
        TRUIUtils.arrowUp(widgets, recipe, 16 + 18 * 2 + 2 + 4, 1 + 26 + 4 + 1);
        UIUtils.cookTime(widgets, recipe.getTime(), 16 + 18 * 3 + 2, 50 - 9);
    }
}
