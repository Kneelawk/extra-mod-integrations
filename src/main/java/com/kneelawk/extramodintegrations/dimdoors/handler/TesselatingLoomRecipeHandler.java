package com.kneelawk.extramodintegrations.dimdoors.handler;

import com.kneelawk.extramodintegrations.dimdoors.DimDoorsCategories;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import org.dimdev.dimdoors.screen.TessellatingContainer;

import java.util.List;
import net.minecraft.world.inventory.Slot;

public class TesselatingLoomRecipeHandler implements StandardRecipeHandler<TessellatingContainer> {
    @Override
    public List<Slot> getInputSources(TessellatingContainer handler) {
        return handler.slots.subList(1, 46);
    }

    @Override
    public List<Slot> getCraftingSlots(TessellatingContainer handler) {
        return List.of(
                handler.slots.get(1),
                handler.slots.get(4),
                handler.slots.get(7),
                handler.slots.get(2),
                handler.slots.get(5),
                handler.slots.get(8),
                handler.slots.get(3),
                handler.slots.get(6),
                handler.slots.get(9)
        );
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == DimDoorsCategories.TESSELATING && recipe.supportsRecipeTree();
    }
}
