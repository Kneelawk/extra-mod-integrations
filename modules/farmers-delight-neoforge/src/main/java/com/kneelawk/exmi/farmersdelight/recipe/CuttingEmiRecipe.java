/*
 * Copyright (c) 2020 vectorwing, the Farmer's Delight Refabricated authors
 * License available at https://github.com/MehVahdJukaar/FarmersDelightRefabricated/blob/b6690b2106abc6205021e40f9117c03f36323362/LICENSE
 */

package com.kneelawk.exmi.farmersdelight.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import vectorwing.farmersdelight.FarmersDelight;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.exmi.farmersdelight.FDRecipeCategories;

public class CuttingEmiRecipe implements EmiRecipe {
    private static final ResourceLocation BACKGROUND =
        ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/jei/cutting_board.png");
    public static final int OUTPUT_GRID_X = 69;
    public static final int OUTPUT_GRID_Y = 3;

    private final ResourceLocation id;
    private final EmiIngredient tool;
    private final EmiIngredient input;
    private final List<EmiStack> outputs;

    public CuttingEmiRecipe(ResourceLocation id, EmiIngredient tool, EmiIngredient input, List<EmiStack> outputs) {
        this.id = id;
        this.tool = tool;
        this.input = input;
        this.outputs = outputs;

    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FDRecipeCategories.CUTTING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(tool);
    }

    @Override
    public int getDisplayWidth() {
        return 111;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0, 111, 44, 4, 7);

        widgets.addSlot(tool, 11, 0).drawBack(false);
        widgets.addSlot(input, 11, 19).drawBack(false);

        int size = outputs.size();
        int centerX = size > 1 ? 1 : 10;
        int centerY = size > 2 ? 1 : 10;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            EmiIngredient output = outputs.get(i);
            widgets.addSlot(output, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset)
                .backgroundTexture(BACKGROUND, output.getChance() < 1 ? 18 : 0, 58).recipeContext(this);
        }
    }
}
