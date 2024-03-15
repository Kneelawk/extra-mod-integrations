package com.kneelawk.extramodintegrations.tconstruct.stack;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.serializer.EmiStackSerializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        ModifierIconManager.renderIcon(draw, entry.getModifier(), x, y, 100, 16);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public NbtCompound getNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("level", entry.getLevel());
        return tag;
    }

    @Override
    public Object getKey() {
        return entry.getModifier();
    }

    @Override
    public Identifier getId() {
        return entry.getId();
    }

    @Override
    public List<TooltipComponent> getTooltip() {
        List<TooltipComponent> list = new ArrayList<>();
        list.add(TooltipComponent.of(getName().asOrderedText()));
        list.addAll(entry.getModifier().getDescriptionList(entry.getLevel())
                .stream()
                .map(Text::asOrderedText)
                .map(TooltipComponent::of)
                .toList());

        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            list.add(TooltipComponent.of(Text.literal(getId().toString()).formatted(Formatting.DARK_GRAY).asOrderedText()));
        }
        String namespace = getId().getNamespace();
        String mod = FabricLoader.getInstance()
                .getModContainer(namespace)
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getName)
                .orElse(namespace);
        list.add(TooltipComponent.of(Text.literal(mod).formatted(Formatting.BLUE, Formatting.ITALIC).asOrderedText()));
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public List<Text> getTooltipText() {
        return List.of();
    }

    @Override
    public Text getName() {
        return entry.getModifier().getDisplayName(entry.getLevel());
    }

    public static class Serializer implements EmiStackSerializer<ModifierEmiStack> {
        @Override
        public EmiStack create(Identifier id, NbtCompound nbt, long amount) {
            return new ModifierEmiStack(new ModifierEntry(new ModifierId(id), nbt.getInt("level")));
        }

        @Override
        public String getType() {
            return "tconstruct_modifier";
        }
    }
}
