package com.kneelawk.exmi.pneumaticcraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.kneelawk.exmi.pneumaticcraft.transfer.ProgrammerRecipeHandler;

import dev.emi.emi.api.EmiDragDropHandler;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.crafting.recipe.AmadronRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.AssemblyRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.ExplosionCraftingRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.FluidMixerRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.HeatFrameCoolingRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.HeatPropertiesRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.PressureChamberRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.RefineryRecipe;
import me.desht.pneumaticcraft.api.crafting.recipe.ThermoPlantRecipe;
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags;
import me.desht.pneumaticcraft.api.item.ISpawnerCoreStats;
import me.desht.pneumaticcraft.api.tileentity.IAirHandler;
import me.desht.pneumaticcraft.client.gui.AbstractPneumaticCraftContainerScreen;
import me.desht.pneumaticcraft.client.gui.AmadronAddTradeScreen;
import me.desht.pneumaticcraft.client.gui.InventorySearcherScreen;
import me.desht.pneumaticcraft.client.gui.ItemSearcherScreen;
import me.desht.pneumaticcraft.client.gui.programmer.ProgWidgetItemFilterScreen;
import me.desht.pneumaticcraft.client.gui.semiblock.AbstractLogisticsScreen;
import me.desht.pneumaticcraft.client.util.PointXY;
import me.desht.pneumaticcraft.common.block.entity.processing.UVLightBoxBlockEntity;
import me.desht.pneumaticcraft.common.config.ConfigHelper;
import me.desht.pneumaticcraft.common.entity.semiblock.AbstractLogisticsFrameEntity;
import me.desht.pneumaticcraft.common.inventory.slot.PhantomSlot;
import me.desht.pneumaticcraft.common.item.EmptyPCBItem;
import me.desht.pneumaticcraft.common.item.ICustomTooltipName;
import me.desht.pneumaticcraft.common.item.PressurizableItem;
import me.desht.pneumaticcraft.common.recipes.machine.UVLightBoxRecipe;
import me.desht.pneumaticcraft.common.registry.ModBlocks;
import me.desht.pneumaticcraft.common.registry.ModFluids;
import me.desht.pneumaticcraft.common.registry.ModItems;
import me.desht.pneumaticcraft.common.registry.ModMenuTypes;
import me.desht.pneumaticcraft.common.registry.ModRecipeTypes;
import me.desht.pneumaticcraft.common.upgrades.ModUpgrades;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;

import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.pneumaticcraft.recipe.AmadronEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.AssemblyEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.BlockHeatPropertiesEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.ElectroStaticGridEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.EtchingTankEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.ExplosionEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.FluidMixerEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.HeatFrameCoolingEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.MemoryEssenceEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.PlasticSolidifyingEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.PressureChamberEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.RefineryEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.SpawnerExtractionEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.ThermoPlantEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.UVLightBoxEmiRecipe;
import com.kneelawk.exmi.pneumaticcraft.recipe.YeastCraftingEmiRecipe;

