package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.mod.crafting.CrushingRecipe;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class CrushingEmiRecipe extends BasicEmiRecipe {
    public CrushingEmiRecipe(RecipeHolder<CrushingRecipe> holder) {
        super(AAIntegration.CRUSHING, holder.id(), 56, 79);

        CrushingRecipe recipe = holder.value();
        this.inputs = List.of(EmiIngredient.of(recipe.getInput()));
        this.outputs = List.of(
            EmiStack.of(recipe.getOutputOne()).setChance(recipe.getFirstChance()),
            EmiStack.of(recipe.getOutputTwo()).setChance(recipe.getSecondChance())
        );
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AssetUtil.getGuiLocation("gui_grinder"), 0, 0, 56, 79, 60, 13);

        widgets.addSlot(inputs.get(0), 19, 7)
            .drawBack(false);
        widgets.addSlot(outputs.get(0), 7, 55)
            .drawBack(false)
            .recipeContext(this);
        widgets.addSlot(outputs.get(1), 31, 55)
            .drawBack(false)
            .recipeContext(this);
    }
}
