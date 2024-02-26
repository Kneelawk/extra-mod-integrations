package com.kneelawk.extramodintegrations.tconstruct.stack;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

import java.util.List;

public class ModifierEmiStack extends EmiStack {
    private final ModifierEntry entry;

    public ModifierEmiStack(ModifierEntry entry) {
        this.entry = entry;
    }

    @Override
    public EmiStack copy() {
        return null;
    }

    @Override
    public void render(DrawContext draw, int x, int y, float delta, int flags) {

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
        return null;
    }

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public List<Text> getTooltipText() {
        return null;
    }

    @Override
    public Text getName() {
        return null;
    }
}
