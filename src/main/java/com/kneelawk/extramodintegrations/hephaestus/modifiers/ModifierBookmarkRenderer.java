package com.kneelawk.extramodintegrations.hephaestus.modifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.render.EmiRenderable;
import slimeknights.tconstruct.library.client.modifiers.ModifierIconManager;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

public class ModifierBookmarkRenderer implements EmiRenderable {
  private ModifierEntry entry;
  public ModifierBookmarkRenderer(ModifierEntry entry) {
    this.entry = entry;
  }
  @Override
  public void render(PoseStack matrices, int x, int y, float delta) {
    ModifierIconManager.renderIcon(matrices, entry.getModifier(), x, y, 100, 16);
  }
}