public class PIntegration implements ExMIPlugin {
    @Override
    public void register(EmiRegistry registry) {
        Minecraft client = Minecraft.getInstance();
        Level level = client.level;
        RecipeManager manager = registry.getRecipeManager();

        registry.addCategory(PCategories.AMADRON_TRADE);
        registry.addCategory(PCategories.ASSEMBLY);
        registry.addCategory(PCategories.ELECTRO_GRID);
        registry.addCategory(PCategories.ETCHING_TANK);
        registry.addCategory(PCategories.EXPLOSION_CRAFTING);
        registry.addCategory(PCategories.FLUID_MIXER);
        registry.addCategory(PCategories.HEAT_FRAME_COOLING);
        registry.addCategory(PCategories.HEAT_PROPERTIES);
        registry.addCategory(PCategories.MEMORY_ESSENCE);
        registry.addCategory(PCategories.PLASTIC_SOLIDIFYING);
        registry.addCategory(PCategories.PRESSURE_CHAMBER);
        registry.addCategory(PCategories.REFINERY);
        registry.addCategory(PCategories.SPAWNER_EXTRACTION);
        registry.addCategory(PCategories.THERMO_PLANT);
        registry.addCategory(PCategories.UV_LIGHT_BOX);
        registry.addCategory(PCategories.YEAST_CRAFTING);

        registry.addWorkstation(PCategories.AMADRON_TRADE, EmiStack.of(ModItems.AMADRON_TABLET.get()));
        registry.addWorkstation(PCategories.ASSEMBLY, EmiStack.of(ModBlocks.ASSEMBLY_CONTROLLER.get()));
        registry.addWorkstation(PCategories.PRESSURE_CHAMBER, EmiStack.of(ModBlocks.PRESSURE_CHAMBER_WALL.get()));
        registry.addWorkstation(PCategories.PRESSURE_CHAMBER, EmiStack.of(ModBlocks.PRESSURE_CHAMBER_VALVE.get()));
        registry.addWorkstation(PCategories.PRESSURE_CHAMBER, EmiStack.of(ModBlocks.PRESSURE_CHAMBER_INTERFACE.get()));
        registry.addWorkstation(PCategories.PRESSURE_CHAMBER, EmiStack.of(ModBlocks.PRESSURE_CHAMBER_GLASS.get()));
        registry.addWorkstation(PCategories.REFINERY, EmiStack.of(ModBlocks.REFINERY.get()));
        registry.addWorkstation(PCategories.REFINERY, EmiStack.of(ModBlocks.REFINERY_OUTPUT.get()));
        registry.addWorkstation(PCategories.THERMO_PLANT, EmiStack.of(ModBlocks.THERMOPNEUMATIC_PROCESSING_PLANT.get()));
        registry.addWorkstation(PCategories.UV_LIGHT_BOX, EmiStack.of(ModBlocks.UV_LIGHT_BOX.get()));
        registry.addWorkstation(PCategories.HEAT_FRAME_COOLING, EmiStack.of(ModItems.HEAT_FRAME.get()));
        registry.addWorkstation(PCategories.ETCHING_TANK, EmiStack.of(ModBlocks.ETCHING_TANK.get()));
        registry.addWorkstation(PCategories.FLUID_MIXER, EmiStack.of(ModBlocks.FLUID_MIXER.get()));
        registry.addWorkstation(PCategories.SPAWNER_EXTRACTION, EmiStack.of(ModBlocks.SPAWNER_EXTRACTOR.get()));
        registry.addWorkstation(PCategories.HEAT_PROPERTIES, EmiStack.of(ModBlocks.HEAT_PIPE.get()));
        registry.addWorkstation(PCategories.MEMORY_ESSENCE, EmiStack.of(ModItems.MEMORY_ESSENCE_BUCKET.get()));
        registry.addWorkstation(PCategories.EXPLOSION_CRAFTING, EmiStack.of(Blocks.TNT));
        registry.addWorkstation(PCategories.ELECTRO_GRID, EmiStack.of(ModBlocks.ELECTROSTATIC_COMPRESSOR));

        for (RecipeHolder<ExplosionCraftingRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.EXPLOSION_CRAFTING.get())) {
            registry.addRecipe(new ExplosionEmiRecipe(holder));
        }

