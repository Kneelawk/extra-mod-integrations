package com.kneelawk.exmi.reliquary.recipe;

import java.util.List;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import reliquary.crafting.AlkahestryChargingRecipe;
import reliquary.init.ModItems;
import reliquary.item.AlkahestryTomeItem;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.exmi.core.api.ExMITextures;
import com.kneelawk.exmi.reliquary.RIntegration;

public class AlkahestryChargingEmiRecipe extends BasicEmiRecipe {
    private final EmiStack inputTome;
    private final EmiIngredient chargingIngredient;
    private final int chargeToAdd;
    private final EmiStack outputTome;

    public AlkahestryChargingEmiRecipe(AlkahestryChargingRecipe recipe, ResourceLocation id) {
        super(RIntegration.ALKAHESTRY_CHARGING, id, 18 * 2 + 24 + 26, 18 * 2);

        inputTome = EmiStack.of(AlkahestryTomeItem.setCharge(new ItemStack(ModItems.ALKAHESTRY_TOME.get()), 0));
        outputTome = EmiStack.of(
            AlkahestryTomeItem.setCharge(new ItemStack(ModItems.ALKAHESTRY_TOME.get()), recipe.getChargeToAdd()));
        chargingIngredient = EmiIngredient.of(recipe.getChargingIngredient());
        chargeToAdd = recipe.getChargeToAdd();
        inputs = List.of(inputTome, chargingIngredient);
        outputs = List.of(outputTome);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputTome, 0, 0);
        widgets.addSlot(chargingIngredient, 18, 0);
        widgets.addSlot(0, 18);
        widgets.addSlot(18, 18);

        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 * 2 + 4, (18 * 2 - 16) / 2);

        widgets.addSlot(outputTome, 18 * 2 + 24, (18 * 2 - 26) / 2).large(true).recipeContext(this);

        widgets.addText(Component.literal("+" + chargeToAdd), 18 * 2 + 4, 3, 0x00AA00, true);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
