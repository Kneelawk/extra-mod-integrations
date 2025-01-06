package com.kneelawk.exmi.techreborn;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.core.api.util.UIUtils;

public class AssemblingMachineEmiRecipe extends TREmiRecipe<RebornRecipe> {
    public AssemblingMachineEmiRecipe(RecipeHolder<? extends RebornRecipe> recipe) {
        super(recipe);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.ASSEMBLING_MACHINE_CATEGORY;
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 + 24 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 16, (50 - 18 * 2 - 2) / 2);
        widgets.addSlot(getInput(1), 16, (50 - 18 * 2 - 2) / 2 + 18 + 2);
        widgets.addSlot(getOutput(0), 16 + 18 + 24, (50 - 26) / 2).large(true).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 0);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 + 4, (50 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.time(), 16 + 18 + 2, 0);
    }
}
