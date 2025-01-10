package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.crafting.LaserRecipe;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class LaserEmiRecipe extends BasicEmiRecipe {
    private static final EmiStack RECONSTRUCTOR = EmiStack.of(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.getItem());

    public LaserEmiRecipe(RecipeHolder<LaserRecipe> holder) {
        super(AAIntegration.LASER, holder.id(), 96, 60);

        LaserRecipe recipe = holder.value();
        this.inputs = List.of(EmiIngredient.of(recipe.getInput()));
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(null)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AssetUtil.getGuiLocation("gui_nei_atomic_reconstructor"), 0, 0, 96, 60, 0, 0);

        widgets.addSlot(inputs.get(0), 4, 18)
            .drawBack(false);
        widgets.addSlot(RECONSTRUCTOR, 34, 19)
            .drawBack(false);
        widgets.addSlot(outputs.get(0), 62, 14)
            .drawBack(false)
            .large(true)
            .recipeContext(this);
    }
}
