package com.kneelawk.extramodintegrations.indrev;

import java.util.Arrays;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.recipes.machines.CompressorRecipe;
import me.steven.indrev.recipes.machines.CondenserRecipe;
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

    public static final EmiRecipeCategory COMPRESSOR_CATEGORY =
        new EmiRecipeCategory(irId("compressor"), COMPRESSOR_STACKS[0], ExMITextures.COMPRESSING,
            EmiRecipeSorting.compareOutputThenInput());
    public static final EmiRecipeCategory CONDENSER_CATEGORY =
        new EmiRecipeCategory(irId("condenser"), CONDENSER_STACKS[0], ExMITextures.CONDENSING,
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
    }

    private static EmiStack[] getAllTiers(MachineRegistry registry) {
        return Arrays.stream(registry.getTiers()).map(registry::block).map(EmiStack::of).toArray(EmiStack[]::new);
    }

    public static Identifier irId(String path) {
        return new Identifier("indrev", path);
    }
}
