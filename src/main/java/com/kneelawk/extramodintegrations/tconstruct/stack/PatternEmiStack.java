package com.kneelawk.extramodintegrations.tconstruct.stack;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        if ((flags & RENDER_ICON) != 0) {
            Sprite sprite = MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).getSprite(pattern.getTexture());
            RenderUtils.setup(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
            draw.drawSprite(x, y, 100, 16, 16, sprite);
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
    public NbtCompound getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return pattern;
    }

    @Override
    public Identifier getId() {
        return pattern;
    }

    @Override
    public List<Text> getTooltipText() {
        return null;
    }

    @Override
    public List<TooltipComponent> getTooltip() {
        List<TooltipComponent> list = new ArrayList<>();
        list.add(TooltipComponent.of(getName().asOrderedText()));

        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            list.add(TooltipComponent.of(Text.literal(getId().toString()).formatted(Formatting.DARK_GRAY).asOrderedText()));
        }
        String namespace = getId().getNamespace();
        String mod = FabricLoader.getInstance()
                .getModContainer(namespace)
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getName)
                .orElse(namespace);
        list.add(TooltipComponent.of(Text.literal(mod).formatted(Formatting.BLUE, Formatting.ITALIC).asOrderedText()));
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public Text getName() {
        return pattern.getDisplayName();
    }
}
