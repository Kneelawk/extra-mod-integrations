package com.kneelawk.extramodintegrations.indrev;

import java.util.Arrays;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.recipes.machines.*;
import me.steven.indrev.registry.MachineRegistry;

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
    public static final EmiStack[] ELECTROLYSIS_STACKS = getAllTiers(MachineRegistry.Companion.getELECTROLYTIC_SEPARATOR_REGISTRY());
    public static final EmiStack[] SOLID_INFUSER_STACKS = getAllTiers(MachineRegistry.Companion.getSOLID_INFUSER_REGISTRY());
    public static final EmiStack[] PULVERIZER_STACKS = getAllTiers(MachineRegistry.Companion.getPULVERIZER_REGISTRY());
    public static final EmiStack[] SAWMILL_STACKS = getAllTiers(MachineRegistry.Companion.getSAWMILL_REGISTRY());

    public static final EmiRecipeCategory COMPRESSOR_CATEGORY =
        new EmiRecipeCategory(irId("compressor"), COMPRESSOR_STACKS[0], ExMITextures.COMPRESSING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory CONDENSER_CATEGORY =
        new EmiRecipeCategory(irId("condenser"), CONDENSER_STACKS[0], ExMITextures.CONDENSING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory ELECTROLIC_SEPERATOR_CATEGORY =
        new EmiRecipeCategory(irId("electrolic_seperator"), ELECTROLYSIS_STACKS[0], ExMITextures.CONDENSING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory SOLID_INFUSER_CATEGORY =
        new EmiRecipeCategory(irId("solid_infuser"), SOLID_INFUSER_STACKS[0], ExMITextures.CHEMICAL_REACTING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory PULVERIZER_CATEGORY =
        new EmiRecipeCategory(irId("pulverizer"), PULVERIZER_STACKS[0], ExMITextures.GRINDING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory SAWMILL_CATEGORY =
        new EmiRecipeCategory(irId("sawmill"), SAWMILL_STACKS[0], ExMITextures.SAWMILLING,
            EmiRecipeSorting.compareOutputThenInput());

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading Industrial Revolution Integration...");

        // Compressor
        registry.addCategory(COMPRESSOR_CATEGORY);
        for (EmiStack stack : COMPRESSOR_STACKS) registry.addWorkstation(COMPRESSOR_CATEGORY, stack);
        for (EmiStack stack : COMPRESSOR_FACTORY_STACKS) registry.addWorkstation(COMPRESSOR_CATEGORY, stack);
        for (CompressorRecipe recipe : registry.getRecipeManager()
            .listAllOfType(CompressorRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, COMPRESSOR_CATEGORY));
        }

        // Condenser
        registry.addCategory(CONDENSER_CATEGORY);
        for (EmiStack stack : CONDENSER_STACKS) registry.addWorkstation(CONDENSER_CATEGORY, stack);
        for (CondenserRecipe recipe : registry.getRecipeManager().listAllOfType(CondenserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new CondenserEmiRecipe(recipe));
        }

        // Electrolysis
        registry.addCategory(ELECTROLIC_SEPERATOR_CATEGORY);
        for (EmiStack stack : ELECTROLYSIS_STACKS) registry.addWorkstation(ELECTROLIC_SEPERATOR_CATEGORY, stack);
        for (ElectrolysisRecipe recipe: registry.getRecipeManager().listAllOfType(ElectrolysisRecipe.Companion.getTYPE())) {
            registry.addRecipe(new ElectrolysisEmiRecipe(recipe));
        }

        // Solid Infuser
        registry.addCategory(SOLID_INFUSER_CATEGORY);
        for (EmiStack stack : SOLID_INFUSER_STACKS) registry.addWorkstation(SOLID_INFUSER_CATEGORY, stack);
        for (InfuserRecipe recipe : registry.getRecipeManager().listAllOfType(InfuserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, SOLID_INFUSER_CATEGORY));
        }

        // Pulverizer
        registry.addCategory(PULVERIZER_CATEGORY);
        for (EmiStack stack : PULVERIZER_STACKS) registry.addWorkstation(PULVERIZER_CATEGORY, stack);
        for (PulverizerRecipe recipe : registry.getRecipeManager().listAllOfType(PulverizerRecipe.Companion.getTYPE())) {
            registry.addRecipe(new PulverizerEmiRecipe(recipe));
        }

        // Sawmill
        registry.addCategory(SAWMILL_CATEGORY);
        for (EmiStack stack : SAWMILL_STACKS) registry.addWorkstation(SAWMILL_CATEGORY, stack);
        for (SawmillRecipe recipe : registry.getRecipeManager().listAllOfType(SawmillRecipe.Companion.getTYPE())) {
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
