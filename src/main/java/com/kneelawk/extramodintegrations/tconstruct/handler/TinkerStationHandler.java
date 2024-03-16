package com.kneelawk.extramodintegrations.tconstruct.handler;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;
import slimeknights.tconstruct.tables.menu.TinkerStationContainerMenu;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

import java.util.ArrayList;
import java.util.List;

public class TinkerStationHandler implements StandardRecipeHandler<TinkerStationContainerMenu> {
    @Override
    public List<Slot> getInputSources(TinkerStationContainerMenu handler) {
        List<Slot> slots = new ArrayList<>(getCraftingSlots(handler));

        int start = handler.getInputSlots().size() + 3 + ArmorSlotType.values().length;
        slots.addAll(handler.slots.subList(start, start + 36));

        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(TinkerStationContainerMenu handler) {
        List<Slot> slots = new ArrayList<>();
        slots.add(handler.slots.get(0));
        slots.addAll(handler.getInputSlots());
        return slots;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == TiCCategories.MODIFIERS && recipe.supportsRecipeTree();
    }
}