        for (RecipeHolder<FluidMixerRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.FLUID_MIXER.get())) {
            registry.addRecipe(new FluidMixerEmiRecipe(holder));
        }

        for (RecipeHolder<AmadronRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.AMADRON.get())) {
            registry.addRecipe(new AmadronEmiRecipe(holder));
        }

        for (RecipeHolder<AssemblyRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.ASSEMBLY_DRILL.get())) {
            registry.addRecipe(new AssemblyEmiRecipe(holder));
        }

        for (RecipeHolder<AssemblyRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.ASSEMBLY_LASER.get())) {
            registry.addRecipe(new AssemblyEmiRecipe(holder));
        }

        for (RecipeHolder<AssemblyRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.ASSEMBLY_DRILL_LASER.get())) {
            registry.addRecipe(new AssemblyEmiRecipe(holder));
        }

        for (RecipeHolder<HeatFrameCoolingRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.HEAT_FRAME_COOLING.get())) {
            registry.addRecipe(new HeatFrameCoolingEmiRecipe(holder));
        }

        for (RecipeHolder<HeatPropertiesRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.BLOCK_HEAT_PROPERTIES.get())) {
            registry.addRecipe(new BlockHeatPropertiesEmiRecipe(holder));
        }

        for (RecipeHolder<PressureChamberRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.PRESSURE_CHAMBER.get())) {
            registry.addRecipe(new PressureChamberEmiRecipe(holder));
        }

        for (RecipeHolder<RefineryRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.REFINERY.get())) {
            registry.addRecipe(new RefineryEmiRecipe(holder));
        }

        for (RecipeHolder<ThermoPlantRecipe> holder : manager.getAllRecipesFor(ModRecipeTypes.THERMO_PLANT.get())) {
            registry.addRecipe(new ThermoPlantEmiRecipe(holder));
        }

        registry.addRecipe(
            new MemoryEssenceEmiRecipe(pncLoc("/memory_essence/memory_stick"), EmiStack.of(ModItems.MEMORY_STICK.get()),
                EmiStack.EMPTY));
        registry.addRecipe(new MemoryEssenceEmiRecipe(pncLoc("/memory_essence/aerial_interface/dispenser_upgrade"),
            EmiStack.of(ModBlocks.AERIAL_INTERFACE.get()), EmiStack.of(ModUpgrades.DISPENSER.get().getItem())));
        registry.addRecipe(new MemoryEssenceEmiRecipe(pncLoc("/memory_essence/drone/programming_puzzle"),
            EmiStack.of(ModItems.DRONE.get()), EmiStack.of(ModItems.PROGRAMMING_PUZZLE.get())));

        for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            Item item = entry.getValue();
            if (!(item instanceof EmptyPCBItem)) continue;
            ResourceLocation id = entry.getKey().location();
            Ingredient input = Ingredient.of(item);
            ItemStack output = item.getDefaultInstance();
            UVLightBoxBlockEntity.setExposureProgress(output, 100);
            ResourceLocation recipeId = pncLoc("/uv_light_box/" + id.toString().replace(":", "/"));
            registry.addRecipe(new UVLightBoxEmiRecipe(recipeId, new UVLightBoxRecipe(input, output)));
        }

        if (ConfigHelper.common().recipes.inWorldPlasticSolidification.get()) {
            registry.addRecipe(new PlasticSolidifyingEmiRecipe(
                pncLoc("/plastic_solidifying/fluid"),
                EmiStack.of(ModFluids.PLASTIC.get(), FluidType.BUCKET_VOLUME),
                EmiStack.of(ModItems.PLASTIC.get())
            ));
            registry.addRecipe(new PlasticSolidifyingEmiRecipe(
                pncLoc("/plastic_solidifying/bucket"),
                EmiStack.of(ModItems.PLASTIC_BUCKET.get()).setRemainder(EmiStack.of(Items.BUCKET)),
                EmiStack.of(ModItems.PLASTIC.get())
            ));
        }

        BuiltInRegistries.BLOCK.getTag(PneumaticCraftTags.Blocks.ELECTROSTATIC_GRID).ifPresent(holderSet -> {
            for (Holder<Block> holder : holderSet) {
                Block block = holder.value();
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
                ResourceLocation recipeId = pncLoc("/electrostatic_grid/" + blockId.toString().replace(":", "/"));
                registry.addRecipe(new ElectroStaticGridEmiRecipe(recipeId, block));
                
            }
        });

        for (Item item : BuiltInRegistries.ITEM) {
            if (!(item instanceof EmptyPCBItem emptyPCBItem)) continue;
            registry.addRecipe(new EtchingTankEmiRecipe(
                pncLoc("/etching_tank/" + BuiltInRegistries.ITEM.getKey(item).toString().replace(":", "/")),
                item.getDefaultInstance(),
                emptyPCBItem.getSuccessItem(),
                emptyPCBItem.getFailedItem(),
                EmptyPCBItem.getEtchingFluid()
            ));
        }

        List<EmiStack> cores = new ArrayList<>();
        for (EntityType<?> type : new EntityType<?>[]{
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER
        }) {
            ItemStack core = new ItemStack(ModItems.SPAWNER_CORE.get());
            ISpawnerCoreStats stats = PneumaticRegistry.getInstance().getItemRegistry().getSpawnerCoreStats(core);
            stats.addAmount(type, 100).save(core);
            cores.add(EmiStack.of(core));
        }
        registry.addRecipe(new SpawnerExtractionEmiRecipe(
            pncLoc("/spawner_extraction"),
            EmiStack.of(ModBlocks.SPAWNER_EXTRACTOR.get()),
            EmiIngredient.of(cores),
            EmiStack.of(ModBlocks.EMPTY_SPAWNER.get())
        ));

        if (ConfigHelper.common().recipes.inWorldYeastCrafting.get()) {
            registry.addRecipe(new YeastCraftingEmiRecipe(
                pncLoc("/yeast_crafting"),
                EmiStack.of(Items.SUGAR),
                EmiStack.of(ModFluids.YEAST_CULTURE.get(), 1000),
                EmiStack.of(ModFluids.YEAST_CULTURE.get(), 1000)
            ));
        }
        
        Map<String, List<EmiIngredient>> infos = new LinkedHashMap<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = item.getDefaultInstance();
            String k = ICustomTooltipName.getTranslationKey(stack, false);
            if (!I18n.exists(k)) continue;
            infos.computeIfAbsent(k, s -> new ArrayList<>()).add(EmiStack.of(item));
        }
        for (Map.Entry<String, List<EmiIngredient>> entry : infos.entrySet()) {
            registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiIngredient.of(entry.getValue())),
                List.of(Component.translatable(entry.getKey())),
                pncLoc("/info/" + entry.getKey())
            ));
        }
        
        registry.addGenericExclusionArea((screen, consumer) -> {
            if (screen instanceof AbstractPneumaticCraftContainerScreen<?, ?> containerScreen) {
                for (Rect2i r : containerScreen.getTabRectangles()) {
                    consumer.accept(new Bounds(r.getX(), r.getY(), r.getWidth(), r.getHeight()));
                }
            }
        });
        
        Comparison pressureComparison = Comparison.compareData(s -> PNCCapabilities.getAirHandler(s.getItemStack()).map(IAirHandler::getPressure).orElse(null));
        Comparison exposureComparison = Comparison.compareData(s -> UVLightBoxBlockEntity.getExposureProgress(s.getItemStack()));
        for (DeferredHolder<Item, ? extends Item> holder : ModItems.ITEMS.getEntries()) {
            Item item = holder.get();
            if (!(item instanceof PressurizableItem)) continue;
            registry.setDefaultComparison(EmiStack.of(item), pressureComparison);
        }
        registry.setDefaultComparison(EmiStack.of(ModItems.EMPTY_PCB.get()), exposureComparison);

        registry.addDragDropHandler(AmadronAddTradeScreen.class, new EmiDragDropHandler.SlotBased<>(
            (screen, slot) -> slot instanceof PhantomSlot phantomSlot && phantomSlot.canAdjust(),
            (screen, slot, ingredient) -> {
                EmiStack emiStack = ingredient.getEmiStacks().getFirst();
                if (emiStack.getKey() instanceof Fluid fluid) {
                    screen.setFluid(slot.index, fluid);
                } else {
                    ItemStack stack = emiStack.getItemStack();
                    screen.setStack(slot.index, stack);
                }
            }
        ));
        registry.addGenericDragDropHandler(new EmiDragDropHandler.BoundsBased<>((screen, bc) -> {
            if (!(screen instanceof AbstractLogisticsScreen<?> gui)) return;
            
            for (Slot slot : gui.getMenu().slots) {
                if (slot instanceof PhantomSlot phantomSlot && phantomSlot.canAdjust()) {
                    bc.accept(
                        new Bounds(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16),
                        ingredient -> gui.updateItemFilter(phantomSlot.getSlotIndex(), ingredient.getEmiStacks().getFirst().getItemStack())
                    );
                }
            }

            for (int i = 0; i < AbstractLogisticsFrameEntity.FLUID_FILTER_SLOTS; i++) {
                PointXY p = gui.getFluidSlotPos(i);
                final int slotNumber = i;
                bc.accept(new Bounds(p.x(), p.y(), 16, 16), ingredient -> {
                    EmiStack emiStack = ingredient.getEmiStacks().getFirst();
                    if (emiStack.getKey() instanceof Fluid fluid) {
                        int amount = emiStack.getAmount() == 0 ? FluidType.BUCKET_VOLUME : (int) emiStack.getAmount();
                        FluidStack fluidStack = new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(fluid), amount, emiStack.getComponentChanges());
                        gui.updateFluidFilter(slotNumber, fluidStack);
                    } else {
                        FluidUtil.getFluidContained(emiStack.getItemStack()).ifPresent(fluidStack -> 
                            gui.updateFluidFilter(slotNumber, fluidStack));
                    }
                });
            }
        }));
        registry.addDragDropHandler(ProgWidgetItemFilterScreen.class, new EmiDragDropHandler.BoundsBased<>((gui, bc) -> {
            bc.accept(
                new Bounds(gui.guiLeft + gui.itemX + 1, gui.guiTop + 52, 16, 16),
                ingredient -> gui.setFilterStack(ingredient.getEmiStacks().getFirst().getItemStack())
            );
        }));
        registry.addDragDropHandler(InventorySearcherScreen.class, new EmiDragDropHandler.SlotBased<>(
            // InventorySearcherScreen.SEARCH_SLOT = 36
            gui -> Collections.singleton(gui.getMenu().getSlot(36)),
            (gui, slot, ingredient) -> 
                gui.setSearchStack(ingredient.getEmiStacks().getFirst().getItemStack()))
        );
        registry.addDragDropHandler(ItemSearcherScreen.class, new EmiDragDropHandler.SlotBased<>(
            // ItemSearcherScreen.SEARCH_SLOT = 48
            gui -> Collections.singleton(gui.getMenu().getSlot(48)),
            (gui, slot, ingredient) -> 
                gui.setSearchStack(ingredient.getEmiStacks().getFirst().getItemStack())
        ));
        
        registry.addRecipeHandler(ModMenuTypes.PROGRAMMER.get(), new ProgrammerRecipeHandler());
    }

    public static ResourceLocation pncLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("pneumaticcraft", path);
    }
}
