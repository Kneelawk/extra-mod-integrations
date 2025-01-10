package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.mod.crafting.EmpowererRecipe;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class EmpoweringEmiRecipe extends BasicEmiRecipe {
    public EmpoweringEmiRecipe(RecipeHolder<EmpowererRecipe> holder) {
        super(AAIntegration.EMPOWERER, holder.id(), 135, 80);

        EmpowererRecipe recipe = holder.value();
        this.inputs = List.of(
            EmiIngredient.of(recipe.getInput()),
            EmiIngredient.of(recipe.getStandOne()),
            EmiIngredient.of(recipe.getStandTwo()),
            EmiIngredient.of(recipe.getStandThree()),
            EmiIngredient.of(recipe.getStandFour())
        );
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(null)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AssetUtil.getGuiLocation("gui_nei_empowerer"), 0, 0, 135, 80, 0, 0);

        widgets.addSlot(inputs.get(0), 27, 27)
            .drawBack(false)
            .large(true);
        widgets.addSlot(inputs.get(1), 1, 31)
            .drawBack(false);
        widgets.addSlot(inputs.get(2), 31, 1)
            .drawBack(false);
        widgets.addSlot(inputs.get(3), 61, 31)
            .drawBack(false);
        widgets.addSlot(inputs.get(4), 31, 61)
            .drawBack(false);
        widgets.addSlot(outputs.get(0), 108, 27)
            .drawBack(false)
            .large(true)
            .recipeContext(this);
    }
}
