package com.kneelawk.extramodintegrations.indrev;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import me.steven.indrev.WCustomTabPanel;
import me.steven.indrev.blockentities.modularworkbench.ModularWorkbenchBlockEntity;
import me.steven.indrev.components.InventoryComponent;
import me.steven.indrev.gui.screenhandlers.machines.ModularWorkbenchScreenHandler;
import me.steven.indrev.inventories.IRInventory;
import me.steven.indrev.packets.common.SelectModuleOnWorkbenchPacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import com.kneelawk.extramodintegrations.ExMIMod;

public class ModuleRecipeHandler implements StandardRecipeHandler<ModularWorkbenchScreenHandler> {
    private static final int CRAFTING_START = 3;
    private static final int CRAFTING_STOP = 15;
    private static final int MAX_INPUT_SLOTS = 6;

    private static final Field TAB_PANEL_WIDGETS;

    static {
        // Cursed code for setting the Modular Workbench tab
        Field tabPanelWidgets;
        try {
            tabPanelWidgets = WCustomTabPanel.class.getDeclaredField("tabWidgets");
            tabPanelWidgets.setAccessible(true);
        } catch (NoSuchFieldException e) {
            tabPanelWidgets = null;
            ExMIMod.LOGGER.warn(
                "[Extra Mod Integrations] Error getting access to Industrial Revolution Modular Workbench tab");
        }
        TAB_PANEL_WIDGETS = tabPanelWidgets;
    }

    @Override
    public List<Slot> getInputSources(ModularWorkbenchScreenHandler handler) {
        ModularInventory inv = getInventory(handler);
        if (inv == null) return List.of();

        List<Slot> inputs = Lists.newArrayList();

        for (int i = 0; i < 36; i++) {
            inputs.add(handler.getSlot(i));
        }

        addCraftingInputs(handler, inv.inventory, inputs);
        return inputs;
    }

    @Override
    public List<Slot> getCraftingSlots(ModularWorkbenchScreenHandler handler) {
        ModularInventory inv = getInventory(handler);
        if (inv == null) return List.of();

        List<Slot> inputs = Lists.newArrayList();

        addCraftingInputs(handler, inv.inventory, inputs);
        return inputs;
    }

    @Override
    public List<Slot> getCraftingSlots(EmiRecipe recipe, ModularWorkbenchScreenHandler handler) {
        if (!(recipe instanceof ModuleEmiRecipe mr)) return List.of();

        ModularInventory inv = getInventory(handler);
        if (inv == null) return List.of();

        setRecipe(inv.blockEntity, handler, mr);

        List<List<Slot>> slotsPerSize = getInputSlots(handler, inv.inventory);

        if (mr.inputs.size() > slotsPerSize.size()) {
            return List.of();
        } else {
            return slotsPerSize.get(mr.inputs.size() - 1);
        }
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == IRIntegration.MODULES_CATEGORY && recipe.supportsRecipeTree() &&
            recipe instanceof ModuleEmiRecipe moduleRecipe && moduleRecipe.inputs.size() <= MAX_INPUT_SLOTS;
    }

    private static @Nullable ModularInventory getInventory(ModularWorkbenchScreenHandler handler) {
        Optional<BlockEntity> be;
        // no better way to do this
        try {
            be = handler.getCtx().get(World::getBlockEntity);
        } catch (NullPointerException e) {
            be = Optional.empty();
        }

        if (be.isEmpty() || !(be.get() instanceof ModularWorkbenchBlockEntity wb)) return null;

        InventoryComponent invComponent = wb.getInventoryComponent();
        if (invComponent == null) return null;

        IRInventory irInv = invComponent.getInventory();

        return new ModularInventory(wb, irInv);
    }

    private static void addCraftingInputs(ModularWorkbenchScreenHandler handler, IRInventory irInv, List<Slot> inputs) {
        int slotCount = handler.slots.size();
        for (int i = 0; i < slotCount; i++) {
            Slot slot = handler.slots.get(i);
            if (slot.inventory == irInv && slot instanceof ValidatedSlot vSlot) {
                int invIndex = vSlot.getInventoryIndex();
                if (CRAFTING_START <= invIndex && invIndex < CRAFTING_STOP) {
                    inputs.add(slot);
                }
            }
        }
    }

    private static List<List<Slot>> getInputSlots(ModularWorkbenchScreenHandler handler, IRInventory irInv) {
        List<List<Slot>> inputsList = Lists.newArrayList();
        List<Slot> curInputs = Lists.newArrayList();

        int slotCount = handler.slots.size();
        for (int i = 0; i < slotCount; i++) {
            Slot slot = handler.slots.get(i);
            if (slot.inventory == irInv && slot instanceof ValidatedSlot vSlot) {
                int invIndex = vSlot.getInventoryIndex();
                if (CRAFTING_START <= invIndex && invIndex < CRAFTING_STOP) {
                    if (invIndex == CRAFTING_START && !curInputs.isEmpty()) {
                        inputsList.add(curInputs);
                        curInputs = Lists.newArrayList();
                    }
                    curInputs.add(slot);
                }
            }
        }

        if (!curInputs.isEmpty()) {
            inputsList.add(curInputs);
        }

        inputsList.sort(Comparator.comparingInt(List::size));

        return inputsList;
    }

    private static void setRecipe(ModularWorkbenchBlockEntity be, ModularWorkbenchScreenHandler sh,
                                  ModuleEmiRecipe recipe) {
        // set recipe client-side
        be.setSelectedRecipe(recipe.recipe.getId());

        // WARNING: extremely cursed code
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(sh.syncId);
        buf.writeIdentifier(recipe.recipe.getId());
        buf.writeBlockPos(be.getPos());
        ClientPlayNetworking.send(SelectModuleOnWorkbenchPacket.INSTANCE.getMODULE_SELECT_PACKET(), buf);

        // Cursed code for setting the Modular Workbench tab
        if (TAB_PANEL_WIDGETS != null && sh.getRootPanel() instanceof WCustomTabPanel tabPanel) {
            try {
                List<WWidget> tabs = (List<WWidget>) TAB_PANEL_WIDGETS.get(tabPanel);
                if (tabs.size() >= 2) {
                    tabs.get(1).onClick(0, 0, 0);
                } else {
                    ExMIMod.LOGGER.warn(
                        "[Extra Mod Integrations] Error setting Industrial Revolution Modular Workbench tab because the Modular Workbench is missing the crafting tab");
                }
            } catch (IllegalAccessException e) {
                ExMIMod.LOGGER.warn(
                    "[Extra Mod Integrations] Error setting Industrial Revolution Modular Workbench tab", e);
            }
        }

        sh.layoutSlots(recipe.recipe);
        sh.getRootPanel().addPainters();
    }

    private record ModularInventory(ModularWorkbenchBlockEntity blockEntity, IRInventory inventory) {
    }
}
