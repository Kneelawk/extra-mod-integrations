package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.UIUtils;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import techreborn.api.recipe.recipes.RollingMachineRecipe;

import java.util.List;

public class RollingMachineEmiRecipe implements EmiRecipe {
    private final RollingMachineRecipe recipe;
    private final Identifier id;
    private final List<EmiIngredient> input;
    private final EmiStack output;

    public RollingMachineEmiRecipe(RollingMachineRecipe recipe) {
        this.recipe = recipe;
        id = recipe.getId();
        input = padIngredients(recipe.getShapedRecipe());
        output = EmiStack.of(recipe.getOutput());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TRIntegration.ROLLING_MACHINE_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 16 + 18 * 3 + 24 + 26;
    }

    @Override
    public int getDisplayHeight() {
        return 18 * 3;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for (int i = 0; i < 9; ++i) {
            if (i < this.input.size()) {
                widgets.addSlot(this.input.get(i), 16 + i % 3 * 18, i / 3 * 18);
            } else {
                widgets.addSlot(EmiStack.EMPTY, 16 + i % 3 * 18, i / 3 * 18);
            }
        }

        widgets.addSlot(output, 16 + 18 * 3 + 24, (18 * 3 - 26) / 2).output(true).recipeContext(this);

        TRUIUtils.energyBar(widgets, recipe, 10, 0, 2);
        TRUIUtils.arrowRight(widgets, recipe, 16 + 18 * 3 + 4, (18 * 3 - 10) / 2);
        UIUtils.cookTime(widgets, recipe.getTime(), 16 + 18 * 3 + 2, 0);
    }

    private static List<EmiIngredient> padIngredients(ShapedRecipe recipe) {
        List<EmiIngredient> list = Lists.newArrayList();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
                    list.add(EmiStack.EMPTY);
                } else {
                    list.add(EmiIngredient.of(recipe.getIngredients().get(i++)));
                }
            }
        }
        return list;
    }
}
