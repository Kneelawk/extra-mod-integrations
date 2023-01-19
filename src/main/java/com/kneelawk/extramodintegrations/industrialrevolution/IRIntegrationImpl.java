package com.kneelawk.extramodintegrations.industrialrevolution;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import me.steven.indrev.recipes.machines.CompressorRecipe;
import me.steven.indrev.registry.MachineRegistry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

@SuppressWarnings("unused")
public class IRIntegrationImpl extends IRIntegration {
    public static final EmiStack[] COMPRESSOR_STACKS = getAllTiers(MachineRegistry.Companion.getCOMPRESSOR_REGISTRY());

    public static final EmiRecipeCategory COMPRESSOR_CATEGORY = new EmiRecipeCategory(irId("compressor"), COMPRESSOR_STACKS[0], ExMITextures.COMPRESSING, EmiRecipeSorting.compareOutputThenInput());

    @Override
    void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading Industrial Revolution Integration...");

        // Compressor
        registry.addCategory(COMPRESSOR_CATEGORY);
        for (EmiStack stack : COMPRESSOR_STACKS) registry.addWorkstation(COMPRESSOR_CATEGORY, stack);
        for (CompressorRecipe recipe : registry.getRecipeManager().listAllOfType(CompressorRecipe.Companion.getTYPE())) {
            // TODO: Make CompressorEmiRecipe and use it here
        }
    }

    private static EmiStack[] getAllTiers(MachineRegistry registry) {
        return Arrays.stream(registry.getTiers()).map(registry::block).map(EmiStack::of).toArray(EmiStack[]::new);
    }

    public static Identifier irId(String path) {
        return new Identifier("indrev", path);
    }
}
