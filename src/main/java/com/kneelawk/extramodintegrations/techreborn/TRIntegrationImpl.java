package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
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
    public static final EmiStack CHEMICAL_REACTOR_STACK = EmiStack.of(TRContent.Machine.CHEMICAL_REACTOR);
    public static final EmiStack COMPRESSOR_STACK = EmiStack.of(TRContent.Machine.COMPRESSOR);
    public static final EmiStack DISTILLATION_TOWER_STACK = EmiStack.of(TRContent.Machine.DISTILLATION_TOWER);
    public static final EmiStack EXTRACTOR_STACK = EmiStack.of(TRContent.Machine.EXTRACTOR);
    public static final EmiStack IMPLOSION_COMPRESSOR_STACK = EmiStack.of(TRContent.Machine.IMPLOSION_COMPRESSOR);
    public static final EmiStack INDUSTRIAL_ELECTROLYZER_STACK = EmiStack.of(TRContent.Machine.INDUSTRIAL_ELECTROLYZER);
    public static final EmiStack GRINDER_STACK = EmiStack.of(TRContent.Machine.GRINDER);

    public static final EmiStack AUTO_CRAFTING_TABLE_STACK = EmiStack.of(TRContent.Machine.AUTO_CRAFTING_TABLE);
    public static final EmiStack IRON_FURNACE_STACK = EmiStack.of(TRContent.Machine.IRON_FURNACE);
    public static final EmiStack ELECTRIC_FURNACE_STACK = EmiStack.of(TRContent.Machine.ELECTRIC_FURNACE);

    public static final EmiRecipeCategory ALLOY_SMELTER_CATEGORY =
        new EmiRecipeCategory(trId("alloy_smelter"), ALLOY_SMELTER_STACK, ExMITextures.ALLOY_SMELTING);
    public static final EmiRecipeCategory ASSEMBLING_MACHINE_CATEGORY =
        new EmiRecipeCategory(trId("assembling_machine"), ASSEMBLY_MACHINE_STACK, ExMITextures.ASSEMBLING);
    public static final EmiRecipeCategory BLAST_FURNACE_CATEGORY =
        new EmiRecipeCategory(trId("blast_furnace"), BLAST_FURNACE_STACK, ExMITextures.BLAST_FURNACE);
    public static final EmiRecipeCategory CENTRIFUGE_CATEGORY =
        new EmiRecipeCategory(trId("centrifuge"), INDUSTRIAL_CENTRIFUGE_STACK, ExMITextures.CENTRIFUGE);
    public static final EmiRecipeCategory CHEMICAL_REACTOR_CATEGORY =
        new EmiRecipeCategory(trId("chemical_reactor"), CHEMICAL_REACTOR_STACK, ExMITextures.CHEMICAL_REACTING);
    public static final EmiRecipeCategory COMPRESSOR_CATEGORY =
        new EmiRecipeCategory(trId("compressor"), COMPRESSOR_STACK, ExMITextures.COMPRESSING);
    public static final EmiRecipeCategory DISTILLATION_TOWER_CATEGORY =
        new EmiRecipeCategory(trId("distillation_tower"), DISTILLATION_TOWER_STACK, ExMITextures.DISTILLATION_TOWER);
    public static final EmiRecipeCategory EXTRACTOR_CATEGORY =
        new EmiRecipeCategory(trId("extractor"), EXTRACTOR_STACK, ExMITextures.EXTRACTING);
    public static final EmiRecipeCategory IMPLOSION_COMPRESSOR_CATEGORY =
        new EmiRecipeCategory(trId("implosion_compressor"), IMPLOSION_COMPRESSOR_STACK,
            ExMITextures.IMPLOSION_COMPRESSING);
    public static final EmiRecipeCategory INDUSTRIAL_ELECTROLYZER_CATEGORY =
        new EmiRecipeCategory(trId("industrial_electrolyzer"), INDUSTRIAL_ELECTROLYZER_STACK,
            ExMITextures.ELECTROLYZING);
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

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, AUTO_CRAFTING_TABLE_STACK);
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, IRON_FURNACE_STACK);
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, ELECTRIC_FURNACE_STACK);

        // Cells should be compared with NBT data
        registry.setDefaultComparison(CELL, comp -> comp.copy().nbt(true).build());
    }

    public static Identifier trId(String path) {
        return new Identifier("techreborn", path);
    }
}
