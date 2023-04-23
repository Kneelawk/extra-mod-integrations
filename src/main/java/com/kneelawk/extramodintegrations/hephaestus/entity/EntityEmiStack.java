package com.kneelawk.extramodintegrations.hephaestus.entity;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.stack.EmiStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.emi.emi.config.EmiConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
  public void render(MatrixStack matrixStack, int x, int y, float delta, int flags) {
    renderer.render(matrixStack, x, y, delta);
  }

  @Override
  public boolean isEmpty() {
    return type == null;
  }

  @Override
  public NbtCompound getNbt() {
    return null;
  }

  @Override
  public Object getKey() {
    return type;
  }

  @Override
  public Identifier getId() {
    return Registries.ENTITY_TYPE.getId(type);
  }

  @Override
  public List<Text> getTooltipText() {
    List<Text> tooltip = new ArrayList<>();

    tooltip.add(type.getName());

    if (MinecraftClient.getInstance().options.advancedItemTooltips) {
      tooltip.add((Text.literal(Objects.requireNonNull(Registries.ENTITY_TYPE.getId(type)).toString())).formatted(Formatting.DARK_GRAY));
    }

    if (EmiConfig.appendModId) {
      String mod = EmiUtil.getModName(Registries.ENTITY_TYPE.getId(type).getNamespace());
      tooltip.add(Text.literal(mod).formatted(Formatting.BLUE, Formatting.ITALIC));
    }

    return tooltip;
  }

  @Override
  public List<TooltipComponent> getTooltip() {
    return getTooltipText().stream()
      .map(Text::asOrderedText)
      .map(TooltipComponent::of)
      .toList();
  }

  @Override
  public Text getName() {
    return type.getName();
  }
}
