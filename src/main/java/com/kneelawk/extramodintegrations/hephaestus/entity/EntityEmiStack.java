package com.kneelawk.extramodintegrations.hephaestus.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityEmiStack extends EmiStack {

  public final EntityType<?> type;
  private final int size;
  private final EntityRenderer renderer;

  public EntityEmiStack(EntityType<?> type, int size) {
    this.type = type;
    this.size = size;
    this.renderer = new EntityRenderer(type, size);
  }

  @Override
  public EmiStack copy() {
    return new EntityEmiStack(type, size);
  }

  @Override
  public void render(PoseStack matrixStack, int x, int y, float delta, int flags) {
    renderer.render(matrixStack, x, y, delta);
  }

  @Override
  public boolean isEmpty() {
    return type == null;
  }

  @Override
  public CompoundTag getNbt() {
    return null;
  }

  @Override
  public Object getKey() {
    return type;
  }

  @Override
  public ResourceLocation getId() {
    return Registry.ENTITY_TYPE.getKey(type);
  }

  @Override
  public List<Component> getTooltipText() {
    List<Component> tooltip = new ArrayList<>();
    tooltip.add(type.getDescription());
    if (Minecraft.getInstance().options.advancedItemTooltips) {
      tooltip.add((new TextComponent(Objects.requireNonNull(Registry.ENTITY_TYPE.getKey(type)).toString())).withStyle(ChatFormatting.DARK_GRAY));
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
    return type.getDescription();
  }
}
