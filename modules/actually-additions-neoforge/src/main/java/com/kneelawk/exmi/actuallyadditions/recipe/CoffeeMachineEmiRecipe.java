package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.ActuallyTags;
import de.ellpeck.actuallyadditions.mod.crafting.CoffeeIngredientRecipe;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class CoffeeMachineEmiRecipe extends BasicEmiRecipe {
    private final String extraText;
    private final int maxAmplifier;

    public CoffeeMachineEmiRecipe(RecipeHolder<CoffeeIngredientRecipe> holder) {
        super(AAIntegration.COFFEE_MACHINE, holder.id(), 126, 92);

        CoffeeIngredientRecipe recipe = holder.value();
        this.inputs = List.of(
            EmiIngredient.of(ActuallyTags.Items.COFFEE_BEANS),
            EmiIngredient.of(recipe.getIngredient()),
            EmiStack.of(ActuallyItems.EMPTY_CUP)
        );
        ItemStack outputStack = ActuallyItems.COFFEE_CUP.toStack();
        ActuallyAdditionsAPI.methodHandler.addRecipeEffectToStack(outputStack, recipe);
        this.outputs = List.of(EmiStack.of(outputStack));
        this.extraText = recipe.getExtraText();
        this.maxAmplifier = recipe.getMaxAmplifier();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AssetUtil.getGuiLocation("gui_nei_coffee_machine"), 0, 0, 126, 92, 0, 0);

        if (extraText != null && !extraText.isEmpty()) {
            widgets.addText(Component.translatable("jei.actuallyadditions.coffee.special").append(":"), 2, 4, 0x404040,
                false);
            widgets.addText(Component.translatable(extraText), 2, 16, 0x404040, false);
        }

        if (maxAmplifier > 0) {
            widgets.addText(
                Component.translatable("jei.actuallyadditions.coffee.maxAmount").append(": " + maxAmplifier), 2, 28,
                0x404040, false);
        }

        widgets.addSlot(inputs.get(0), 1, 38)
            .drawBack(false);
        widgets.addSlot(inputs.get(1), 89, 20)
            .drawBack(false);
        widgets.addSlot(inputs.get(2), 44, 38)
            .drawBack(false);
        widgets.addSlot(outputs.get(0), 44, 69)
            .drawBack(false)
            .recipeContext(this);
    }
}
