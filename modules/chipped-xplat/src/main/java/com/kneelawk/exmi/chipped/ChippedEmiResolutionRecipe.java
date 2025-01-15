package com.kneelawk.exmi.chipped;

import java.util.List;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import com.kneelawk.exmi.core.api.ExMITextures;

public class ChippedEmiResolutionRecipe extends BasicEmiRecipe {
    private final EmiIngredient input;
    private final EmiStack output;

    public ChippedEmiResolutionRecipe(EmiIngredient input, EmiStack output) {
        super(CIntegration.WORKBENCH, null, 18 + 24 + 26, 26);
        this.input = input;
        this.output = output;

        this.inputs = List.of(this.input);
        this.outputs = List.of(this.output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 0, 4);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 4, 5);
        widgets.addSlot(output, 18 + 24, 0).large(true).recipeContext(this);
    }
}
