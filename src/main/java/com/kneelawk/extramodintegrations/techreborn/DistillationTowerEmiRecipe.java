package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExMITextures;
import com.kneelawk.extramodintegrations.util.NinePatchWidget;
import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

public class DistillationTowerEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public DistillationTowerEmiRecipe(RebornRecipe recipe) {
        super(recipe);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.DISTILLATION_TOWER_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 4 + 18 * 3 + 2 * 2 + 4;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (50 - 18 * 2 - 2) / 2);
        widgets.addSlot(getInput(1), 16, (50 - 18 * 2 - 2) / 2 + 18 + 2);

        widgets.add(new NinePatchWidget(ExMITextures.SLOT_BG, 16 + 18 + 24, (50 - 26) / 2, 4 + 18 * 3 + 2 * 2 + 4, 26));
        widgets.addSlot(getOutput(0), 16 + 18 + 24 + 4, (50 - 18) / 2).drawBack(false).recipeContext(this);
        widgets.addSlot(getOutput(1), 16 + 18 + 24 + 4 + 18 + 2, (50 - 18) / 2).drawBack(false).recipeContext(this);
        widgets.addSlot(getOutput(2), 16 + 18 + 24 + 4 + 18 * 2 + 2 * 2, (50 - 18) / 2).drawBack(false).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 0);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (50 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16 + 18 + 2, 0);
    }
}
