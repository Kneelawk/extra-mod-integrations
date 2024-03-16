package com.kneelawk.extramodintegrations.util.widget;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ScaledSlotWidget extends SlotWidget {
    private final float scaleFactor;

    public ScaledSlotWidget(EmiIngredient stack, int x, int y, float scaleFactor) {
        super(stack, x, y);
        this.scaleFactor = scaleFactor;
    }

    @Override
    public Bounds getBounds() {
        Bounds orig = super.getBounds();
        return new Bounds(orig.x(), orig.y(), (int) (orig.width() * scaleFactor), (int) (orig.height() * scaleFactor));
    }

    @Override
    public void drawBackground(DrawContext draw, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = draw.getMatrices();
        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, 1);
        matrices.translate(-x / scaleFactor, -y / scaleFactor, 0);
        super.drawBackground(draw, mouseX, mouseY, delta);
        matrices.pop();
    }

    @Override
    public void drawSlotHighlight(DrawContext draw, Bounds bounds) {
        MatrixStack matrices = draw.getMatrices();
        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, 1);
        matrices.translate(-x / scaleFactor, -y / scaleFactor, 0);
        super.drawSlotHighlight(draw, super.getBounds());
        matrices.pop();
    }

    @Override
    public void drawStack(DrawContext draw, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = draw.getMatrices();
        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, 1);
        Bounds bounds = getBounds();
        int xOff = (int) ((bounds.width() - 16 * scaleFactor) / 2);
        int yOff = (int) ((bounds.width() - 16 * scaleFactor) / 2);
        int newX = bounds.x() + xOff;
        int newY = bounds.y() + yOff;
        matrices.translate(-newX / scaleFactor, -newY / scaleFactor, 0);
        getStack().render(draw, newX, newY, delta);
        matrices.pop();
    }
}
