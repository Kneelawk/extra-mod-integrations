package com.kneelawk.exmi.reliquary.recipe;

import java.util.List;

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

public class MortarEmiRecipe extends BasicEmiRecipe {
    private final EmiStack output;

    public MortarEmiRecipe(PotionEssence essence, ResourceLocation id) {
        super(RIntegration.APOTHECARY_MORTAR, id, 18 * 2 + 24 + 18, 18 + 18 + 18);
        inputs = essence.getIngredients().stream().map(pi -> (EmiIngredient) EmiStack.of(pi.getItem())).toList();
        ItemStack outputStack = new ItemStack(ModItems.POTION_ESSENCE.get());
        PotionHelper.addPotionContentsToStack(outputStack, essence.getPotionContents());
        output = EmiStack.of(outputStack);
        outputs = List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int inputLen = inputs.size();
        for (int i = 0; i < Math.max(3, inputLen); i++) {
            if (i < inputLen) {
                widgets.addSlot(inputs.get(i), 18 * i, 0).drawBack(false);
            } else {
                widgets.addSlot(18 * i, 0).drawBack(false);
            }
        }

        widgets.addTexture(ExMITextures.DOWN_ARROW, 18 + 1, 18 + 1);

        widgets.addSlot(RIntegration.MORTAR_STACK, 18, 18 * 2).drawBack(false);

        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 * 2 + 4, 18 * 2 + 1);

        widgets.addSlot(output, 18 * 2 + 24, 18 * 2).drawBack(false).recipeContext(this);
    }
}
