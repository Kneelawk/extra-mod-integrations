package com.kneelawk.extramodintegrations.hephaestus.modifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

import java.util.ArrayList;
import java.util.List;

public class ModifierEmiStack extends EmiStack {
  private final ModifierBookmarkRenderer renderer;
  private final ModifierEntry entry;
  private final Component name;
  public ModifierEmiStack(ModifierEntry entry) {
    this.entry = entry;
    this.renderer = new ModifierBookmarkRenderer(entry);
    this.name = new TranslatableComponent("jei.tconstruct.modifier_ingredient",
      new TranslatableComponent(entry.getModifier().getTranslationKey()))
      .withStyle(style -> style.withColor(entry.getModifier().getTextColor()));
  }

  @Override
  public EmiStack copy() {
    return new ModifierEmiStack(entry);
  }

  @Override
  public void render(PoseStack matrices, int x, int y, float delta, int flags) {
    renderer.render(matrices, x, y, delta);
  }

  @Override
  public boolean isEmpty() {
    return entry == null;
  }

  @Override
  public CompoundTag getNbt() {
    return null;
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
  public List<Component> getTooltipText() {
    List<Component> tooltip = new ArrayList<>();
    tooltip.add(name);
    tooltip.addAll(entry.getModifier().getDescriptionList());
    if (Minecraft.getInstance().options.advancedItemTooltips) {
      tooltip.add(new TextComponent(entry.getId().toString()).withStyle(ChatFormatting.DARK_GRAY));
    }
    return tooltip;
  }

  @Override
  public List<ClientTooltipComponent> getTooltip() {
    return getTooltipText().stream()
      .map(Component::getVisualOrderText)
      .map(ClientTooltipComponent::create)
      .toList();
  }

  @Override
  public Component getName() {
    return name;
  }
}
