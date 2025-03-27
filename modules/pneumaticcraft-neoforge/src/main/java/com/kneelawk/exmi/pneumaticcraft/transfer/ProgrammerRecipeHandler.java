package com.kneelawk.exmi.pneumaticcraft.transfer;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import me.desht.pneumaticcraft.api.drone.IProgWidget;
import me.desht.pneumaticcraft.client.gui.ProgrammerScreen;
import me.desht.pneumaticcraft.client.util.PointXY;
import me.desht.pneumaticcraft.common.block.entity.drone.ProgrammerBlockEntity;
import me.desht.pneumaticcraft.common.drone.ProgWidgetUtils;
import me.desht.pneumaticcraft.common.drone.progwidgets.ProgWidgetCrafting;
import me.desht.pneumaticcraft.common.drone.progwidgets.ProgWidgetItemFilter;
import me.desht.pneumaticcraft.common.inventory.ProgrammerMenu;
import me.desht.pneumaticcraft.common.network.NetworkHandler;
import me.desht.pneumaticcraft.common.network.PacketProgrammerSync;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public class ProgrammerRecipeHandler implements EmiRecipeHandler<ProgrammerMenu> {

    @Override
    public EmiPlayerInventory getInventory(AbstractContainerScreen<ProgrammerMenu> screen) {
        // we do have an inventory, but it's not used for crafting
        return new EmiPlayerInventory(List.of());
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<ProgrammerMenu> ctx) {
        return recipe instanceof EmiCraftingRecipe && ctx.getScreen() instanceof ProgrammerScreen ps && findSuitableCraftingWidget(ps) != null;
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<ProgrammerMenu> ctx) {
        if (!(recipe instanceof EmiCraftingRecipe craftingRecipe)) return false;
        AbstractContainerScreen<ProgrammerMenu> screen = ctx.getScreen();
        if (!(screen instanceof ProgrammerScreen programmerScreen)) return false;

        IProgWidget craftingWidget = findSuitableCraftingWidget(programmerScreen);

        List<ProgWidgetItemFilter>
            params = makeFilterWidgets(programmerScreen, craftingWidget, craftingRecipe, craftingWidget == null);
        if (params.isEmpty()) return false;
        ProgrammerBlockEntity programmer = programmerScreen.te;
        programmer.progWidgets.addAll(params);
        NetworkHandler.sendToServer(PacketProgrammerSync.forBlockEntity(programmer));
        ProgWidgetUtils.updatePuzzleConnections(programmer.progWidgets);
        return true;
    }

    private static IProgWidget findSuitableCraftingWidget(ProgrammerScreen programmerScreen) {
        // find a visible crafting widget which doesn't already have any item filters attached
        return programmerScreen.te.progWidgets.stream()
            .filter(w -> w instanceof ProgWidgetCrafting && programmerScreen.isVisible(w))
            .filter(w -> w.getConnectedParameters()[0] == null && w.getConnectedParameters()[1] == null && w.getConnectedParameters()[2] == null)
            .findFirst()
            .orElse(null);
    }

    private static List<ProgWidgetItemFilter> makeFilterWidgets(ProgrammerScreen programmerScreen, IProgWidget craftingWidget, EmiCraftingRecipe recipe, boolean uniqueStacks) {
        List<EmiIngredient> l = recipe.getInputs();

        if (!recipe.shapeless) {
            if (l.size() != 9) {
                // expecting a standard 3x3 grid for shaped crafting
                return Collections.emptyList();
            }

            // if recipe has empty ingredients in left column, shift left (possibly twice)
            for (int n = 0; n < 2; n++) {
                if (l.get(0).isEmpty() && l.get(3).isEmpty() && l.get(6).isEmpty()) {
                    for (int i = 0; i < l.size(); i++) {
                        if (i % 3 == 2) {
                            l.set(i, EmiStack.EMPTY);
                        } else {
                            l.set(i, l.get(i + 1));
                        }
                    }
                }
            }
        }

        PointXY base;
        ProgWidgetItemFilter filterWidget = new ProgWidgetItemFilter();
        if (craftingWidget == null) {
            Rect2i bounds = programmerScreen.getProgrammerBounds();
            base = programmerScreen.mouseToWidgetCoords(programmerScreen.getGuiLeft() + bounds.getX() + 50, programmerScreen.getGuiTop() + bounds.getY() + 50, filterWidget);
        } else {
            base = new PointXY(craftingWidget.getX() + craftingWidget.getWidth() / 2, craftingWidget.getY());
        }

        ImmutableList.Builder<ProgWidgetItemFilter> builder = ImmutableList.builder();
        int c = 0;
        for (int n = 0; n < l.size(); n++) {
            EmiIngredient ingredient = l.get(n);
            if (!ingredient.isEmpty() || craftingWidget != null && n % 3 < 2 && !l.get(n + 1).isEmpty()) {
                ItemStack stack;
                // todo update when pnc allows tag filters in crafting programs
                /*if (ingredient instanceof TagEmiIngredient tei) {
                    TagKey<Item> tagKey = (TagKey<Item>) tei.key;
                    stack = ModItems.TAG_FILTER.toStack();
                    TagFilterItem.setConfiguredTagList(stack, Set.of(tagKey));
                } else*/ {
                    stack = ingredient.getEmiStacks().getFirst().getItemStack();
                }
                ProgWidgetItemFilter w = ProgWidgetItemFilter.withFilter(stack);
                if (craftingWidget == null) {
                    w.setPosition(base.x() + c++ * filterWidget.getWidth() / 2, base.y());
                } else {
                    w.setPosition(base.x() + (n % 3) * filterWidget.getWidth() / 2, base.y() + (n / 3) * filterWidget.getHeight() / 2);
                }
                builder.add(w);
            }
        }

        return builder.build();
    }

}
