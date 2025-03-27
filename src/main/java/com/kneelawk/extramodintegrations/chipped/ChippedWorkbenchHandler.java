package com.kneelawk.extramodintegrations.chipped;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import earth.terrarium.chipped.common.menus.WorkbenchMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;

public class ChippedWorkbenchHandler implements StandardRecipeHandler<WorkbenchMenu> {

    @Override
    public List<Slot> getInputSources(WorkbenchMenu handler) {
        return handler.slots.subList(0, 35);
    }

    @Override
    public List<Slot> getCraftingSlots(WorkbenchMenu handler) {
        return List.of();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() instanceof ChippedIntegration.ChippedEmiRecipeCategory; // && correct table.
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<WorkbenchMenu> context) {
        return recipe.getCategory().getName().getString().equals(context.getScreen().getTitle().getString()) && context.getInventory().canCraft(recipe);
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<WorkbenchMenu> context) {

        // context.getAmount(). Amount doesn't apply to Chipped Workbench.
        MinecraftClient client = MinecraftClient.getInstance();

        List<Slot> slots = getInputSources(context.getScreenHandler());
        EmiIngredient ingredient = recipe.getInputs().get(0);
        Item output = recipe.getOutputs().get(0).getItemStack().getItem();

        for (int i = 0; i < slots.size(); i++) {
            EmiStack slotStack = EmiStack.of(slots.get(i).getStack());
            if (ingredient.getEmiStacks().contains(slotStack)) {
                // Technically this should work, looks ok in client side, but server refuses to actually craft.
                // So used client.interactionManager to simulate real click.
                // context.getScreenHandler().selectStack(i);
                client.interactionManager.clickSlot(context.getScreenHandler().syncId, i, 0, SlotActionType.PICKUP, client.player);
                context.getScreenHandler().results().stream()
                        .filter(s -> s.getItem().equals(output))
                        .findFirst()
                        .ifPresent(itemStack -> context.getScreenHandler().setChosenStack(itemStack));

                MinecraftClient.getInstance().setScreen(context.getScreen());
                return true;
            }
        }

        return false;
    }
}
