package com.kneelawk.exmi.chipped;

import java.util.List;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import earth.terrarium.chipped.Chipped;
import earth.terrarium.chipped.common.registry.ModBlocks;
import earth.terrarium.chipped.common.registry.ModRecipeTypes;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import com.kneelawk.exmi.core.api.ExMIPlugin;

public class CIntegration implements ExMIPlugin {
    private static final EmiStack BOTANIST_WORKBENCH = EmiStack.of(ModBlocks.BOTANIST_WORKBENCH.get());
    private static final EmiStack GLASSBLOWER = EmiStack.of(ModBlocks.GLASSBLOWER.get());
    private static final EmiStack CARPENTERS_TABLE = EmiStack.of(ModBlocks.CARPENTERS_TABLE.get());
    private static final EmiStack LOOM_TABLE = EmiStack.of(ModBlocks.LOOM_TABLE.get());
    private static final EmiStack MASON_TABLE = EmiStack.of(ModBlocks.MASON_TABLE.get());
    private static final EmiStack ALCHEMY_BENCH = EmiStack.of(ModBlocks.ALCHEMY_BENCH.get());
    private static final EmiStack TINKERING_TABLE = EmiStack.of(ModBlocks.TINKERING_TABLE.get());

    public static final EmiRecipeCategory WORKBENCH =
        new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Chipped.MOD_ID, "workbench"), BOTANIST_WORKBENCH);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(WORKBENCH);

        registry.addWorkstation(WORKBENCH, BOTANIST_WORKBENCH);
        registry.addWorkstation(WORKBENCH, GLASSBLOWER);
        registry.addWorkstation(WORKBENCH, CARPENTERS_TABLE);
        registry.addWorkstation(WORKBENCH, LOOM_TABLE);
        registry.addWorkstation(WORKBENCH, MASON_TABLE);
        registry.addWorkstation(WORKBENCH, ALCHEMY_BENCH);
        registry.addWorkstation(WORKBENCH, TINKERING_TABLE);

        Registry<Item> items = BuiltInRegistries.ITEM;
        for (var recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.WORKBENCH.get())) {
            List<Ingredient> ingredients = recipeHolder.value().ingredients();
            int ingredientsLen = ingredients.size();
            for (int i = 0; i < ingredientsLen; i++) {
                Ingredient ingredient = ingredients.get(i);
                for (ItemStack stack : ingredient.getItems()) {
                    ResourceLocation key = items.getKey(stack.getItem());
                    ResourceLocation id =
                        recipeHolder.id().withPrefix("/")
                            .withSuffix("/" + i + "/" + key.getNamespace() + "/" + key.getPath());
                    registry.addRecipe(new ChippedEmiRecipe(ingredient, stack, id));
                }
            }
        }
    }
}
