package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;

import net.minecraft.client.gui.DrawContext;

public class NinePatchWidget extends Widget {
    private final NinePatchTexture texture;
    private final int x, y, w, h;

    public NinePatchWidget(NinePatchTexture texture, int x, int y, int w, int h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, w, h);
    }

    @Override
    public void render(DrawContext matrixStack, int i, int i1, float v) {
        texture.render(matrixStack.getMatrices(), x, y, w, h);
    }
}
