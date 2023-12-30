package com.kneelawk.extramodintegrations.appeng;

import appeng.core.definitions.AEBlocks;
import appeng.menu.implementations.InscriberMenu;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import com.kneelawk.extramodintegrations.AbstractAE2Integration;
import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMITextures;

@SuppressWarnings("unused")
public class AE2Integration extends AbstractAE2Integration {
    public static final EmiStack CHARGER_STACK = EmiStack.of(AEBlocks.CHARGER);
    public static final EmiStack CRANK_STACK = EmiStack.of(AEBlocks.CRANK);
    public static final EmiStack INSCRIBER_STACK = EmiStack.of(AEBlocks.INSCRIBER);

    public static final EmiRecipeCategory CHARGER_CATEGORY =
        new EmiRecipeCategory(ae2Id("charger"), CHARGER_STACK, ExMITextures.ENERGIZING,
            EmiRecipeSorting.compareOutputThenInput());

    public static final EmiRecipeCategory INSCRIBER_CATEGORY =
        new EmiRecipeCategory(ae2Id("inscriber"), INSCRIBER_STACK, ExMITextures.COMPRESSING,
            EmiRecipeSorting.compareOutputThenInput());

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.logLoading("Applied Energistics 2");
        RecipeManager manager = registry.getRecipeManager();

        // Charger
        registry.addCategory(CHARGER_CATEGORY);
        registry.addWorkstation(CHARGER_CATEGORY, CHARGER_STACK);
        registry.addWorkstation(CHARGER_CATEGORY, CRANK_STACK);
        for (ChargerRecipe recipe : manager.listAllOfType(ChargerRecipe.TYPE)) {
            registry.addRecipe(new ChargerEmiRecipe(recipe));
        }

        // Inscriber
        registry.addCategory(INSCRIBER_CATEGORY);
        registry.addWorkstation(INSCRIBER_CATEGORY, INSCRIBER_STACK);
        for (InscriberRecipe recipe : manager.listAllOfType(InscriberRecipe.TYPE)) {
            registry.addRecipe(new InscriberEmiRecipe(recipe));
        }
        registry.addRecipeHandler(InscriberMenu.TYPE, new InscriberRecipeHandler());
    }

    public static Identifier ae2Id(String path) {
        return new Identifier("ae2", path);
    }
}
