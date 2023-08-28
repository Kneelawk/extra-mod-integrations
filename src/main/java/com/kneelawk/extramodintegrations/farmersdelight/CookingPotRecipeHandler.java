package com.kneelawk.extramodintegrations.farmersdelight;

import com.nhoryzon.mc.farmersdelight.entity.block.screen.CookingPotScreenHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class CookingPotRecipeHandler implements StandardRecipeHandler<CookingPotScreenHandler> {
    @Override
    public List<Slot> getInputSources(CookingPotScreenHandler handler) {
        List<Slot> result = new ArrayList<>(handler.slots.subList(0, 6));
        result.addAll(handler.slots.subList(9, 9 + 36));
        return result;
    }

    @Override
    public List<Slot> getCraftingSlots(CookingPotScreenHandler handler) {
        List<Slot> result = new ArrayList<>();
        result.add(handler.slots.get(7));
        result.addAll(handler.slots.subList(0, 6));
        return result;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == FDIntegration.COOKING_CATEGORY && recipe.supportsRecipeTree();
    }
}
