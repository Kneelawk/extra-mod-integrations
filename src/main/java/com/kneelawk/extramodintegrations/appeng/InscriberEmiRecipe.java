package com.kneelawk.extramodintegrations.appeng;

import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.util.Identifier;

public class InscriberEmiRecipe implements EmiRecipe {
    private final InscriberRecipe recipe;
    private final Identifier id;
    private final EmiIngredient middle;
    private final EmiIngredient top;
    private final EmiIngredient bottom;
    private final List<EmiIngredient> inputs;
    private final List<EmiIngredient> catalysts;
    private final EmiStack output;

    public InscriberEmiRecipe(InscriberRecipe recipe) {
        this.recipe = recipe;
        id = recipe.getId();
        middle = EmiIngredient.of(recipe.getMiddleInput());
        top = EmiIngredient.of(recipe.getTopOptional());
        bottom = EmiIngredient.of(recipe.getBottomOptional());
        output = EmiStack.of(recipe.getResultItem());


        inputs = Lists.newArrayList();
        catalysts = Lists.newArrayList();
        inputs.add(middle);
        List<EmiIngredient> extraInputs;
        if (recipe.getProcessType() == InscriberProcessType.PRESS) {
            extraInputs = inputs;
        } else {
            extraInputs = catalysts;
        }
        extraInputs.add(top);
        extraInputs.add(bottom);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return AE2Integration.INSCRIBER_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 97;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AE2Textures.INSCRIBER_BACKGROUND, 0, 0);

        widgets.addSlot(middle, 18, 23).drawBack(false);
        widgets.addSlot(top, 0, 0).drawBack(false)
            .catalyst(recipe.getProcessType() == InscriberProcessType.INSCRIBE && !top.isEmpty());
        widgets.addSlot(bottom, 0, 46).drawBack(false)
            .catalyst(recipe.getProcessType() == InscriberProcessType.INSCRIBE && !bottom.isEmpty());

        widgets.addSlot(output, 64, 20).drawBack(false).large(true).recipeContext(this);
    }
}
