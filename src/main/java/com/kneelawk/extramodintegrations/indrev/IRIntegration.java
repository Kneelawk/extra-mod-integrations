package com.kneelawk.extramodintegrations.indrev;

import java.util.Arrays;
import java.util.List;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.gui.screenhandlers.ScreenhandlersKt;
import me.steven.indrev.recipes.machines.CompressorRecipe;
import me.steven.indrev.recipes.machines.CondenserRecipe;
import me.steven.indrev.recipes.machines.ElectrolysisRecipe;
import me.steven.indrev.recipes.machines.FluidInfuserRecipe;
import me.steven.indrev.recipes.machines.InfuserRecipe;
import me.steven.indrev.recipes.machines.LaserRecipe;
import me.steven.indrev.recipes.machines.ModuleRecipe;
import me.steven.indrev.recipes.machines.PulverizerRecipe;
import me.steven.indrev.recipes.machines.RecyclerRecipe;
import me.steven.indrev.recipes.machines.SawmillRecipe;
import me.steven.indrev.recipes.machines.SmelterRecipe;
import me.steven.indrev.registry.IRBlockRegistry;
import me.steven.indrev.registry.MachineRegistry;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.AbstractIRIntegration;
import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;
import com.kneelawk.extramodintegrations.util.LongHolder;

@SuppressWarnings("unused")
public class IRIntegration extends AbstractIRIntegration {
    public static final EmiStack[] COMPRESSOR_STACKS = getAllTiers(MachineRegistry.Companion.getCOMPRESSOR_REGISTRY());
    public static final EmiStack[] COMPRESSOR_FACTORY_STACKS =
        getAllTiers(MachineRegistry.Companion.getCOMPRESSOR_FACTORY_REGISTRY());
    public static final EmiStack[] CONDENSER_STACKS = getAllTiers(MachineRegistry.Companion.getCONDENSER_REGISTRY());
    public static final EmiStack[] ELECTROLYSIS_STACKS =
        getAllTiers(MachineRegistry.Companion.getELECTROLYTIC_SEPARATOR_REGISTRY());
    public static final EmiStack[] FURNACE_STACKS =
        getAllTiers(MachineRegistry.Companion.getELECTRIC_FURNACE_REGISTRY());
    public static final EmiStack[] FURNACE_FACTORY_STACKS =
        getAllTiers(MachineRegistry.Companion.getELECTRIC_FURNACE_FACTORY_REGISTRY());
    public static final EmiStack[] FLUID_INFUSER_STACKS =
        getAllTiers(MachineRegistry.Companion.getFLUID_INFUSER_REGISTRY());
    public static final EmiStack[] SOLID_INFUSER_STACKS =
        getAllTiers(MachineRegistry.Companion.getSOLID_INFUSER_REGISTRY());
    public static final EmiStack[] SOLID_INFUSER_FACTORY_STACKS =
        getAllTiers(MachineRegistry.Companion.getSOLID_INFUSER_FACTORY_REGISTRY());
    public static final EmiStack[] LASER_EMITTER_STACKS =
        getAllTiers(MachineRegistry.Companion.getLASER_EMITTER_REGISTRY());
    public static final EmiStack CAPSULE_STACK = EmiStack.of(IRBlockRegistry.INSTANCE.getCAPSULE_BLOCK());
    public static final EmiStack[] MODULAR_WORKBENCH_STACKS =
        getAllTiers(MachineRegistry.Companion.getMODULAR_WORKBENCH_REGISTRY());
    public static final EmiStack[] PULVERIZER_STACKS = getAllTiers(MachineRegistry.Companion.getPULVERIZER_REGISTRY());
    public static final EmiStack[] PULVERIZER_FACTORY_STACKS =
        getAllTiers(MachineRegistry.Companion.getPULVERIZER_FACTORY_REGISTRY());
    public static final EmiStack[] RECYCLER_STACKS = getAllTiers(MachineRegistry.Companion.getRECYCLER_REGISTRY());
    public static final EmiStack[] SAWMILL_STACKS = getAllTiers(MachineRegistry.Companion.getSAWMILL_REGISTRY());
    public static final EmiStack[] SMELTER_STACKS = getAllTiers(MachineRegistry.Companion.getSMELTER_REGISTRY());

