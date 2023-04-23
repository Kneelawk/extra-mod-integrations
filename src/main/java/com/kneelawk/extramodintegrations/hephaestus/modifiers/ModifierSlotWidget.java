package com.kneelawk.extramodintegrations.hephaestus.modifiers;

import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.config.EmiConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

public class ModifierSlotWidget extends SlotWidget {
    private final ModifierEntry entry;
    private final int width;
    private final int height;
    public ModifierSlotWidget(ModifierEntry entry, int x, int y, int width, int height) {
        super(new ModifierEmiStack(entry), x, y);
        this.entry = entry;
        this.width = width;
        this.height = height;
        this.bounds = new Bounds(x, y, width, height);
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (entry != null) {
            Text name = entry.getModifier().getDisplayName(entry.getLevel());
            TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;
            int xOffset = (width - fontRenderer.getWidth(name)) / 2;
            fontRenderer.drawWithShadow(matrices, name, x + xOffset, y+1, -1);
        }

        if (EmiConfig.showHoverOverlay && bounds.contains(mouseX, mouseY)) {
            EmiRenderHelper.drawSlotHightlight(matrices, bounds.x(), bounds.y(), bounds.width(),
                    bounds.height());
        }
    }
}
