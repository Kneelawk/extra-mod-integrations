package com.kneelawk.extramodintegrations.hephaestus.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.registry.Registries;
import slimeknights.tconstruct.TConstruct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class EntityRenderer implements EmiRenderable {
  /** Entity types that will not render, as they either errored or are the wrong type */
  private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();

  /** Square size of the renderer in pixels */
  private final int size;

  /** Cache of entities for each entity type */
  private final Map<EntityType<?>,Entity> ENTITY_MAP = new HashMap<>();

  private final EntityType<?> type;

  public EntityRenderer(EntityType<?> type, int size) {
    this.type = type;
    this.size = size;
  }

  @Override
  public void render(MatrixStack matrixStack, int x, int y, float delta) {
    if (type != null) {
      World world = MinecraftClient.getInstance().world;
      if (world != null && !IGNORED_ENTITIES.contains(type)) {
        Entity entity;
        // players cannot be created using the type, but we can use the client player
        // side effect is it renders armor/items
        if (type == EntityType.PLAYER) {
          entity = MinecraftClient.getInstance().player;
        } else {
          // entity is created with the client world, but the entity map is thrown away when JEI restarts so they should be okay I think
          entity = ENTITY_MAP.computeIfAbsent(type, t -> t.create(world));
        }
        // only can draw living entities, plus non-living ones don't get recipes anyways
        if (entity instanceof LivingEntity livingEntity) {
          // scale down large mobs, but don't scale up small ones
          int scale = size / 2;
          float height = entity.getHeight();
          float width = entity.getWidth();
          if (height > 2 || width > 2) {
            scale = (int)(size / Math.max(height, width));
          }
          // catch exceptions drawing the entity to be safe, any caught exceptions blacklist the entity
          try {
            MatrixStack modelView = RenderSystem.getModelViewStack();
            modelView.push();
            modelView.multiplyPositionMatrix(matrixStack.peek().getPositionMatrix());
            InventoryScreen.drawEntity(matrixStack, x + size / 4, y + size * 3 / 4, scale, 0, 10, livingEntity);
            modelView.pop();
            RenderSystem.applyModelViewMatrix();
            return;
          } catch (Exception e) {
            TConstruct.LOG.error("Error drawing entity " + Registries.ENTITY_TYPE.getId(type), e);
            IGNORED_ENTITIES.add(type);
            ENTITY_MAP.remove(type);
          }
        } else {
          // not living, so might as well skip next time
          IGNORED_ENTITIES.add(type);
          ENTITY_MAP.remove(type);
        }
      }

      // fallback, draw a pink and black "spawn egg"
//      RenderUtils.setup(EntityMeltingRecipeCategory.BACKGROUND_LOC);
//      int offset = (size - 16) / 2;
//      Screen.blit(matrixStack, offset, offset, 149f, 58f, 16, 16, 256, 256);
    }
  }
}
