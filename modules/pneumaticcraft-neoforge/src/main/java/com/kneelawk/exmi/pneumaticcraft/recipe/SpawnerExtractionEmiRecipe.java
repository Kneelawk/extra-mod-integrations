package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;
import java.util.Random;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.ISpawnerCoreStats;
import me.desht.pneumaticcraft.common.registry.ModBlocks;
import me.desht.pneumaticcraft.common.registry.ModItems;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.Blocks;

import com.kneelawk.exmi.pneumaticcraft.PCategories;
import com.kneelawk.exmi.pneumaticcraft.PIntegration;

public class SpawnerExtractionEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_SPAWNER_EXTRACTION, 0, 0, 120, 64);
    // vanilla naturally generating spawners
    private static final EntityType<?>[] ENTITY_TYPES = new EntityType<?>[] {
        EntityType.BLAZE,
        EntityType.CAVE_SPIDER,
        EntityType.MAGMA_CUBE,
        EntityType.SILVERFISH,
        EntityType.SKELETON,
        EntityType.SPIDER,
        EntityType.ZOMBIE
    };
    
    private final EmiIngredient spawnerExtractor = EmiStack.of(ModBlocks.SPAWNER_EXTRACTOR.get());
    private final EmiStack emptySpawner = EmiStack.of(ModBlocks.EMPTY_SPAWNER.get());
    private final int uniq = new Random().nextInt();

    public SpawnerExtractionEmiRecipe() {
        super(PIntegration.pncLoc("/spawner_extraction"));
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.SPAWNER_EXTRACTION;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiStack.of(Blocks.SPAWNER));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(ModItems.SPAWNER_CORE.get()), emptySpawner);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addSlot(spawnerExtractor, 52, 2)
            .drawBack(false);
        widgets.addGeneratedSlot(r -> getSlot(r, 0), uniq, 52, 33)
            .drawBack(false);
        widgets.addGeneratedSlot(r -> getSlot(r, 1), uniq, 17,33)
            .drawBack(false)
            .recipeContext(this);
        widgets.addSlot(emptySpawner, 87, 33)
            .drawBack(false)
            .recipeContext(this);
    }
    
    private EmiIngredient getSlot(Random random, int idx) {
        EntityType<?> entityType = ENTITY_TYPES[random.nextInt(ENTITY_TYPES.length)];
        
        ItemStack spawner = Items.SPAWNER.getDefaultInstance();
        // todo find a better way of doing this
        CompoundTag entityTag = new CompoundTag();
        entityTag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
        CompoundTag spawnDataTag = new CompoundTag();
        spawnDataTag.put("entity", entityTag);
        CompoundTag tag = new CompoundTag();
        tag.put(BaseSpawner.SPAWN_DATA_TAG, spawnDataTag);
        spawner.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
        
        ItemStack core = ModItems.SPAWNER_CORE.toStack();
        ISpawnerCoreStats stats = PneumaticRegistry.getInstance().getItemRegistry().getSpawnerCoreStats(core);
        stats.addAmount(entityType, 100).save(core);

        return new EmiIngredient[] {
            EmiStack.of(spawner),
            EmiStack.of(core)
        }[idx];
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
