package com.kneelawk.extramodintegrations.appeng;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import appeng.core.definitions.AEBlocks;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.block.Blocks;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;

import static com.kneelawk.extramodintegrations.ExMIMod.gui;

public class TransformEmiRecipe implements EmiRecipe {
    private final TransformRecipe recipe;
    private final Identifier id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;
    private final EmiIngredient fluidCircumstance;
    private final EmiIngredient explosionCircumstance;

    public TransformEmiRecipe(TransformRecipe recipe) {
        this.recipe = recipe;
        id = recipe.getId();

        inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        outputs = List.of(EmiStack.of(recipe.getResultItem()));

        if (recipe.circumstance.isFluid()) {
            fluidCircumstance =
                EmiIngredient.of(recipe.circumstance.getFluidsForRendering().stream().map(EmiStack::of).toList());
        } else {
            fluidCircumstance = EmiIngredient.of(Ingredient.EMPTY);
        }

        if (recipe.circumstance.isExplosion()) {
            explosionCircumstance = EmiIngredient.of(List.of(EmiStack.of(Blocks.TNT), EmiStack.of(AEBlocks.TINY_TNT)));
        } else {
            explosionCircumstance = EmiIngredient.of(Ingredient.EMPTY);
        }
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(fluidCircumstance, explosionCircumstance);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.WORLD_INTERACTION;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 18 + 36 + 18 + 36 + 18;
    }

    @Override
    public int getDisplayHeight() {
        return 3 * 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        switch (inputs.size()) {
            case 1 -> widgets.addSlot(inputs.get(0), 0, 18);
            case 2 -> {
                widgets.addSlot(inputs.get(0), 0, 9);
                widgets.addSlot(inputs.get(1), 0, 27);
            }
            case 3 -> {
                widgets.addSlot(inputs.get(0), 0, 0);
                widgets.addSlot(inputs.get(1), 0, 18);
                widgets.addSlot(inputs.get(2), 0, 36);
            }
            default -> ExMIMod.LOGGER.warn(
                "Encountered AE2 Transform recipe {} with more than 3 inputs. It will not be rendered correctly.", id);
        }

        widgets.addSlot(outputs.get(0), 18 + 36 + 18 + 36, 18).recipeContext(this);

        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 10, 19);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 36 + 18 + 10, 19);

        if (recipe.circumstance.isFluid()) {
            widgets.addSlot(fluidCircumstance, 18 + 36, 18).drawBack(false).catalyst(true);
            widgets.addText(gui("ae2.submerge_in"), 18 + 36 + 9, 0, 0xFF3F3F3F, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);
        }

        if (recipe.circumstance.isExplosion()) {
            widgets.addSlot(explosionCircumstance, 18 + 36, 18).drawBack(false);
            widgets.addText(gui("ae2.explosion"), 18 + 36 + 9, 0, 0xFF3F3F3F, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);
        }
    }
}
