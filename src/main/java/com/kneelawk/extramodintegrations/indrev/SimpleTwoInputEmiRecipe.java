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
        return 8 + 18 + 36 + 36;
    }

    @Override
    public int getDisplayHeight() {
        return 34;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInput(0), 4, 4 + (26 - 18) / 2);
        widgets.addSlot(getInput(1), 4 + 18, 4 + (26 - 18) / 2);
        widgets.addSlot(getOutput(0), 4 + 18 + 46, 4).output(true).recipeContext(this);

        UIUtils.cookArrow(widgets, recipe.getTicks(), 4 + 18 + 20, 4 + (26 - 18) / 2);
    }
}
