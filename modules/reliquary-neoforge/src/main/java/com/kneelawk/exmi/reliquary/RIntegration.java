package com.kneelawk.exmi.reliquary;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import reliquary.Reliquary;
import reliquary.crafting.AlkahestryChargingRecipe;
import reliquary.crafting.AlkahestryCraftingRecipe;
import reliquary.crafting.AlkahestryRecipeRegistry;
import reliquary.init.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.core.api.util.RecipeReverseLookup;
import com.kneelawk.exmi.reliquary.recipe.AlkahestryChargingEmiRecipe;
import com.kneelawk.exmi.reliquary.recipe.AlkahestryCraftingEmiRecipe;

public class RIntegration implements ExMIPlugin {
    public static final EmiStack CRAFTING_TABLE = EmiStack.of(Blocks.CRAFTING_TABLE);
    public static final EmiStack ALKAHESTRY_TOME = EmiStack.of(ModItems.ALKAHESTRY_TOME.get());

    public static final EmiRecipeCategory ALKAHESTRY_CHARGING =
        new EmiRecipeCategory(Reliquary.getRL("alkahestry_charging"), ALKAHESTRY_TOME);
    public static final EmiRecipeCategory ALKAHESTRY_CRAFTING =
        new EmiRecipeCategory(Reliquary.getRL("alkahestry_crafting"), ALKAHESTRY_TOME);

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
    }
}
