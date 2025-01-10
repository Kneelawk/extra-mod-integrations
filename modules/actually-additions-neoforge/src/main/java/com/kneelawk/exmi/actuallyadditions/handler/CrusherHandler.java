package com.kneelawk.exmi.actuallyadditions.handler;

import java.util.ArrayList;
import java.util.List;

import de.ellpeck.actuallyadditions.mod.inventory.CrusherContainer;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

import net.minecraft.world.inventory.Slot;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class CrusherHandler implements StandardRecipeHandler<CrusherContainer> {
    @Override
    public List<Slot> getInputSources(CrusherContainer handler) {
        List<Slot> list = new ArrayList<>();
        list.addAll(getCraftingSlots(handler));
        int inventoryStart = handler.isDouble ? 6 : 3;
        list.addAll(handler.slots.subList(inventoryStart, inventoryStart + 4 * 9));
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(CrusherContainer handler) {
        return List.of(handler.slots.get(0));
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == AAIntegration.CRUSHING && recipe.supportsRecipeTree();
    }
}
