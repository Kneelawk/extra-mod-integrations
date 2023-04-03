package com.kneelawk.extramodintegrations.hephaestus.modifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

public class ModifierRenderer implements EmiRenderable {
  private ModifierEntry entry;
  public ModifierRenderer(ModifierEntry entry) {
    this.entry = entry;
  }

  @Override
  public void render(PoseStack matrices, int x, int y, float delta) {
    if (entry != null) {
      Component name = entry.getModifier().getDisplayName(entry.getLevel());
//      Font fontRenderer = getFontRenderer(Minecraft.getInstance(), entry);
//      int x = (width - fontRenderer.width(name)) / 2;
//      fontRenderer.drawShadow(matrices, name, x, 1, -1);
    }
  }
}
