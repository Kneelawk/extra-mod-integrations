package com.kneelawk.extramodintegrations.tconstruct.recipe.modifiers;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.stack.ModifierEmiStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.DrawableWidget;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.mantle.client.model.NBTKeyModel;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.crafting.Recipe;

public class ModifierEmiRecipe extends BasicEmiRecipe {
    protected static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");
    private static final List<Component> TEXT_FREE = Collections.singletonList(TConstruct.makeTranslation("jei", "modifiers.free"));
    private static final List<Component> TEXT_INCREMENTAL = Collections.singletonList(TConstruct.makeTranslation("jei", "modifiers.incremental"));
    private static final String KEY_SLOT = TConstruct.makeTranslationKey("jei", "modifiers.slot");
    private static final String KEY_SLOTS = TConstruct.makeTranslationKey("jei", "modifiers.slots");
    private static final String KEY_MAX = TConstruct.makeTranslationKey("jei", "modifiers.max");

    private final boolean hasRequirements, isIncremental;
    private final int maxLevel;
    private final SlotType.SlotCount slots;
    private final String requirementsError;
    private final EmiIngredient toolWithoutModifier, toolWithModifier;
    private final Map<SlotType, TextureAtlasSprite> slotTypeSprites = new HashMap<>();

    public ModifierEmiRecipe(IDisplayModifierRecipe recipe) {
        super(TiCCategories.MODIFIERS, recipe instanceof Recipe<?> r ? r.getId() : null, 128, 77);

        this.inputs = List.of(
                EmiIngredient.of(recipe.getDisplayItems(0).stream().map(EmiStack::of).toList()),
                EmiIngredient.of(recipe.getDisplayItems(1).stream().map(EmiStack::of).toList()),
                EmiIngredient.of(recipe.getDisplayItems(2).stream().map(EmiStack::of).toList()),
                EmiIngredient.of(recipe.getDisplayItems(3).stream().map(EmiStack::of).toList()),
                EmiIngredient.of(recipe.getDisplayItems(4).stream().map(EmiStack::of).toList())
        );
        this.outputs = List.of(ModifierEmiStack.of(recipe.getDisplayResult()));
        this.hasRequirements = recipe.hasRequirements();
        this.isIncremental = recipe.isIncremental();
        this.maxLevel = recipe.getMaxLevel();
        this.slots = recipe.getSlots();
        this.requirementsError = recipe.getRequirementsError();
        this.toolWithoutModifier = EmiIngredient.of(recipe.getToolWithoutModifier().stream().map(EmiStack::of).toList());
        this.toolWithModifier = EmiIngredient.of(recipe.getToolWithModifier().stream().map(EmiStack::of).toList());
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return Stream.concat(
                Stream.of(toolWithoutModifier),
                inputs.stream()
        ).toList();
    }

    private DrawableWidget drawSlotType(WidgetHolder widgets, int x, int y) {
        SlotType slotType = slots == null ? null : slots.getType();

        Minecraft minecraft = Minecraft.getInstance();
        TextureAtlasSprite sprite = slotTypeSprites.computeIfAbsent(slotType, s -> {
            ModelManager modelManager = minecraft.getModelManager();
            // gets the model for the item, its a sepcial one that gives us texture info
            BakedModel model = minecraft.getItemRenderer().getItemModelShaper().getItemModel(TinkerModifiers.creativeSlotItem.get());
            if (model != null && model.getOverrides() instanceof NBTKeyModel.Overrides) {
                Material material = ((NBTKeyModel.Overrides) model.getOverrides()).getTexture(s == null ? "slotless" : s.getName());
                return modelManager.getAtlas(material.atlasLocation()).getSprite(material.texture());
            } else {
                // failed to use the model, use missing texture
                return modelManager.getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(MissingTextureAtlasSprite.getLocation());
            }
        });

        return widgets.addDrawable(x, y, 16, 16, (graphics, mouseX, mouseY, delta) -> {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

            graphics.blit(0, 0, 0, 16, 16, sprite);
        });
    }

    private void drawSlot(WidgetHolder widgets, List<EmiIngredient> stacks, int slot, int x, int y) {
        if (stacks.get(slot).isEmpty()) {
            widgets.addTexture(BACKGROUND_LOC, x + 1, y + 1, 16, 16, 128 + slot * 16, 0);
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 128, 77, 0, 0);

        if (maxLevel > 0) {
            widgets.addText(Component.translatable(KEY_MAX).append(String.valueOf(maxLevel)), 66, 16, 0x808080, false);
        }

        List<ClientTooltipComponent> slotTypeTooltip;
        if (slots != null) {
            int count = slots.getCount();
            if (count == 1) {
                slotTypeTooltip = List.of(ClientTooltipComponent.create(Component.translatable(KEY_SLOT, slots.getType().getDisplayName()).getVisualOrderText()));
            } else {
                slotTypeTooltip = List.of(ClientTooltipComponent.create(Component.translatable(KEY_SLOTS, slots.getType().getDisplayName()).getVisualOrderText()));
            }
        } else {
            slotTypeTooltip = TEXT_FREE.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList();
        }

        drawSlotType(widgets, 110, 58)
                .tooltip(slotTypeTooltip);
        if (slots != null) {
            widgets.addText(Component.literal(String.valueOf(slots.getCount())), 111, 63, 0x808080, false)
                    .horizontalAlign(TextWidget.Alignment.END);
        }

        widgets.addSlot(inputs.get(0), 2, 32).drawBack(false);
        widgets.addSlot(inputs.get(1), 24, 14).drawBack(false);
        widgets.addSlot(inputs.get(2), 46, 32).drawBack(false);
        widgets.addSlot(inputs.get(3), 42, 57).drawBack(false);
        widgets.addSlot(inputs.get(4), 6, 57).drawBack(false);

        widgets.addSlot(toolWithoutModifier, 24, 37)
                .drawBack(false);
        widgets.addSlot(toolWithModifier, 100, 29)
                .drawBack(false)
                .large(true)
                .recipeContext(this);

        widgets.addText(outputs.get(0).getName(), 64, 2, -1, true)
                .horizontalAlign(TextWidget.Alignment.CENTER);
        widgets.addSlot(outputs.get(0), 104, 10)
                .drawBack(false)
                .recipeContext(this);

        drawSlot(widgets, inputs, 0, 2, 32);
        drawSlot(widgets, inputs, 1, 24, 14);
        drawSlot(widgets, inputs, 2, 46, 32);
        drawSlot(widgets, inputs, 3, 42, 57);
        drawSlot(widgets, inputs, 4, 6, 57);

        if (hasRequirements) {
            widgets.addTexture(BACKGROUND_LOC, 66, 58, 16, 16, 128, 17)
                    .tooltipText(List.of(Component.translatable(requirementsError)));
        }
        if (isIncremental) {
            widgets.addTexture(BACKGROUND_LOC, 83, 59, 16, 16, 128, 33)
                    .tooltipText(TEXT_INCREMENTAL);
        }
    }
}