    public static final EmiRecipeCategory COMPRESS_CATEGORY =
        new EmiRecipeCategory(irId("compress"), COMPRESSOR_STACKS[0], ExMITextures.COMPRESSING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory CONDENSER_CATEGORY =
        new EmiRecipeCategory(irId("condenser"), CONDENSER_STACKS[0], ExMITextures.CONDENSING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory ELECTROLYSIS_CATEGORY =
        new EmiRecipeCategory(irId("electrolysis"), ELECTROLYSIS_STACKS[0], ExMITextures.ELECTROLYZING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory FLUID_INFUSE_CATEGORY =
        new EmiRecipeCategory(irId("fluid_infuse"), FLUID_INFUSER_STACKS[0], ExMITextures.MIXING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory INFUSE_CATEGORY =
        new EmiRecipeCategory(irId("infuse"), SOLID_INFUSER_STACKS[0], ExMITextures.CHEMICAL_REACTING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory LASER_CATEGORY =
        new EmiRecipeCategory(irId("laser"), LASER_EMITTER_STACKS[0], ExMITextures.LASERING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory MODULES_CATEGORY =
        new EmiRecipeCategory(irId("modules"), MODULAR_WORKBENCH_STACKS[0], ExMITextures.MODULE,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory PULVERIZE_CATEGORY =
        new EmiRecipeCategory(irId("pulverize"), PULVERIZER_STACKS[0], ExMITextures.GRINDING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory RECYCLE_CATEGORY =
        new EmiRecipeCategory(irId("recycle"), RECYCLER_STACKS[0], ExMITextures.RECYCLING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory SAWMILL_CATEGORY =
        new EmiRecipeCategory(irId("sawmill"), SAWMILL_STACKS[0], ExMITextures.SAWMILLING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory SMELTER_CATEGORY =
        new EmiRecipeCategory(irId("smelter"), SMELTER_STACKS[0], ExMITextures.METAL_SMELTING,
            EmiRecipeSorting.compareOutputThenInput());

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading Industrial Revolution Integration...");
        RecipeManager manager = registry.getRecipeManager();

        // Compressor
        registry.addCategory(COMPRESS_CATEGORY);
        for (EmiStack stack : COMPRESSOR_STACKS) registry.addWorkstation(COMPRESS_CATEGORY, stack);
        for (EmiStack stack : COMPRESSOR_FACTORY_STACKS) registry.addWorkstation(COMPRESS_CATEGORY, stack);
        for (CompressorRecipe recipe : manager.listAllOfType(CompressorRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, COMPRESS_CATEGORY));
        }
        registry.addRecipeHandler(ScreenhandlersKt.getCOMPRESSOR_HANDLER(),
            new SimpleRecipeHandler<>(COMPRESS_CATEGORY, List.of(41), 0));

        // Condenser
        registry.addCategory(CONDENSER_CATEGORY);
        for (EmiStack stack : CONDENSER_STACKS) registry.addWorkstation(CONDENSER_CATEGORY, stack);
        LongHolder maxCondenserVolume = new LongHolder(800 * 81);
        for (CondenserRecipe recipe : manager.listAllOfType(CondenserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new CondenserEmiRecipe(recipe, maxCondenserVolume));
        }

        // Electrolysis
        registry.addCategory(ELECTROLYSIS_CATEGORY);
        for (EmiStack stack : ELECTROLYSIS_STACKS) registry.addWorkstation(ELECTROLYSIS_CATEGORY, stack);
        LongHolder maxElectrolyzerVolume = new LongHolder(800 * 81);
        for (ElectrolysisRecipe recipe : manager.listAllOfType(ElectrolysisRecipe.Companion.getTYPE())) {
            registry.addRecipe(new ElectrolysisEmiRecipe(recipe, maxElectrolyzerVolume));
        }

        // Fluid Infuser
        registry.addCategory(FLUID_INFUSE_CATEGORY);
        for (EmiStack stack : FLUID_INFUSER_STACKS) registry.addWorkstation(FLUID_INFUSE_CATEGORY, stack);
        LongHolder maxFluidInfuserVolume = new LongHolder(800 * 81);
        for (FluidInfuserRecipe recipe : manager.listAllOfType(FluidInfuserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new FluidInfuserEmiRecipe(recipe, maxFluidInfuserVolume));
        }

        // Solid Infuser
        registry.addCategory(INFUSE_CATEGORY);
        for (EmiStack stack : SOLID_INFUSER_STACKS) registry.addWorkstation(INFUSE_CATEGORY, stack);
        for (EmiStack stack : SOLID_INFUSER_FACTORY_STACKS) registry.addWorkstation(INFUSE_CATEGORY, stack);
        for (InfuserRecipe recipe : manager.listAllOfType(InfuserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, INFUSE_CATEGORY));
        }
        registry.addRecipeHandler(ScreenhandlersKt.getSOLID_INFUSER_HANDLER(),
            new SimpleRecipeHandler<>(INFUSE_CATEGORY, List.of(41, 42), 0));

        // Laser
        registry.addCategory(LASER_CATEGORY);
        for (EmiStack stack : LASER_EMITTER_STACKS) registry.addWorkstation(LASER_CATEGORY, stack);
        registry.addWorkstation(LASER_CATEGORY, CAPSULE_STACK);
        for (LaserRecipe recipe : manager.listAllOfType(LaserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new LaserEmiRecipe(recipe));
        }

        // Modular Workbench
        registry.addCategory(MODULES_CATEGORY);
        for (EmiStack stack : MODULAR_WORKBENCH_STACKS) registry.addWorkstation(MODULES_CATEGORY, stack);
        for (ModuleRecipe recipe : manager.listAllOfType(ModuleRecipe.Companion.getTYPE())) {
            registry.addRecipe(new ModuleEmiRecipe(recipe));
        }

        // Pulverizer
        registry.addCategory(PULVERIZE_CATEGORY);
        for (EmiStack stack : PULVERIZER_STACKS) registry.addWorkstation(PULVERIZE_CATEGORY, stack);
        for (EmiStack stack : PULVERIZER_FACTORY_STACKS) registry.addWorkstation(PULVERIZE_CATEGORY, stack);
        for (PulverizerRecipe recipe : manager.listAllOfType(PulverizerRecipe.Companion.getTYPE())) {
            registry.addRecipe(new PulverizerEmiRecipe(recipe));
        }
        registry.addRecipeHandler(ScreenhandlersKt.getPULVERIZER_HANDLER(),
            new SimpleRecipeHandler<>(PULVERIZE_CATEGORY, List.of(41), 0));

        // Recycler
        registry.addCategory(RECYCLE_CATEGORY);
        for (EmiStack stack : RECYCLER_STACKS) registry.addWorkstation(RECYCLE_CATEGORY, stack);
        for (RecyclerRecipe recipe : manager.listAllOfType(RecyclerRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, RECYCLE_CATEGORY));
        }
        registry.addRecipeHandler(ScreenhandlersKt.getRECYCLER_HANDLER(),
            new SimpleRecipeHandler<>(RECYCLE_CATEGORY, List.of(40), 0));

        // Sawmill
        registry.addCategory(SAWMILL_CATEGORY);
        for (EmiStack stack : SAWMILL_STACKS) registry.addWorkstation(SAWMILL_CATEGORY, stack);
        for (SawmillRecipe recipe : manager.listAllOfType(SawmillRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SawmillEmiRecipe(recipe));
        }
        registry.addRecipeHandler(ScreenhandlersKt.getSAWMILL_HANDLER(),
            new SimpleRecipeHandler<>(SAWMILL_CATEGORY, List.of(41), 0));

        // Smelter
        registry.addCategory(SMELTER_CATEGORY);
        for (EmiStack stack : SMELTER_STACKS) registry.addWorkstation(SMELTER_CATEGORY, stack);
        LongHolder maxSmelterVolume = new LongHolder(800 * 81);
        for (SmelterRecipe recipe : manager.listAllOfType(SmelterRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SmelterEmiRecipe(recipe, maxSmelterVolume));
        }
        registry.addRecipeHandler(ScreenhandlersKt.getSMELTER_HANDLER(),
            new SimpleRecipeHandler<>(SMELTER_CATEGORY, List.of(41), 0));

        // Furnaces
        for (EmiStack stack : FURNACE_STACKS) registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, stack);
        for (EmiStack stack : FURNACE_FACTORY_STACKS)
            registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, stack);
    }

    private static EmiStack[] getAllTiers(MachineRegistry registry) {
        return Arrays.stream(registry.getTiers()).map(registry::block).map(EmiStack::of).toArray(EmiStack[]::new);
    }

    public static Identifier irId(String path) {
        return new Identifier("indrev", path);
    }
}
