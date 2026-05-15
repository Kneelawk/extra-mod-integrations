package com.kneelawk.exmi.rechiseled;

import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingRecipeManager;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.exmi.core.api.ExMIPlugin;

public class RIntegration implements ExMIPlugin {
    private static final EmiStack CHISEL = EmiStack.of(Rechiseled.chisel);

    public static final EmiRecipeCategory CHISELING =
        new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath("rechiseled", "chiseling"), CHISEL);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CHISELING);

        registry.addWorkstation(CHISELING, CHISEL);

        for (ChiselingRecipe recipe : ChiselingRecipeManager.get(true).getAllRecipes()) {
            registry.addRecipe(new ChiselingEmiRecipe(recipe));
        }
    }
}
