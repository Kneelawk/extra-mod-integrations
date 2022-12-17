package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.*;
import com.kneelawk.extramodintegrations.util.LongHolder;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.recipe.recipes.*;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.items.DynamicCellItem;

@SuppressWarnings("unused")
public class TRIntegration extends AbstractTRIntegration {
    public static final EmiStack CELL = EmiStack.of(TRContent.CELL);
    public static final EmiStack ALLOY_SMELTER_STACK = EmiStack.of(TRContent.Machine.ALLOY_SMELTER);
    public static final EmiStack IRON_ALLOY_FURNACE_STACK = EmiStack.of(TRContent.Machine.IRON_ALLOY_FURNACE);
    public static final EmiStack ASSEMBLY_MACHINE_STACK = EmiStack.of(TRContent.Machine.ASSEMBLY_MACHINE);
    public static final EmiStack BLAST_FURNACE_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
    public static final EmiStack INDUSTRIAL_CENTRIFUGE_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_CENTRIFUGE);
    public static final EmiStack CHEMICAL_REACTOR_STACK = EmiStack.of(TRContent.Machine.CHEMICAL_REACTOR);
    public static final EmiStack COMPRESSOR_STACK = EmiStack.of(TRContent.Machine.COMPRESSOR);
    public static final EmiStack DISTILLATION_TOWER_STACK = EmiStack.of(TRContent.Machine.DISTILLATION_TOWER);
    public static final EmiStack EXTRACTOR_STACK = EmiStack.of(TRContent.Machine.EXTRACTOR);
    public static final EmiStack GRINDER_STACK = EmiStack.of(TRContent.Machine.GRINDER);
    public static final EmiStack IMPLOSION_COMPRESSOR_STACK = EmiStack.of(TRContent.Machine.IMPLOSION_COMPRESSOR);
    public static final EmiStack INDUSTRIAL_ELECTROLYZER_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_ELECTROLYZER);
    public static final EmiStack INDUSTRIAL_GRINDER_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_GRINDER);
    public static final EmiStack INDUSTRIAL_SAWMILL_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_SAWMILL);
    public static final EmiStack SCRAP_BOX_STACK = EmiStack.of(TRContent.SCRAP_BOX);
    public static final EmiStack SCRAPBOXINATOR_STACK = EmiStack.of(TRContent.Machine.SCRAPBOXINATOR);
    public static final EmiStack VACUUM_FREEZER_STACK = EmiStack.of(TRContent.Machine.VACUUM_FREEZER);
    public static final EmiStack FLUID_REPLICATOR_STACK = EmiStack.of(TRContent.Machine.FLUID_REPLICATOR);
    public static final EmiStack FUSION_CONTROL_COMPUTER_STACK = EmiStack.of(TRContent.Machine.FUSION_CONTROL_COMPUTER);
    public static final EmiStack ROLLING_MACHINE_STACK = EmiStack.of(TRContent.Machine.ROLLING_MACHINE);
    public static final EmiStack SOLID_CANNING_MACHINE_STACK = EmiStack.of(TRContent.Machine.SOLID_CANNING_MACHINE);
    public static final EmiStack WIRE_MILL_STACK = EmiStack.of(TRContent.Machine.WIRE_MILL);

    public static final EmiStack THERMAL_GENERATOR_STACK = EmiStack.of(TRContent.Machine.THERMAL_GENERATOR);
    public static final EmiStack GAS_TURBINE_STACK = EmiStack.of(TRContent.Machine.GAS_TURBINE);
    public static final EmiStack DIESEL_GENERATOR_STACK = EmiStack.of(TRContent.Machine.DIESEL_GENERATOR);
    public static final EmiStack SEMI_FLUID_GENERATOR_STACK = EmiStack.of(TRContent.Machine.SEMI_FLUID_GENERATOR);
    public static final EmiStack PLASMA_GENERATOR_STACK = EmiStack.of(TRContent.Machine.PLASMA_GENERATOR);

    public static final EmiStack AUTO_CRAFTING_TABLE_STACK = EmiStack.of(TRContent.Machine.AUTO_CRAFTING_TABLE);
    public static final EmiStack IRON_FURNACE_STACK = EmiStack.of(TRContent.Machine.IRON_FURNACE);
    public static final EmiStack ELECTRIC_FURNACE_STACK = EmiStack.of(TRContent.Machine.ELECTRIC_FURNACE);

    public static final EmiRecipeCategory ALLOY_SMELTER_CATEGORY =
        new EmiRecipeCategory(trId("alloy_smelter"), ALLOY_SMELTER_STACK, ExMITextures.ALLOY_SMELTING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory ASSEMBLING_MACHINE_CATEGORY =
        new EmiRecipeCategory(trId("assembling_machine"), ASSEMBLY_MACHINE_STACK, ExMITextures.ASSEMBLING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory BLAST_FURNACE_CATEGORY =
        new EmiRecipeCategory(trId("blast_furnace"), BLAST_FURNACE_STACK, ExMITextures.BLAST_FURNACE,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory CENTRIFUGE_CATEGORY =
        new EmiRecipeCategory(trId("centrifuge"), INDUSTRIAL_CENTRIFUGE_STACK, ExMITextures.CENTRIFUGE,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory CHEMICAL_REACTOR_CATEGORY =
        new EmiRecipeCategory(trId("chemical_reactor"), CHEMICAL_REACTOR_STACK, ExMITextures.CHEMICAL_REACTING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory COMPRESSOR_CATEGORY =
        new EmiRecipeCategory(trId("compressor"), COMPRESSOR_STACK, ExMITextures.COMPRESSING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory DISTILLATION_TOWER_CATEGORY =
        new EmiRecipeCategory(trId("distillation_tower"), DISTILLATION_TOWER_STACK, ExMITextures.DISTILLATION_TOWER,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory EXTRACTOR_CATEGORY =
        new EmiRecipeCategory(trId("extractor"), EXTRACTOR_STACK, ExMITextures.EXTRACTING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory GRINDER_CATEGORY =
        new EmiRecipeCategory(trId("grinder"), GRINDER_STACK, ExMITextures.GRINDING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory IMPLOSION_COMPRESSOR_CATEGORY =
        new EmiRecipeCategory(trId("implosion_compressor"), IMPLOSION_COMPRESSOR_STACK,
            ExMITextures.IMPLOSION_COMPRESSING, EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory INDUSTRIAL_ELECTROLYZER_CATEGORY =
        new EmiRecipeCategory(trId("industrial_electrolyzer"), INDUSTRIAL_ELECTROLYZER_STACK,
            ExMITextures.ELECTROLYZING, EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory INDUSTRIAL_GRINDER_CATEGORY =
        new EmiRecipeCategory(trId("industrial_grinder"), INDUSTRIAL_GRINDER_STACK, ExMITextures.INDUSTRIAL_GRINDING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory INDUSTRIAL_SAWMILL_CATEGORY =
        new EmiRecipeCategory(trId("industrial_sawmill"), INDUSTRIAL_SAWMILL_STACK, ExMITextures.SAWMILLING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory SCRAPBOX_CATEGORY =
        new EmiRecipeCategory(trId("scrapbox"), SCRAP_BOX_STACK, ExMITextures.SCRAPBOX,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory VACUUM_FREEZER_CATEGORY =
        new EmiRecipeCategory(trId("vacuum_freezer"), VACUUM_FREEZER_STACK, ExMITextures.VACUUM_FREEZING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory FLUID_REPLICATOR_CATEGORY =
        new EmiRecipeCategory(trId("fluid_replicator"), FLUID_REPLICATOR_STACK, ExMITextures.FLUID_REPLICATING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory FUSION_REACTOR_CATEGORY =
        new EmiRecipeCategory(trId("fusion_reactor"), FUSION_CONTROL_COMPUTER_STACK, ExMITextures.FUSION_REACTOR,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory ROLLING_MACHINE_CATEGORY =
        new EmiRecipeCategory(trId("rolling_machine"), ROLLING_MACHINE_STACK, ExMITextures.ROLLING_MACHINE,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory SOLID_CANNING_MACHINE_CATEGORY =
        new EmiRecipeCategory(trId("solid_canning_machine"), SOLID_CANNING_MACHINE_STACK, ExMITextures.CANNING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory WIRE_MILL_CATEGORY =
        new EmiRecipeCategory(trId("wire_mill"), WIRE_MILL_STACK, ExMITextures.WIRE_MILLING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory THERMAL_GENERATOR_CATEGORY =
        new EmiRecipeCategory(trId("thermal_generator"), THERMAL_GENERATOR_STACK, THERMAL_GENERATOR_STACK,
            EmiRecipeSorting.compareInputThenOutput());
    public static final EmiRecipeCategory GAS_TURBINE_CATEGORY =
        new EmiRecipeCategory(trId("gas_turbine"), GAS_TURBINE_STACK, GAS_TURBINE_STACK,
            EmiRecipeSorting.compareInputThenOutput());
    public static final EmiRecipeCategory DIESEL_GENERATOR_CATEGORY =
        new EmiRecipeCategory(trId("diesel_generator"), DIESEL_GENERATOR_STACK, DIESEL_GENERATOR_STACK,
            EmiRecipeSorting.compareInputThenOutput());
    public static final EmiRecipeCategory SEMI_FLUID_GENERATOR_CATEGORY =
        new EmiRecipeCategory(trId("semi_fluid_generator"), SEMI_FLUID_GENERATOR_STACK, SEMI_FLUID_GENERATOR_STACK,
            EmiRecipeSorting.compareInputThenOutput());
    public static final EmiRecipeCategory PLASMA_GENERATOR_CATEGORY =
        new EmiRecipeCategory(trId("plasma_generator"), PLASMA_GENERATOR_STACK, PLASMA_GENERATOR_STACK,
            EmiRecipeSorting.compareInputThenOutput());

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading TechReborn Integration...");

        // Alloy Smelting
        registry.addCategory(ALLOY_SMELTER_CATEGORY);
        registry.addWorkstation(ALLOY_SMELTER_CATEGORY, ALLOY_SMELTER_STACK);
        registry.addWorkstation(ALLOY_SMELTER_CATEGORY, IRON_ALLOY_FURNACE_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.ALLOY_SMELTER)) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, ALLOY_SMELTER_CATEGORY, 1));
        }

        // Assembling
        registry.addCategory(ASSEMBLING_MACHINE_CATEGORY);
        registry.addWorkstation(ASSEMBLING_MACHINE_CATEGORY, ASSEMBLY_MACHINE_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.ASSEMBLING_MACHINE)) {
            registry.addRecipe(new AssemblingMachineEmiRecipe(recipe));
        }

        // Blast Furnace
        registry.addCategory(BLAST_FURNACE_CATEGORY);
        registry.addWorkstation(BLAST_FURNACE_CATEGORY, BLAST_FURNACE_STACK);
        for (BlastFurnaceRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.BLAST_FURNACE)) {
            registry.addRecipe(new BlastFurnaceEmiRecipe(recipe));
        }

        // Centrifuge
        registry.addCategory(CENTRIFUGE_CATEGORY);
        registry.addWorkstation(CENTRIFUGE_CATEGORY, INDUSTRIAL_CENTRIFUGE_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.CENTRIFUGE)) {
            registry.addRecipe(new CentrifugeEmiRecipe(recipe));
        }

        // Chemical Reacting
        registry.addCategory(CHEMICAL_REACTOR_CATEGORY);
        registry.addWorkstation(CHEMICAL_REACTOR_CATEGORY, CHEMICAL_REACTOR_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.CHEMICAL_REACTOR)) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, CHEMICAL_REACTOR_CATEGORY, 10));
        }

        // Compressing
        registry.addCategory(COMPRESSOR_CATEGORY);
        registry.addWorkstation(COMPRESSOR_CATEGORY, COMPRESSOR_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.COMPRESSOR)) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, COMPRESSOR_CATEGORY, 1));
        }

        // Distillation Tower
        registry.addCategory(DISTILLATION_TOWER_CATEGORY);
        registry.addWorkstation(DISTILLATION_TOWER_CATEGORY, DISTILLATION_TOWER_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.DISTILLATION_TOWER)) {
            registry.addRecipe(new DistillationTowerEmiRecipe(recipe));
        }

        // Extracting
        registry.addCategory(EXTRACTOR_CATEGORY);
        registry.addWorkstation(EXTRACTOR_CATEGORY, EXTRACTOR_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.EXTRACTOR)) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, EXTRACTOR_CATEGORY, 1));
        }

        // Grinding
        registry.addCategory(GRINDER_CATEGORY);
        registry.addWorkstation(GRINDER_CATEGORY, GRINDER_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.GRINDER)) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, GRINDER_CATEGORY, 1));
        }

        // Implosion Compressor
        registry.addCategory(IMPLOSION_COMPRESSOR_CATEGORY);
        registry.addWorkstation(IMPLOSION_COMPRESSOR_CATEGORY, IMPLOSION_COMPRESSOR_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.IMPLOSION_COMPRESSOR)) {
            registry.addRecipe(new ImplosionCompressorEmiRecipe(recipe));
        }

        // Industrial Electrolyzing
        registry.addCategory(INDUSTRIAL_ELECTROLYZER_CATEGORY);
        registry.addWorkstation(INDUSTRIAL_ELECTROLYZER_CATEGORY, INDUSTRIAL_ELECTROLYZER_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.INDUSTRIAL_ELECTROLYZER)) {
            registry.addRecipe(new IndustrialElectrolyzerEmiRecipe(recipe));
        }

        // Industrial Grinding
        registry.addCategory(INDUSTRIAL_GRINDER_CATEGORY);
        registry.addWorkstation(INDUSTRIAL_GRINDER_CATEGORY, INDUSTRIAL_GRINDER_STACK);
        LongHolder grinderCapacityHolder = new LongHolder(1000 * 81);
        for (IndustrialGrinderRecipe recipe : registry.getRecipeManager()
            .listAllOfType(ModRecipes.INDUSTRIAL_GRINDER)) {
            registry.addRecipe(new IndustrialGrinderEmiRecipe(recipe, grinderCapacityHolder));
        }

        // Industrial Sawmilling
        registry.addCategory(INDUSTRIAL_SAWMILL_CATEGORY);
        registry.addWorkstation(INDUSTRIAL_SAWMILL_CATEGORY, INDUSTRIAL_SAWMILL_STACK);
        LongHolder sawmillCapacityHolder = new LongHolder(1000 * 81);
        for (IndustrialSawmillRecipe recipe : registry.getRecipeManager()
            .listAllOfType(ModRecipes.INDUSTRIAL_SAWMILL)) {
            registry.addRecipe(new IndustrialSawmillEmiRecipe(recipe, sawmillCapacityHolder));
        }

        // Scrapbox
        registry.addCategory(SCRAPBOX_CATEGORY);
        registry.addWorkstation(SCRAPBOX_CATEGORY, SCRAPBOXINATOR_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.SCRAPBOX)) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, SCRAPBOX_CATEGORY, 1));
        }

        // Vacuum Freezing
        registry.addCategory(VACUUM_FREEZER_CATEGORY);
        registry.addWorkstation(VACUUM_FREEZER_CATEGORY, VACUUM_FREEZER_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.VACUUM_FREEZER)) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, VACUUM_FREEZER_CATEGORY, 64));
        }

        // Fluid Replicating
        registry.addCategory(FLUID_REPLICATOR_CATEGORY);
        registry.addWorkstation(FLUID_REPLICATOR_CATEGORY, FLUID_REPLICATOR_STACK);
        LongHolder replicatorCapacityHolder = new LongHolder(1000 * 81);
        for (FluidReplicatorRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.FLUID_REPLICATOR)) {
            registry.addRecipe(new FluidReplicatorEmiRecipe(recipe, replicatorCapacityHolder));
        }

        // Fusion Reactor
        registry.addCategory(FUSION_REACTOR_CATEGORY);
        registry.addWorkstation(FUSION_REACTOR_CATEGORY, FUSION_CONTROL_COMPUTER_STACK);
        for (FusionReactorRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.FUSION_REACTOR)) {
            registry.addRecipe(new FusionReactorEmiRecipe(recipe));
        }

        // Rolling Machine
        registry.addCategory(ROLLING_MACHINE_CATEGORY);
        registry.addWorkstation(ROLLING_MACHINE_CATEGORY, ROLLING_MACHINE_STACK);
        for (RollingMachineRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.ROLLING_MACHINE)) {
            registry.addRecipe(new RollingMachineEmiRecipe(recipe));
        }

        // Solid Canning
        registry.addCategory(SOLID_CANNING_MACHINE_CATEGORY);
        registry.addWorkstation(SOLID_CANNING_MACHINE_CATEGORY, SOLID_CANNING_MACHINE_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.SOLID_CANNING_MACHINE)) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, SOLID_CANNING_MACHINE_CATEGORY, 1));
        }

        // Wire Milling
        registry.addCategory(WIRE_MILL_CATEGORY);
        registry.addWorkstation(WIRE_MILL_CATEGORY, WIRE_MILL_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.WIRE_MILL)) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, WIRE_MILL_CATEGORY, 1));
        }

        // Generators
        registry.addCategory(THERMAL_GENERATOR_CATEGORY);
        registry.addWorkstation(THERMAL_GENERATOR_CATEGORY, THERMAL_GENERATOR_STACK);
        for (FluidGeneratorRecipe recipe : GeneratorRecipeHelper.getFluidRecipesForGenerator(EFluidGenerator.THERMAL)
            .getRecipes()) {
            registry.addRecipe(new FluidGeneratorEmiRecipe(recipe, THERMAL_GENERATOR_CATEGORY,
                generatorRecipeId(THERMAL_GENERATOR_CATEGORY, recipe), 10, 1000000));
        }

        registry.addCategory(GAS_TURBINE_CATEGORY);
        registry.addWorkstation(GAS_TURBINE_CATEGORY, GAS_TURBINE_STACK);
        for (FluidGeneratorRecipe recipe : GeneratorRecipeHelper.getFluidRecipesForGenerator(EFluidGenerator.GAS)
            .getRecipes()) {
            registry.addRecipe(new FluidGeneratorEmiRecipe(recipe, GAS_TURBINE_CATEGORY,
                generatorRecipeId(GAS_TURBINE_CATEGORY, recipe), 10, 1000000));
        }

        registry.addCategory(DIESEL_GENERATOR_CATEGORY);
        registry.addWorkstation(DIESEL_GENERATOR_CATEGORY, DIESEL_GENERATOR_STACK);
        for (FluidGeneratorRecipe recipe : GeneratorRecipeHelper.getFluidRecipesForGenerator(EFluidGenerator.DIESEL)
            .getRecipes()) {
            registry.addRecipe(new FluidGeneratorEmiRecipe(recipe, DIESEL_GENERATOR_CATEGORY,
                generatorRecipeId(DIESEL_GENERATOR_CATEGORY, recipe), 10, 10000));
        }

        registry.addCategory(SEMI_FLUID_GENERATOR_CATEGORY);
        registry.addWorkstation(SEMI_FLUID_GENERATOR_CATEGORY, SEMI_FLUID_GENERATOR_STACK);
        for (FluidGeneratorRecipe recipe : GeneratorRecipeHelper.getFluidRecipesForGenerator(EFluidGenerator.SEMIFLUID)
            .getRecipes()) {
            registry.addRecipe(new FluidGeneratorEmiRecipe(recipe, SEMI_FLUID_GENERATOR_CATEGORY,
                generatorRecipeId(SEMI_FLUID_GENERATOR_CATEGORY, recipe), 10, 1000000));
        }

        registry.addCategory(PLASMA_GENERATOR_CATEGORY);
        registry.addWorkstation(PLASMA_GENERATOR_CATEGORY, PLASMA_GENERATOR_STACK);
        for (FluidGeneratorRecipe recipe : GeneratorRecipeHelper.getFluidRecipesForGenerator(EFluidGenerator.PLASMA)
            .getRecipes()) {
            registry.addRecipe(new FluidGeneratorEmiRecipe(recipe, PLASMA_GENERATOR_CATEGORY,
                generatorRecipeId(PLASMA_GENERATOR_CATEGORY, recipe), 10, 500000000));
        }

        // Add machines that do vanilla things
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, AUTO_CRAFTING_TABLE_STACK);
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, IRON_FURNACE_STACK);
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, ELECTRIC_FURNACE_STACK);

        // Cells should be compared with NBT data
        registry.setDefaultComparison(CELL, comp -> comp.copy().nbt(true).build());

        // Fluid into and from Cells
        Identifier cellId = CELL.getId();
        for (Identifier fluidId : Registries.FLUID.getIds()) {
            Fluid fluid = Registries.FLUID.get(fluidId);

            if (!fluid.isStill(fluid.getDefaultState())) {
                continue;
            }

            EmiStack fluidStack = EmiStack.of(fluid, 1000 * 81);
            EmiStack fluidCellStack = EmiStack.of(DynamicCellItem.getCellWithFluid(fluid));

            Identifier fromId = new Identifier(ExMIPlugin.FLUID_FROM_CONTAINER_CATEGORY.id.getNamespace(),
                ExMIPlugin.FLUID_FROM_CONTAINER_CATEGORY.id.getPath() + "/" + cellId.getNamespace() + "/" +
                    cellId.getNamespace() + "/" + fluidId.getNamespace() + "/" + fluidId.getPath());
            registry.addRecipe(new FluidFromContainerEmiRecipe(fromId, fluidStack, fluidCellStack, CELL));
            Identifier intoId = new Identifier(ExMIPlugin.FLUID_INTO_CONTAINER_CATEGORY.id.getNamespace(),
                ExMIPlugin.FLUID_INTO_CONTAINER_CATEGORY.id.getPath() + "/" + cellId.getNamespace() + "/" +
                    cellId.getNamespace() + "/" + fluidId.getNamespace() + "/" + fluidId.getPath());
            registry.addRecipe(new FluidIntoContainerEmiRecipe(intoId, fluidStack, fluidCellStack, CELL));
        }
    }

    public static Identifier trId(String path) {
        return new Identifier("techreborn", path);
    }

    private static Identifier generatorRecipeId(EmiRecipeCategory category, FluidGeneratorRecipe recipe) {
        Identifier fluidId = Registries.FLUID.getId(recipe.fluid());
        return new Identifier(category.id.getNamespace(),
            category.id.getPath() + "/" + fluidId.getNamespace() + "/" + fluidId.getPath());
    }
}
