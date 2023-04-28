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

    public static final EmiRecipeCategory PULVERIZE_CATEGORY =
        new EmiRecipeCategory(irId("pulverize"), PULVERIZER_STACKS[0], ExMITextures.GRINDING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory SAWMILL_CATEGORY =
        new EmiRecipeCategory(irId("sawmill"), SAWMILL_STACKS[0], ExMITextures.SAWMILLING,
            EmiRecipeSorting.compareOutputThenInput());

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading Industrial Revolution Integration...");

        // Compressor
        registry.addCategory(COMPRESS_CATEGORY);
        for (EmiStack stack : COMPRESSOR_STACKS) registry.addWorkstation(COMPRESS_CATEGORY, stack);
        for (EmiStack stack : COMPRESSOR_FACTORY_STACKS) registry.addWorkstation(COMPRESS_CATEGORY, stack);
        for (CompressorRecipe recipe : registry.getRecipeManager()
            .listAllOfType(CompressorRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleOneInputEmiRecipe(recipe, COMPRESS_CATEGORY));
        }

        // Condenser
        registry.addCategory(CONDENSER_CATEGORY);
        for (EmiStack stack : CONDENSER_STACKS) registry.addWorkstation(CONDENSER_CATEGORY, stack);
        for (CondenserRecipe recipe : registry.getRecipeManager().listAllOfType(CondenserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new CondenserEmiRecipe(recipe));
        }

        // Electrolysis
        registry.addCategory(ELECTROLYSIS_CATEGORY);
        for (EmiStack stack : ELECTROLYSIS_STACKS) registry.addWorkstation(ELECTROLYSIS_CATEGORY, stack);
        for (ElectrolysisRecipe recipe: registry.getRecipeManager().listAllOfType(ElectrolysisRecipe.Companion.getTYPE())) {
            registry.addRecipe(new ElectrolysisEmiRecipe(recipe));
        }

        // Solid Infuser
        registry.addCategory(INFUSE_CATEGORY);
        for (EmiStack stack : SOLID_INFUSER_STACKS) registry.addWorkstation(INFUSE_CATEGORY, stack);
        for (InfuserRecipe recipe : registry.getRecipeManager().listAllOfType(InfuserRecipe.Companion.getTYPE())) {
            registry.addRecipe(new SimpleTwoInputEmiRecipe(recipe, INFUSE_CATEGORY));
        }

        // Pulverizer
        registry.addCategory(PULVERIZE_CATEGORY);
        for (EmiStack stack : PULVERIZER_STACKS) registry.addWorkstation(PULVERIZE_CATEGORY, stack);
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
