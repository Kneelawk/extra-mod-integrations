package com.kneelawk.extramodintegrations.chipped;

import com.kneelawk.extramodintegrations.AbstractChippedIntegration;
import com.kneelawk.extramodintegrations.ExMIMod;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import earth.terrarium.chipped.common.recipes.ChippedRecipe;
import earth.terrarium.chipped.common.registry.ModBlocks;
import earth.terrarium.chipped.common.registry.ModMenuTypes;
import earth.terrarium.chipped.common.registry.ModRecipeTypes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChippedIntegration extends AbstractChippedIntegration {

    public static final EmiRecipeCategory BOTANIST_WORKBENCH_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.BOTANIST_WORKBENCH);
    public static final EmiRecipeCategory GLASSBLOWER_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.GLASSBLOWER);
    public static final EmiRecipeCategory CARPENTERS_TABLE_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.CARPENTERS_TABLE);
    public static final EmiRecipeCategory LOOM_TABLE_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.LOOM_TABLE);
    public static final EmiRecipeCategory MASON_TABLE_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.MASON_TABLE);
    public static final EmiRecipeCategory ALCHEMY_BENCH_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.ALCHEMY_BENCH);
    public static final EmiRecipeCategory TINKERING_TABLE_CATEGORY = new ChippedEmiRecipeCategory(ModBlocks.TINKERING_TABLE);

    private static List<ChippedEmiRecipe.FlattenedRecipe> flatten(Collection<ChippedRecipe> recipes) {
        List<ChippedEmiRecipe.FlattenedRecipe> flattenedRecipes = new ArrayList<>();

        for (ChippedRecipe recipe : recipes) {
            for (RegistryEntryList<Item> tag : recipe.tags()) {
                List<Item> items = tag.stream().filter(RegistryEntry::hasKeyAndValue).map(RegistryEntry::value).toList();
                Ingredient ingredient = Ingredient.ofStacks(items.stream().map(ItemStack::new));

                for (Item item : items) {
                    flattenedRecipes.add(new ChippedEmiRecipe.FlattenedRecipe(item.toString(), ingredient, new ItemStack(item)));
                }
            }
        }
        return flattenedRecipes;
    }

    @Override
    protected void registerImpl(EmiRegistry registry) {

        ExMIMod.logLoading("Chipped");

        // categories
        registry.addCategory(BOTANIST_WORKBENCH_CATEGORY);
        registry.addCategory(GLASSBLOWER_CATEGORY);
        registry.addCategory(CARPENTERS_TABLE_CATEGORY);
        registry.addCategory(LOOM_TABLE_CATEGORY);
        registry.addCategory(MASON_TABLE_CATEGORY);
        registry.addCategory(ALCHEMY_BENCH_CATEGORY);
        registry.addCategory(TINKERING_TABLE_CATEGORY);

        // workstations
        registry.addWorkstation(BOTANIST_WORKBENCH_CATEGORY, EmiStack.of(ModBlocks.BOTANIST_WORKBENCH.get()));
        registry.addWorkstation(GLASSBLOWER_CATEGORY, EmiStack.of(ModBlocks.GLASSBLOWER.get()));
        registry.addWorkstation(CARPENTERS_TABLE_CATEGORY, EmiStack.of(ModBlocks.CARPENTERS_TABLE.get()));
        registry.addWorkstation(LOOM_TABLE_CATEGORY, EmiStack.of(ModBlocks.LOOM_TABLE.get()));
        registry.addWorkstation(MASON_TABLE_CATEGORY, EmiStack.of(ModBlocks.MASON_TABLE.get()));
        registry.addWorkstation(ALCHEMY_BENCH_CATEGORY, EmiStack.of(ModBlocks.ALCHEMY_BENCH.get()));
        registry.addWorkstation(TINKERING_TABLE_CATEGORY, EmiStack.of(ModBlocks.TINKERING_TABLE.get()));

        // handler
        registry.addRecipeHandler(ModMenuTypes.WORKBENCH.get(), new ChippedWorkbenchHandler());

        // recipes
        RecipeManager manager = registry.getRecipeManager();

        // Botanist Workbench Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.BOTANIST_WORKBENCH.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(BOTANIST_WORKBENCH_CATEGORY, recipe))
                .forEach(registry::addRecipe);

        // Glassblower Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.GLASSBLOWER.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(GLASSBLOWER_CATEGORY, recipe))
                .forEach(registry::addRecipe);

        // Carpenters Table Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.CARPENTERS_TABLE.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(CARPENTERS_TABLE_CATEGORY, recipe))
                .forEach(registry::addRecipe);

        // Loom Table Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.LOOM_TABLE.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(LOOM_TABLE_CATEGORY, recipe))
                .forEach(registry::addRecipe);

        // Mason Table Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.MASON_TABLE.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(MASON_TABLE_CATEGORY, recipe))
                .forEach(registry::addRecipe);

        // Alchemy Bench Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.ALCHEMY_BENCH.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(ALCHEMY_BENCH_CATEGORY, recipe))
                .forEach(registry::addRecipe);

        // Tinkering Table Recipes
        flatten(manager.listAllOfType(ModRecipeTypes.TINKERING_TABLE.get()))
                .stream()
                .map(recipe -> new ChippedEmiRecipe(TINKERING_TABLE_CATEGORY, recipe))
                .forEach(registry::addRecipe);

    }

    public static class ChippedEmiRecipeCategory extends EmiRecipeCategory {
        private final Block block;

        public ChippedEmiRecipeCategory(com.teamresourceful.resourcefullib.common.registry.RegistryEntry<Block> block) {
            super(block.getId(), EmiStack.of(block.get()));
            this.block = block.get();
        }

        @Override
        public Text getName() {
            return block.getName();
        }
    }
}
