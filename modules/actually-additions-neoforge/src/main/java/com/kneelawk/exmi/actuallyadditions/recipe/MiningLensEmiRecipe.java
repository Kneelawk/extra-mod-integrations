package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.crafting.MiningLensRecipe;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.network.chat.Component;
import net.minecraft.util.random.Weight;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class MiningLensEmiRecipe extends BasicEmiRecipe {
    private static final EmiStack RECONSTRUCTOR = EmiStack.of(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.getItem());
    private static final EmiStack LENS = EmiStack.of(ActuallyItems.LENS_OF_THE_MINER);

    private final Weight weight;

    public MiningLensEmiRecipe(RecipeHolder<MiningLensRecipe> holder) {
        super(AAIntegration.MINING_LENS, holder.id(), 96, 60);

        MiningLensRecipe recipe = holder.value();
        this.inputs = List.of(EmiIngredient.of(recipe.getInput()));
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(null)));
        this.weight = recipe.getWeight();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AssetUtil.getGuiLocation("gui_nei_atomic_reconstructor"), 0, 0, 96, 60, 0, 0);

        widgets.addText(Component.translatable("jei.actuallyadditions.mining_lens.weight"), 2, 42, 0, false);
        widgets.addText(Component.literal(String.valueOf(weight)), 16, 52, 0, false)
            .horizontalAlign(TextWidget.Alignment.CENTER);

        widgets.addSlot(inputs.get(0), 4, 18)
            .drawBack(false);
        widgets.addSlot(RECONSTRUCTOR, 26, 19)
            .drawBack(false);
        widgets.addSlot(LENS, 42, 19)
            .drawBack(false);
        widgets.addSlot(outputs.get(0), 62, 14)
            .drawBack(false)
            .large(true)
            .recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
