package com.kneelawk.extramodintegrations.hephaestus.modifiers;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.Recipe;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.model.NBTKeyModel;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.GuiUtil;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

public class ModifierEmiRecipe implements EmiRecipe {
  protected static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");
  private final List<EmiIngredient> input = new ArrayList<>();
  private final List<EmiIngredient> tools;
  private final ModifierEntry entry;
  private final Identifier id;
  private final Map<SlotType,Sprite> slotTypeSprites = new HashMap<>();
  private final boolean hasRequirements;
  private final boolean isIncremental;
  private final int maxLevel;
  private final SlotType.SlotCount slots;
  private final String requirementsError;

  public ModifierEmiRecipe(IDisplayModifierRecipe recipe) {
    // TODO: this is a bad id. get a better id from somewhere or make one up
//    id = recipe.getModifier().getId();
    if (recipe instanceof Recipe<?> r) {
      id = r.getId();
    } else id = null;

    tools = Stream.of(recipe.getToolWithoutModifier(), recipe.getToolWithModifier()).map(t -> t.stream().map(EmiStack::of).toList()).map(EmiIngredient::of).toList();
    IntStream.range(0, 5).forEachOrdered(i -> input.add(EmiIngredient.of(recipe.getDisplayItems(i).stream().map(EmiStack::of).toList())));
    entry = recipe.getDisplayResult();
    requirementsError = recipe.getRequirementsError();
    hasRequirements = recipe.hasRequirements();
    isIncremental = recipe.isIncremental();
    maxLevel = recipe.getMaxLevel();
    slots = recipe.getSlots();
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return HephaestusIntegration.MODIFIER_CATEGORY;
  }

  @Override
  public @Nullable Identifier getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return input;
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(new ModifierEmiStack(entry));
  }

  @Override
  public List<EmiIngredient> getCatalysts() {
    return tools;
  }

  @Override
  public int getDisplayWidth() {
    return 128;
  }

  @Override
  public int getDisplayHeight() {
    return 77;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(BACKGROUND_LOC, 0, 0, 128, 77, 0, 0);

    // slot outlines
    drawOutline(widgets, 0,  2, 32);
    drawOutline(widgets, 1, 24, 14);
    drawOutline(widgets, 2, 46, 32);
    drawOutline(widgets, 3, 42, 57);
    drawOutline(widgets, 4,  6, 57);

    // info icons
    if (hasRequirements) {
      widgets.addTexture(BACKGROUND_LOC, 66, 58, 16, 16, 128, 17)
        .tooltip((checkX, checkY) -> {
          if (GuiUtil.isHovered(checkX, checkY, 66, 58, 16, 16))
            return List.of(TooltipComponent.of(Text.translatable(requirementsError).asOrderedText()));
          return null;
        });
    }
    if (isIncremental) {
      widgets.addTexture(BACKGROUND_LOC, 83, 59, 16, 16, 128, 33)
        .tooltip((checkX, checkY) -> {
          if (GuiUtil.isHovered(checkX, checkY, 83, 59, 16, 16))
            return List.of(TooltipComponent.of(Text.translatable("jei.tconstruct.modifiers.incremental").asOrderedText()));
          return null;
        });
    }

    // max count
    if (maxLevel > 0) {
      widgets.addText(Text.translatable("jei.tconstruct.modifiers.max").append(String.valueOf(maxLevel)),
        66, 16, Color.GRAY.getRGB(), false);
    }


    // slot cost
    widgets.add(new SlotCountWidget(110, 58, slots));
    if (slots != null) {
      Text text = Text.literal(Integer.toString(slots.getCount()));
      widgets.addText(text, 111, 63, Color.GRAY.getRGB(), false).horizontalAlign(TextWidget.Alignment.END);
    }

    // inputs
    widgets.addSlot(input.get(0),  2, 32).drawBack(false);
    widgets.addSlot(input.get(1), 24, 14).drawBack(false);
    widgets.addSlot(input.get(2), 46, 32).drawBack(false);
    widgets.addSlot(input.get(3), 42, 57).drawBack(false);
    widgets.addSlot(input.get(4),  6, 57).drawBack(false);
    // modifiers
    widgets.add(new ModifierSlotWidget(entry, 2, 2, 124, 10))
            .recipeContext(this);
    // tool
    widgets.addSlot(tools.get(0),  24, 37).drawBack(false);
    widgets.addSlot(tools.get(1), 100, 29).drawBack(false).output(true);
  }

  private void drawOutline(WidgetHolder widgets, int slot, int x, int y) {
    if (input.get(slot).isEmpty()) {
      widgets.addTexture(BACKGROUND_LOC, x + 1, y + 1, 16, 16, 128 + slot * 16, 0);
    }
  }

  private class SlotCountWidget extends Widget {
    private final int x, y;
    private final Bounds bounds;
    private final SlotType.SlotCount slots;

    public SlotCountWidget(int x, int y, @Nullable SlotType.SlotCount slots) {
      this.x = x;
      this.y = y;
      this.bounds = new Bounds(x, y, 16, 16);
      this.slots = slots;
    }

    @Override
    public Bounds getBounds() {
      return bounds;
    }

    @Override
    public List<TooltipComponent> getTooltip(int mouseX, int mouseY) {
      Text tooltipComponent = null;
      if (slots != null) {
        int count = slots.getCount();
        if (count == 1) {
          tooltipComponent = Text.translatable("jei.tconstruct.modifiers.slot", slots.getType().getDisplayName());
        } else if (count > 1) {
          tooltipComponent = Text.translatable("jei.tconstruct.modifiers.slots", slots, slots.getType().getDisplayName());
        }
      } else {
        tooltipComponent = Text.translatable("jei.tconstruct.modifiers.free");
      }
      if (tooltipComponent != null) {
        return List.of(TooltipComponent.of(tooltipComponent.asOrderedText()));
      }
      return List.of();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
      MinecraftClient minecraft = MinecraftClient.getInstance();
      Sprite sprite;
      SlotType slotType = slots == null ? null : slots.getType();
      if (slotTypeSprites.containsKey(slotType)) {
        sprite = slotTypeSprites.get(slotType);
      } else {
        BakedModelManager modelManager = minecraft.getBakedModelManager();
        // gets the model for the item, its a sepcial one that gives us texture info
        BakedModel model = minecraft.getItemRenderer().getModels().getModel(TinkerModifiers.creativeSlotItem.get());
        if (model != null && model.getOverrides() instanceof NBTKeyModel.Overrides) {
          SpriteIdentifier material = ((NBTKeyModel.Overrides) model.getOverrides()).getTexture(slotType == null ? "slotless" : slotType.getName());
          sprite = modelManager.getAtlas(material.getAtlasId()).getSprite(material.getTextureId());
        } else {
          // failed to use the model, use missing texture
          sprite = modelManager.getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).getSprite(MissingSprite.getMissingSpriteId());
        }
        slotTypeSprites.put(slotType, sprite);
      }
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

      Screen.drawSprite(matrices, x, y, 0, 16, 16, sprite);
    }
  }

}
