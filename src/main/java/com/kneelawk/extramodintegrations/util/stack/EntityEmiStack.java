package com.kneelawk.extramodintegrations.util.stack;

import com.kneelawk.extramodintegrations.ExMIMod;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityEmiStack extends EmiStack {
    private static final Map<EntityType<?>, Entity> CACHE = new HashMap<>();
    private static final Set<EntityType<?>> BLOCKLIST = new HashSet<>();

    private final EntityType<?> type;

    public EntityEmiStack(EntityType<?> entityType) {
        this.type = entityType;
        Minecraft client = Minecraft.getInstance();
        if (!BLOCKLIST.contains(entityType)) {
            CACHE.computeIfAbsent(entityType, entityType1 -> {
                if (entityType1 == EntityType.PLAYER) return client.player;
                try {
                    return entityType.create(client.level);
                } catch (Throwable t) {
                    BLOCKLIST.add(entityType1);
                    ExMIMod.LOGGER.error("Failed to construct entity", t);
                    return null;
                }
            });
        }
    }

    @Override
    public EmiStack copy() {
        return new EntityEmiStack(type);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        if (((flags & RENDER_ICON) != 0)) {
            Entity entity = CACHE.get(this.type);
            if (entity instanceof LivingEntity living) {
                MouseHandler mouse = Minecraft.getInstance().mouseHandler;
                float mouseX = (float) mouse.xpos() + x;
                float mouseY = (float) mouse.ypos() + y;
                InventoryScreen.renderEntityInInventoryFollowsMouse(draw, x + 8, y + 16, 8, 0, 0, living);
            }
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
        return type;
    }

    @Override
    public ResourceLocation getId() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(type);
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
    public List<Component> getTooltipText() {
        return null;
    }

    @Override
    public Component getName() {
        Entity entity = CACHE.get(this.type);
        if (entity != null) {
            return entity.getName();
        }
        return this.type.getDescription();
    }
}
