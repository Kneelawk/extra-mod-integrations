/*
 * Copyright (c) 2020 vectorwing, the Farmer's Delight Refabricated authors
 * License available at https://github.com/MehVahdJukaar/FarmersDelightRefabricated/blob/b6690b2106abc6205021e40f9117c03f36323362/LICENSE
 */

package com.kneelawk.exmi.farmersdelight;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.farmersdelight.handler.CookingPotEmiRecipeHandler;
import com.kneelawk.exmi.farmersdelight.recipe.CookingPotEmiRecipe;
import com.kneelawk.exmi.farmersdelight.recipe.CuttingEmiRecipe;
import com.kneelawk.exmi.farmersdelight.recipe.DecompositionEmiRecipe;

public class FDIntegration implements ExMIPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FDRecipeCategories.COOKING);
        registry.addCategory(FDRecipeCategories.CUTTING);
        registry.addCategory(FDRecipeCategories.DECOMPOSITION);

        registry.addWorkstation(FDRecipeCategories.COOKING, FDRecipeWorkstations.COOKING_POT);
        registry.addWorkstation(FDRecipeCategories.CUTTING, FDRecipeWorkstations.CUTTING_BOARD);
        registry.addRecipeHandler(ModMenuTypes.COOKING_POT.get(), new CookingPotEmiRecipeHandler());
        //TODO: add ability to client on recipe arrow. we can also do it directly from recipe screen


        for (RecipeHolder<CookingPotRecipe> recipe : registry.getRecipeManager()
            .getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            registry.addRecipe(new CookingPotEmiRecipe(recipe.id(),
                recipe.value().getIngredients().stream().map(EmiIngredient::of).toList(),
                EmiStack.of(recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess())),
                EmiStack.of(recipe.value().getOutputContainer()), recipe.value().getCookTime(),
                recipe.value().getExperience()));
        }

        for (RecipeHolder<CuttingBoardRecipe> recipe : registry.getRecipeManager()
            .getAllRecipesFor(ModRecipeTypes.CUTTING.get())) {
            registry.addRecipe(new CuttingEmiRecipe(recipe.id(), EmiIngredient.of(recipe.value().getTool()),
                EmiIngredient.of(recipe.value().getIngredients().get(0)),
                recipe.value().getRollableResults().stream()
                    .map(chanceResult -> EmiStack.of(chanceResult.stack()).setChance(chanceResult.chance())).toList()));
        }
        registry.addRecipe(new DecompositionEmiRecipe());
    }

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, path);
    }
}
