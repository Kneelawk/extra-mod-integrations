package com.kneelawk.extramodintegrations.util.stack;

import com.kneelawk.extramodintegrations.ExMIMod;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
        MinecraftClient client = MinecraftClient.getInstance();
        if (!BLOCKLIST.contains(entityType)) {
            CACHE.computeIfAbsent(entityType, entityType1 -> {
                if (entityType1 == EntityType.PLAYER) return client.player;
                try {
                    return entityType.create(client.world);
                } catch (Throwable t) {
                    BLOCKLIST.add(entityType1);
                    ExMIMod.LOGGER.error("Failed to construct entity");
                    t.printStackTrace();
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
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        if (((flags & RENDER_ICON) != 0)) {
            Entity entity = CACHE.get(this.type);
            if (entity instanceof LivingEntity living) {
                Mouse mouse = MinecraftClient.getInstance().mouse;
                float mouseX = (float) mouse.getX() + x;
                float mouseY = (float) mouse.getY() + y;
                InventoryScreen.drawEntity(draw, x + 8, y + 16, 8, 0, 0, living);
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
        Entity entity = CACHE.get(this.type);
        if (entity != null) {
            return entity.getName();
        }
        return this.type.getName();
    }
}
