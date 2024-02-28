package com.kneelawk.extramodintegrations.util.stack;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityEmiStack extends EmiStack {
    private static final Map<EntityType<? extends LivingEntity>, LivingEntity> CACHE = new HashMap<>();

    private final LivingEntity entity;
    private final EntityType<? extends LivingEntity> type;

    public EntityEmiStack(EntityType<? extends LivingEntity> entityType) {
        this.type = entityType;
        MinecraftClient client = MinecraftClient.getInstance();
        this.entity = CACHE.computeIfAbsent(entityType, entityType1 -> {
            if (entityType1 == EntityType.PLAYER) return client.player;
            return entityType.create(client.world);
        });
    }

    @Override
    public EmiStack copy() {
        return new EntityEmiStack(type);
    }

    @Override
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        if (((flags & RENDER_ICON) != 0)) {
            Mouse mouse = MinecraftClient.getInstance().mouse;
            float mouseX = (float) mouse.getX() + x;
            float mouseY = (float) mouse.getY() + y;
            InventoryScreen.drawEntity(draw, x + 8, y + 16, 8, mouseX, mouseY, entity);
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
        return type;
    }

    @Override
    public Identifier getId() {
        return Registries.ENTITY_TYPE.getId(type);
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
    public List<Text> getTooltipText() {
        return null;
    }

    @Override
    public Text getName() {
        return entity.getName();
    }
}
