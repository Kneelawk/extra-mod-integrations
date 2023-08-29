package com.kneelawk.extramodintegrations.conjuring;

import com.glisco.conjuring.util.SoulfireForgeScreenHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class SoulfireForgeRecipeHandler implements StandardRecipeHandler<SoulfireForgeScreenHandler> {
    @Override
    public List<Slot> getInputSources(SoulfireForgeScreenHandler handler) {
        List<Slot> result = new ArrayList<>();
        result.addAll(handler.slots.subList(0, 9));
        // skip result slot
        result.addAll(handler.slots.subList(10, 10 + 36));
        return result;
    }

    @Override
    public List<Slot> getCraftingSlots(SoulfireForgeScreenHandler handler) {
        return handler.slots.subList(0, 9);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == ConjuringIntegration.SOULFIRE_FORGE && recipe.supportsRecipeTree();
    }
}
