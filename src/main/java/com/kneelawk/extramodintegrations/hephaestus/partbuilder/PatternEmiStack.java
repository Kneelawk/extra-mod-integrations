package com.kneelawk.extramodintegrations.hephaestus.partbuilder;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import slimeknights.tconstruct.library.client.RenderUtils;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PatternEmiStack extends EmiStack {
  private final Pattern pattern;

  public PatternEmiStack(Pattern pattern) {
    this.pattern = pattern;
  }

  @Override
  public EmiStack copy() {
    return new PatternEmiStack(pattern);
  }

  @Override
  public void render(PoseStack matrices, int x, int y, float delta, int flags) {
    if (pattern != null) {
      TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(pattern.getTexture());
      RenderUtils.setup(InventoryMenu.BLOCK_ATLAS);
      Screen.blit(matrices, x, y, 100, 16, 16, sprite);
    }
  }

  @Override
  public boolean isEmpty() {
    return pattern == null;
  }

  @Override
  public CompoundTag getNbt() {
    return null;
  }

  @Override
  public Object getKey() {
    return pattern.hashCode();
  }

  public Pattern getPattern() {
    return pattern;
  }

  @Override
  public ResourceLocation getId() {
    return new ResourceLocation(pattern.getNamespace(), pattern.getPath());
  }

  @Override
  public List<Component> getTooltipText() {
    if (Minecraft.getInstance().options.advancedItemTooltips) {
      return Arrays.asList(pattern.getDisplayName(), new TextComponent(pattern.toString()).withStyle(ChatFormatting.DARK_GRAY));
    } else {
      return Collections.singletonList(pattern.getDisplayName());
    }
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
    return pattern.getDisplayName();
  }
}
