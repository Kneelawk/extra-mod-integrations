package com.kneelawk.extramodintegrations.tconstruct.stack;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.serializer.EmiStackSerializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.modifiers.ModifierIconManager;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.ArrayList;
import java.util.List;

public class ModifierEmiStack extends EmiStack {
    private final ModifierEntry entry;

    private ModifierEmiStack(ModifierEntry entry) {
        this.entry = entry;
    }

    public static EmiStack of(ModifierEntry entry) {
        return new ModifierEmiStack(entry);
    }

    @Override
    public EmiStack copy() {
        return new ModifierEmiStack(entry);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        ModifierIconManager.renderIcon(draw, entry.getModifier(), x, y, 100, 16);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public CompoundTag getNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("level", entry.getLevel());
        return tag;
    }

    @Override
    public Object getKey() {
        return entry.getModifier();
    }

    @Override
    public ResourceLocation getId() {
        return entry.getId();
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>();
        list.add(ClientTooltipComponent.create(getName().getVisualOrderText()));
        list.addAll(entry.getModifier().getDescriptionList(entry.getLevel())
                .stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .toList());

        if (Minecraft.getInstance().options.advancedItemTooltips) {
            list.add(ClientTooltipComponent.create(Component.literal(getId().toString()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText()));
        }
        String namespace = getId().getNamespace();
        String mod = FabricLoader.getInstance()
                .getModContainer(namespace)
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getName)
                .orElse(namespace);
        list.add(ClientTooltipComponent.create(Component.literal(mod).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC).getVisualOrderText()));
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public List<Component> getTooltipText() {
        return List.of();
    }

    @Override
    public Component getName() {
        return entry.getModifier().getDisplayName(entry.getLevel());
    }

    public static class Serializer implements EmiStackSerializer<ModifierEmiStack> {
        @Override
        public EmiStack create(ResourceLocation id, CompoundTag nbt, long amount) {
            return new ModifierEmiStack(new ModifierEntry(new ModifierId(id), nbt.getInt("level")));
        }

        @Override
        public String getType() {
            return "tconstruct_modifier";
        }
    }
}
