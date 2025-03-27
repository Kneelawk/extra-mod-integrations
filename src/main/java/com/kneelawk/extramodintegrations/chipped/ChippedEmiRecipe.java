package com.kneelawk.extramodintegrations.chipped;

import com.kneelawk.extramodintegrations.ExMIMod;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class ChippedEmiRecipe extends BasicEmiRecipe {

    public ChippedEmiRecipe(EmiRecipeCategory category, FlattenedRecipe recipe) {
        super(category, new Identifier(ExMIMod.MOD_ID, "/chipped/" + recipe.id), 80, 18);
        this.inputs.add(EmiIngredient.of(recipe.tag));
        this.outputs.add(EmiStack.of(recipe.result));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }

    public record FlattenedRecipe(
            String id,
            Ingredient tag,
            ItemStack result
    ) {
    }
}
