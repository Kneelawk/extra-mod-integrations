package com.kneelawk.extramodintegrations.appeng;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.ChargerRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;

public class ChargerEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;

    public ChargerEmiRecipe(ChargerRecipe recipe) {
        id = recipe.getId();
        input = EmiIngredient.of(recipe.getIngredient());
        output = EmiStack.of(recipe.getResultItem());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return AE2Integration.CHARGER_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 48 + 18 + 36 + 18;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 24, 0);
        widgets.addSlot(output, 24 + 18 + 36, 0).recipeContext(this);

        widgets.addTexture(ExMITextures.RIGHT_ARROW, 24 + 18 + 10, 1);
        widgets.addText(ExMIMod.gui("ae2.charger"), getDisplayWidth(), getDisplayHeight(), 0xFF3F3F3F, false)
            .horizontalAlign(TextWidget.Alignment.END).verticalAlign(TextWidget.Alignment.END);
        widgets.addSlot(AE2Integration.CRANK_STACK, 0, 40 - 18).drawBack(false);
    }
}
