package com.kneelawk.extramodintegrations.indrev;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import me.steven.indrev.recipes.machines.IRRecipe;

public class SimpleTwoInputEmiRecipe extends IREmiRecipe<IRRecipe> {
    private final EmiRecipeCategory category;

    protected SimpleTwoInputEmiRecipe(IRRecipe recipe, EmiRecipeCategory category) {
        super(recipe);
        this.category = category;
        checkInputCount(2);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public int getDisplayWidth() {
        return 6 + 18 * 2 + 36 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 32;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 3, (32 - 18) / 2);
        widgets.addSlot(getInput(1), 3 + 18, (32 - 18) / 2);
        widgets.addSlot(getOutput(0), 3 + 18 * 2 + 36, 3).output(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 3 + 18 * 2 + 6, (32 - 18) / 2);
    }
}
