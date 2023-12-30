package com.kneelawk.extramodintegrations.appeng;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import appeng.menu.implementations.InscriberMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

import net.minecraft.screen.slot.Slot;

public class InscriberRecipeHandler implements StandardRecipeHandler<InscriberMenu> {
    @Override
    public List<Slot> getInputSources(InscriberMenu handler) {
        List<Slot> inputs = Lists.newArrayList();

        // player inventory
        for (int i = 4; i < 40; i++) {
            inputs.add(handler.getSlot(i));
        }

        inputs.add(handler.getSlot(42));
        inputs.add(handler.getSlot(40));
        inputs.add(handler.getSlot(41));

        return inputs;
    }

    @Override
    public List<Slot> getCraftingSlots(InscriberMenu handler) {
        List<Slot> inputs = Lists.newArrayList();

        inputs.add(handler.getSlot(42));
        inputs.add(handler.getSlot(40));
        inputs.add(handler.getSlot(41));

        return inputs;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == AE2Integration.INSCRIBER_CATEGORY && recipe.supportsRecipeTree();
    }
}
