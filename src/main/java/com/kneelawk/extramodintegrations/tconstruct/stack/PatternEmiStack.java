package com.kneelawk.extramodintegrations.tconstruct.stack;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import slimeknights.tconstruct.library.client.RenderUtils;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;

import java.util.ArrayList;
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
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        if ((flags & RENDER_ICON) != 0) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(pattern.getTexture());
            RenderUtils.setup(InventoryMenu.BLOCK_ATLAS);
            draw.blit(x, y, 100, 16, 16, sprite);
        }
        if ((flags & RENDER_REMAINDER) != 0) {
            EmiRender.renderRemainderIcon(this, draw, x, y);
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public CompoundTag getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return pattern;
    }

    @Override
    public ResourceLocation getId() {
        return pattern;
    }

    @Override
    public List<Component> getTooltipText() {
        return null;
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = new ArrayList<>();
        list.add(ClientTooltipComponent.create(getName().getVisualOrderText()));

        if (Minecraft.getInstance().options.advancedItemTooltips) {
            list.add(ClientTooltipComponent.create(Component.literal(getId().toString()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText()));
        }
        String namespace = getId().getNamespace();
        String mod = FabricLoader.getInstance()
                .getModContainer(namespace)
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getName)
                .orElse(namespace);
        list.add(ClientTooltipComponent.create(Component.literal(mod).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC).getVisualOrderText()));
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public Component getName() {
        return pattern.getDisplayName();
    }
}
