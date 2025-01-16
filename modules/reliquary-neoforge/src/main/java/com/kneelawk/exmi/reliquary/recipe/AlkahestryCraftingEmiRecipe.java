package com.kneelawk.exmi.reliquary.recipe;

import java.util.List;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reliquary.crafting.AlkahestryCraftingRecipe;
import reliquary.init.ModItems;
import reliquary.items.AlkahestryTomeItem;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.exmi.core.api.ExMITextures;
import com.kneelawk.exmi.reliquary.RIntegration;

public class AlkahestryCraftingEmiRecipe extends BasicEmiRecipe {
    private final EmiStack inputTome;
    private final EmiIngredient craftingIngredient;
    private final int chargeToRemove;
    private final EmiStack outputTome;
    private final EmiStack outputResult;

    public AlkahestryCraftingEmiRecipe(AlkahestryCraftingRecipe recipe, ResourceLocation id) {
        super(RIntegration.ALKAHESTRY_CRAFTING, id, 18 * 2 + 24 + 26, 18 * 2 + 24 + 18);

        inputTome = EmiStack.of(
            AlkahestryTomeItem.setCharge(new ItemStack(ModItems.ALKAHESTRY_TOME.get()), recipe.getChargeNeeded()));
        craftingIngredient = EmiIngredient.of(recipe.getCraftingIngredient());
        chargeToRemove = recipe.getChargeNeeded();
        outputTome = EmiStack.of(AlkahestryTomeItem.setCharge(new ItemStack(ModItems.ALKAHESTRY_TOME.get()),
            AlkahestryTomeItem.getChargeLimit() - recipe.getChargeNeeded()));
        outputResult = EmiStack.of(recipe.getResult());

        inputs = List.of(inputTome, craftingIngredient);
        outputs = List.of(outputTome, outputResult);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputTome, 0, 0);
        widgets.addSlot(craftingIngredient, 18, 0);
        widgets.addSlot(0, 18);
        widgets.addSlot(18, 18);

        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 * 2 + 4, (18 * 2 - 16) / 2);

        widgets.addText(Component.literal("-" + chargeToRemove), 18 * 2 + 4, 18 * 2 + 4, 0x555555, true);

        widgets.addSlot(outputResult, 18 * 2 + 24, (18 * 2 - 26) / 2).large(true).recipeContext(this);

        widgets.addTexture(ExMITextures.DOWN_ARROW, 18 + 1, 18 * 2 + 4);

        widgets.addSlot(outputTome, 18, 18 * 2 + 24).drawBack(false).recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
