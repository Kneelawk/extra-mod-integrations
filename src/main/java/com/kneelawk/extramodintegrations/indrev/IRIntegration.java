package com.kneelawk.extramodintegrations.indrev;

import java.util.Arrays;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.recipes.machines.CompressorRecipe;
import me.steven.indrev.recipes.machines.CondenserRecipe;
import me.steven.indrev.recipes.machines.ElectrolysisRecipe;
import me.steven.indrev.recipes.machines.InfuserRecipe;
import me.steven.indrev.recipes.machines.ModuleRecipe;
import me.steven.indrev.recipes.machines.PulverizerRecipe;
import me.steven.indrev.recipes.machines.RecyclerRecipe;
import me.steven.indrev.recipes.machines.SawmillRecipe;
import me.steven.indrev.registry.MachineRegistry;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.AbstractIRIntegration;
import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;

@SuppressWarnings("unused")
public class IRIntegration extends AbstractIRIntegration {
    public static final EmiStack[] COMPRESSOR_STACKS = getAllTiers(MachineRegistry.Companion.getCOMPRESSOR_REGISTRY());
    public static final EmiStack[] COMPRESSOR_FACTORY_STACKS =
        getAllTiers(MachineRegistry.Companion.getCOMPRESSOR_FACTORY_REGISTRY());
    public static final EmiStack[] CONDENSER_STACKS = getAllTiers(MachineRegistry.Companion.getCONDENSER_REGISTRY());
    public static final EmiStack[] ELECTROLYSIS_STACKS =
        getAllTiers(MachineRegistry.Companion.getELECTROLYTIC_SEPARATOR_REGISTRY());
    public static final EmiStack[] SOLID_INFUSER_STACKS =
        getAllTiers(MachineRegistry.Companion.getSOLID_INFUSER_REGISTRY());
    public static final EmiStack[] PULVERIZER_STACKS = getAllTiers(MachineRegistry.Companion.getPULVERIZER_REGISTRY());
    public static final EmiStack[] RECYCLER_STACKS = getAllTiers(MachineRegistry.Companion.getRECYCLER_REGISTRY());
    public static final EmiStack[] SAWMILL_STACKS = getAllTiers(MachineRegistry.Companion.getSAWMILL_REGISTRY());
    public static final EmiStack[] MODULAR_WORKBENCH_STACKS =
        getAllTiers(MachineRegistry.Companion.getMODULAR_WORKBENCH_REGISTRY());

    public static final EmiRecipeCategory COMPRESS_CATEGORY =
        new EmiRecipeCategory(irId("compress"), COMPRESSOR_STACKS[0], ExMITextures.COMPRESSING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory CONDENSER_CATEGORY =
        new EmiRecipeCategory(irId("condenser"), CONDENSER_STACKS[0], ExMITextures.CONDENSING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory ELECTROLYSIS_CATEGORY =
        new EmiRecipeCategory(irId("electrolysis"), ELECTROLYSIS_STACKS[0], ExMITextures.ELECTROLYZING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory INFUSE_CATEGORY =
        new EmiRecipeCategory(irId("infuse"), SOLID_INFUSER_STACKS[0], ExMITextures.CHEMICAL_REACTING,
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

        // Condenser
        registry.addCategory(CONDENSER_CATEGORY);
        for (EmiStack stack : CONDENSER_STACKS) registry.addWorkstation(CONDENSER_CATEGORY, stack);
        for (CondenserRecipe recipe : manager.listAllOfType(CondenserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new CondenserEmiRecipe(recipe));
        }

        // Electrolysis
        registry.addCategory(ELECTROLYSIS_CATEGORY);
        for (EmiStack stack : ELECTROLYSIS_STACKS) registry.addWorkstation(ELECTROLYSIS_CATEGORY, stack);
        for (ElectrolysisRecipe recipe : manager.listAllOfType(ElectrolysisRecipe.Companion.getTYPE())) {
            registry.addRecipe(new ElectrolysisEmiRecipe(recipe));
        }

        // Solid Infuser
        registry.addCategory(INFUSE_CATEGORY);
        for (EmiStack stack : SOLID_INFUSER_STACKS) registry.addWorkstation(INFUSE_CATEGORY, stack);
        for (InfuserRecipe recipe : manager.listAllOfType(InfuserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, INFUSE_CATEGORY));
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
        for (PulverizerRecipe recipe : manager.listAllOfType(PulverizerRecipe.Companion.getTYPE())) {
            registry.addRecipe(new PulverizerEmiRecipe(recipe));
        }

        // Recycler
        registry.addCategory(RECYCLE_CATEGORY);
        for (EmiStack stack : RECYCLER_STACKS) registry.addWorkstation(RECYCLE_CATEGORY, stack);
        for (RecyclerRecipe recipe : manager.listAllOfType(RecyclerRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, RECYCLE_CATEGORY));
        }

        // Sawmill
        registry.addCategory(SAWMILL_CATEGORY);
        for (EmiStack stack : SAWMILL_STACKS) registry.addWorkstation(SAWMILL_CATEGORY, stack);
        for (SawmillRecipe recipe : manager.listAllOfType(SawmillRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SawmillEmiRecipe(recipe));
        }
    }

    private static EmiStack[] getAllTiers(MachineRegistry registry) {
        return Arrays.stream(registry.getTiers()).map(registry::block).map(EmiStack::of).toArray(EmiStack[]::new);
    }

    public static Identifier irId(String path) {
        return new Identifier("indrev", path);
    }
}