package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.WidgetUtils;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import reborncore.common.crafting.RebornRecipe;

import java.util.List;

import static com.kneelawk.extramodintegrations.ExMIMod.tooltip;

public class GrinderEmiRecipe extends TREmiRecipe {
    protected GrinderEmiRecipe(RebornRecipe recipe) {
        super(recipe);
        checkInputCount(1);
        checkOutputCount(1);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegrationImpl.GRINDER_CATEGORY;
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
        widgets.addSlot(getInput(0), 16, (50 - 18) / 2);
        widgets.addSlot(getOutput(0), 16 + 18 + 24, (50 - 26) / 2).output(true).recipeContext(this);

        widgets.addTexture(TRTextures.ENERGY_BAR_EMPTY, 0, 0).tooltip((mx, my) -> List.of(
            TooltipComponent.of(tooltip("techreborn.recipe_power", recipe.getPower()).asOrderedText())));
        widgets.addAnimatedTexture(TRTextures.ENERGY_BAR_FULL, 0, 0, 1000 / recipe.getPower() * 50, false, true, true);

        widgets.addTexture(TRTextures.ARROW_RIGHT_EMPTY, 16 + 18 + 4, (50 - 10) / 2);
        widgets.addAnimatedTexture(TRTextures.ARROW_RIGHT_FULL, 16 + 18 + 4, (50 - 10) / 2, recipe.getTime() * 50, true,
            false, false);
        widgets.addText(WidgetUtils.cookTime(recipe.getTime()), 16, 0, 0xFF3F3F3F, false);
    }
}
