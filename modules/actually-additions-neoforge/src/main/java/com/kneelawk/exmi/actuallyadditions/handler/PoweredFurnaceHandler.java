package com.kneelawk.exmi.actuallyadditions.handler;

import java.util.ArrayList;
import java.util.List;

import de.ellpeck.actuallyadditions.mod.inventory.ContainerFurnaceDouble;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

import net.minecraft.world.inventory.Slot;

public class PoweredFurnaceHandler implements StandardRecipeHandler<ContainerFurnaceDouble> {
    @Override
    public List<Slot> getInputSources(ContainerFurnaceDouble handler) {
        List<Slot> list = new ArrayList<>();
        list.addAll(getCraftingSlots(handler));
        list.addAll(handler.slots.subList(4, 4 + 4 * 9));
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(ContainerFurnaceDouble handler) {
        return handler.slots.subList(0, 1);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.SMELTING && recipe.supportsRecipeTree();
    }
}
