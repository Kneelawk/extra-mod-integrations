package com.kneelawk.exmi.reliquary;

import java.util.stream.Collectors;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import reliquary.Reliquary;
import reliquary.crafting.AlkahestryChargingRecipe;
import reliquary.crafting.AlkahestryCraftingRecipe;
import reliquary.crafting.AlkahestryRecipeRegistry;
import reliquary.init.ModBlocks;
import reliquary.init.ModItems;
import reliquary.util.potions.PotionEssence;
import reliquary.util.potions.PotionMap;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.core.api.util.RecipeReverseLookup;
import com.kneelawk.exmi.reliquary.recipe.AlkahestryChargingEmiRecipe;
import com.kneelawk.exmi.reliquary.recipe.AlkahestryCraftingEmiRecipe;
import com.kneelawk.exmi.reliquary.recipe.MortarEmiRecipe;

public class RIntegration implements ExMIPlugin {
    public static final EmiStack CRAFTING_TABLE = EmiStack.of(Blocks.CRAFTING_TABLE);
    public static final EmiStack ALKAHESTRY_TOME = EmiStack.of(ModItems.ALKAHESTRY_TOME.get());
    public static final EmiStack MORTAR_STACK = EmiStack.of(ModBlocks.APOTHECARY_MORTAR_ITEM.get());
    public static final EmiStack CAULDRON_STACK = EmiStack.of(ModBlocks.APOTHECARY_CAULDRON_ITEM.get());
    public static final EmiStack POTION_ESSENCE = EmiStack.of(ModItems.POTION_ESSENCE.get());
    public static final EmiStack POTION = EmiStack.of(ModItems.POTION.get());

    public static final EmiRecipeCategory ALKAHESTRY_CHARGING =
        new EmiRecipeCategory(Reliquary.getRL("alkahestry_charging"), ALKAHESTRY_TOME);
    public static final EmiRecipeCategory ALKAHESTRY_CRAFTING =
        new EmiRecipeCategory(Reliquary.getRL("alkahestry_crafting"), ALKAHESTRY_TOME);
    public static final EmiRecipeCategory APOTHECARY_MORTAR =
        new EmiRecipeCategory(Reliquary.getRL("apothecary_mortar"), MORTAR_STACK);
    public static final EmiRecipeCategory APOTHECARY_CAULDRON =
        new EmiRecipeCategory(Reliquary.getRL("apothecary_cauldron"), CAULDRON_STACK);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ALKAHESTRY_CHARGING);
        registry.addWorkstation(ALKAHESTRY_CHARGING, CRAFTING_TABLE);
        for (AlkahestryChargingRecipe recipe : AlkahestryRecipeRegistry.getChargingRecipes()) {
            ResourceLocation normalId = RecipeReverseLookup.getId(recipe);
            if (normalId == null) continue; // filter out duplicates

            ResourceLocation id =
                Reliquary.getRL("/alkahestry_charging/" + normalId.getNamespace() + "/" + normalId.getPath());
            registry.addRecipe(new AlkahestryChargingEmiRecipe(recipe, id));
        }

        registry.addCategory(ALKAHESTRY_CRAFTING);
        registry.addWorkstation(ALKAHESTRY_CRAFTING, CRAFTING_TABLE);
        for (AlkahestryCraftingRecipe recipe : AlkahestryRecipeRegistry.getCraftingRecipes()) {
            ResourceLocation normalId = RecipeReverseLookup.getId(recipe);
            if (normalId == null) continue; // filter out duplicates

            ResourceLocation id =
                Reliquary.getRL("/alkahestry_crafting/" + normalId.getNamespace() + "/" + normalId.getPath());
            registry.addRecipe(new AlkahestryCraftingEmiRecipe(recipe, id));
        }

        registry.addCategory(APOTHECARY_MORTAR);
        registry.addWorkstation(APOTHECARY_MORTAR, MORTAR_STACK);
        registry.setDefaultComparison(POTION_ESSENCE, Comparison.compareComponents());
        Registry<Item> items = BuiltInRegistries.ITEM;
        for (PotionEssence essence : PotionMap.potionCombinations) {
            String combined = essence.getIngredients().stream().map(i -> {
                    ResourceLocation key = items.getKey(i.getItem().getItem());
                    return key.getNamespace() + "/" + key.getPath();
                })
                .sorted().collect(Collectors.joining("/"));
            ResourceLocation id = Reliquary.getRL("/apothecary_mortar/" + combined);
            registry.addRecipe(new MortarEmiRecipe(essence, id));
        }

        registry.setDefaultComparison(POTION, Comparison.compareComponents());
    }
}
