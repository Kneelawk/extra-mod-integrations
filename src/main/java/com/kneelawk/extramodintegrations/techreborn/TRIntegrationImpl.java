package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.ExMIMod;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

import static com.kneelawk.extramodintegrations.ExMIPlugin.SIMPLIFIED_ICONS;

@SuppressWarnings("unused")
public class TRIntegrationImpl extends TRIntegration {
    public static final EmiStack ALLOY_SMELTER_STACK = EmiStack.of(TRContent.Machine.ALLOY_SMELTER);
    public static final EmiStack IRON_ALLOY_FURNACE_STACK = EmiStack.of(TRContent.Machine.IRON_ALLOY_FURNACE);
    public static final EmiStack ASSEMBLY_MACHINE_STACK = EmiStack.of(TRContent.Machine.ASSEMBLY_MACHINE);
    public static final EmiStack GRINDER_STACK = EmiStack.of(TRContent.Machine.GRINDER);

    public static final EmiRecipeCategory ALLOY_SMELTER_CATEGORY =
        new EmiRecipeCategory(new Identifier("techreborn:alloy_smelter"), ALLOY_SMELTER_STACK,
            new EmiTexture(SIMPLIFIED_ICONS, 16, 0, 16, 16));
    public static final EmiRecipeCategory ASSEMBLING_MACHINE_CATEGORY =
        new EmiRecipeCategory(new Identifier("techreborn:assembling_machine"), ASSEMBLY_MACHINE_STACK,
            new EmiTexture(SIMPLIFIED_ICONS, 32, 0, 16, 16));
    public static final EmiRecipeCategory GRINDER_CATEGORY =
        new EmiRecipeCategory(new Identifier("techreborn:grinder"), GRINDER_STACK,
            new EmiTexture(SIMPLIFIED_ICONS, 0, 0, 16, 16));

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

        // Grinding
        registry.addCategory(GRINDER_CATEGORY);
        registry.addWorkstation(GRINDER_CATEGORY, GRINDER_STACK);
        for (RebornRecipe recipe : registry.getRecipeManager().listAllOfType(ModRecipes.GRINDER)) {
            registry.addRecipe(new GrinderEmiRecipe(recipe));
        }
    }
}
