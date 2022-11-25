package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

@SuppressWarnings("unused")
public class TRIntegrationImpl extends TRIntegration {
    public static final EmiStack CELL = EmiStack.of(TRContent.CELL);
    public static final EmiStack ALLOY_SMELTER_STACK = EmiStack.of(TRContent.Machine.ALLOY_SMELTER);
    public static final EmiStack IRON_ALLOY_FURNACE_STACK = EmiStack.of(TRContent.Machine.IRON_ALLOY_FURNACE);
    public static final EmiStack ASSEMBLY_MACHINE_STACK = EmiStack.of(TRContent.Machine.ASSEMBLY_MACHINE);
    public static final EmiStack BLAST_FURNACE_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
    public static final EmiStack INDUSTRIAL_CENTRIFUGE_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_CENTRIFUGE);
    public static final EmiStack GRINDER_STACK = EmiStack.of(TRContent.Machine.GRINDER);

    public static final EmiRecipeCategory ALLOY_SMELTER_CATEGORY =
        new EmiRecipeCategory(trId("alloy_smelter"), ALLOY_SMELTER_STACK, ExMITextures.ALLOY_SMELTING);
    public static final EmiRecipeCategory ASSEMBLING_MACHINE_CATEGORY =
        new EmiRecipeCategory(trId("assembling_machine"), ASSEMBLY_MACHINE_STACK, ExMITextures.ASSEMBLING);
    public static final EmiRecipeCategory BLAST_FURNACE_CATEGORY =
        new EmiRecipeCategory(trId("blast_furnace"), BLAST_FURNACE_STACK, ExMITextures.BLAST_FURNACE);
    public static final EmiRecipeCategory CENTRIFUGE_CATEGORY =
        new EmiRecipeCategory(trId("centrifuge"), INDUSTRIAL_CENTRIFUGE_STACK, ExMITextures.CENTRIFUGE);
    public static final EmiRecipeCategory GRINDER_CATEGORY =
        new EmiRecipeCategory(trId("grinder"), GRINDER_STACK, ExMITextures.GRINDING);

    @Override
    void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading TechReborn Integration...");

        // Alloy Smelting
        registry.addCategory(ALLOY_SMELTER_CATEGORY);
        registry.addWorkstation(ALLOY_SMELTER_CATEGORY, ALLOY_SMELTER_STACK);
        registry.addWorkstation(ALLOY_SMELTER_CATEGORY, IRON_ALLOY_FURNACE_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.ALLOY_SMELTER)) {
            registry.addRecipe(new AlloySmelterEmiRecipe(recipe));
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

        // Grinding
        registry.addCategory(GRINDER_CATEGORY);
        registry.addWorkstation(GRINDER_CATEGORY, GRINDER_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.GRINDER)) {
            registry.addRecipe(new GrinderEmiRecipe(recipe));
        }

        // Cells should be compared with NBT data
        registry.setDefaultComparison(CELL, comp -> comp.copy().nbt(true).build());
    }

    public static Identifier trId(String path) {
        return new Identifier("techreborn", path);
    }
}
