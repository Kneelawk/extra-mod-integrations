/*
 * Copyright (c) 2020 vectorwing, the Farmer's Delight Refabricated authors
 * License available at https://github.com/MehVahdJukaar/FarmersDelightRefabricated/blob/b6690b2106abc6205021e40f9117c03f36323362/LICENSE
 */

package com.kneelawk.exmi.farmersdelight;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
                EmiIngredient.of(recipe.value().getIngredients().get(0)), recipe.value().getRollableResults().stream()
                .map(chanceResult -> EmiStack.of(chanceResult.stack()).setChance(chanceResult.chance())).toList()));
        }
        registry.addRecipe(new DecompositionEmiRecipe());

        addInfo(registry, "straw", ModItems.STRAW);
        addInfo(registry, "ham", ModItems.HAM);
        addInfo(registry, "ham", "smoked", ModItems.SMOKED_HAM);
        addInfo(registry, "knife", "flint", ModItems.FLINT_KNIFE);
        addInfo(registry, "knife", "iron", ModItems.IRON_KNIFE);
        addInfo(registry, "knife", "diamond", ModItems.DIAMOND_KNIFE);
        addInfo(registry, "knife", "netherite", ModItems.NETHERITE_KNIFE);
        addInfo(registry, "knife", "golden", ModItems.GOLDEN_KNIFE);
        addInfo(registry, "wild_cabbages", ModItems.WILD_CABBAGES, ModItems.CABBAGE, ModItems.CABBAGE_LEAF);
        addInfo(registry, "wild_beetroots", ModItems.WILD_BEETROOTS, Items.BEETROOT);
        addInfo(registry, "wild_carrots", ModItems.WILD_CARROTS, Items.CARROT);
        addInfo(registry, "wild_onions", ModItems.WILD_ONIONS, ModItems.ONION);
        addInfo(registry, "wild_potatoes", ModItems.WILD_POTATOES, Items.POTATO);
        addInfo(registry, "wild_tomatoes", ModItems.WILD_TOMATOES, ModItems.TOMATO);
        addInfo(registry, "wild_rice", ModItems.WILD_RICE, ModItems.RICE);
    }

    private static void addInfo(EmiRegistry registry, String name, Object... items) {
        addInfo(registry, name, "", items);
    }

    private static void addInfo(EmiRegistry registry, String name, String extraName, Object... items) {
        registry.addRecipe(new EmiInfoRecipe(Arrays.stream(items).map(o -> (EmiIngredient) switch (o) {
            case Supplier<?> s -> EmiStack.of((Item) s.get());
            case Item i -> EmiStack.of(i);
            default -> throw new IllegalStateException("Unexpected value: " + o);
        }).toList(), List.of(Component.translatable(FarmersDelight.MODID + ".jei.info." + name)),
            res("/info/" + name + "/" + extraName)));
    }

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, path);
    }
}
