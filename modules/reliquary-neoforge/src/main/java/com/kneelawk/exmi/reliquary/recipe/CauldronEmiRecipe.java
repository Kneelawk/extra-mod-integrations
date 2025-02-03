package com.kneelawk.exmi.reliquary.recipe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reliquary.init.ModItems;
import reliquary.util.potions.PotionEssence;
import reliquary.util.potions.PotionHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.exmi.core.api.ExMITextures;
import com.kneelawk.exmi.reliquary.RIntegration;

public class CauldronEmiRecipe extends BasicEmiRecipe {
    private final EmiStack output;
    private final EmiStack essenceStack;
    private final EmiStack[] extraInputs;

    public CauldronEmiRecipe(PotionEssence essence, EmiStack[] extraInputs, EmiStack output, ResourceLocation id) {
        super(RIntegration.APOTHECARY_CAULDRON, id, 18 + 24 + 18 + 24 + 18, 18 + 18 + 18);
        ItemStack outputStack = output.getItemStack();
        PotionHelper.addPotionContentsToStack(outputStack, essence.getPotionContents());
        this.output = EmiStack.of(outputStack);
        ItemStack is = new ItemStack(ModItems.POTION_ESSENCE.get());
        PotionHelper.addPotionContentsToStack(is, essence.getPotionContents());
        essenceStack = EmiStack.of(is);
        this.extraInputs = extraInputs;
        this.inputs =
            Stream.concat(Stream.of(essenceStack), Arrays.stream(extraInputs)).<EmiIngredient>map(e -> e).toList();
        this.outputs = List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(essenceStack, 0, 18 + 18).drawBack(false);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 4, 18 + 18 + 1);
        widgets.addSlot(RIntegration.CAULDRON_STACK, 18 + 24, 18 + 18).drawBack(false);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 24 + 18 + 4, 18 + 18 + 1);
        widgets.addSlot(output, 18 + 24 + 18 + 24, 18 + 18).drawBack(false).recipeContext(this);
        widgets.addTexture(ExMITextures.DOWN_ARROW, 18 + 24 + 1, 18 + 1);

        int startX = 18 + 24 + 9 - extraInputs.length * 18 / 2;
        for (int i = 0; i < extraInputs.length; i++) {
            widgets.addSlot(extraInputs[i], startX + i * 18, 0).drawBack(false);
        }
    }
}
