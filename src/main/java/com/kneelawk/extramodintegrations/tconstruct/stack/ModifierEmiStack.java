package com.kneelawk.extramodintegrations.tconstruct.stack;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.client.modifiers.ModifierIconManager;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

import java.util.ArrayList;
import java.util.List;

public class ModifierEmiStack extends EmiStack {
    private final ModifierEntry entry;

    public ModifierEmiStack(ModifierEntry entry) {
        this.entry = entry;
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
        return null;
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
}
