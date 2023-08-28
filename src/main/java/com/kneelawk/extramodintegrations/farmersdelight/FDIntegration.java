package com.kneelawk.extramodintegrations.farmersdelight;

import com.kneelawk.extramodintegrations.AbstractFDIntegration;
import com.kneelawk.extramodintegrations.ExMIMod;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.registry.BlocksRegistry;
import com.nhoryzon.mc.farmersdelight.registry.ExtendedScreenTypesRegistry;
import com.nhoryzon.mc.farmersdelight.registry.RecipeTypesRegistry;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class FDIntegration extends AbstractFDIntegration {
    public static EmiRecipeCategory COOKING_CATEGORY = new EmiRecipeCategory(
            new Identifier("farmersdelight", "cooking"),
            EmiStack.of(BlocksRegistry.COOKING_POT.get())
    ) {
        @Override
        public Text getName() {
            return Text.translatable("farmersdelight.rei.cooking");
        }
    };
    public static EmiRecipeCategory CUTTING_CATEGORY = new EmiRecipeCategory(
            new Identifier("farmersdelight", "cutting"),
            EmiStack.of(BlocksRegistry.CUTTING_BOARD.get())
    ) {
        @Override
        public Text getName() {
            return Text.translatable("farmersdelight.rei.cutting");
        }
    };
    public static EmiRecipeCategory DECOMPOSITION_CATEGORY = new EmiRecipeCategory(
            new Identifier("farmersdelight", "decomposition"),
            EmiStack.of(BlocksRegistry.RICH_SOIL.get())
    ) {
        @Override
        public Text getName() {
            return Text.translatable("farmersdelight.rei.decomposition");
        }
    };

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading Farmer's Delight Integration...");

        // categories
        registry.addCategory(COOKING_CATEGORY);
        registry.addCategory(CUTTING_CATEGORY);
        registry.addCategory(DECOMPOSITION_CATEGORY);

        // workstations
        registry.addWorkstation(COOKING_CATEGORY, EmiStack.of(BlocksRegistry.COOKING_POT.get()));
        registry.addWorkstation(CUTTING_CATEGORY, EmiStack.of(BlocksRegistry.CUTTING_BOARD.get()));
        registry.addWorkstation(DECOMPOSITION_CATEGORY, EmiStack.of(BlocksRegistry.RICH_SOIL.get()));

        // recipes
        RecipeManager manager = registry.getRecipeManager();
        manager.listAllOfType(RecipeTypesRegistry.COOKING_RECIPE_SERIALIZER.type())
                .stream()
                .map(CookingPotRecipe.class::cast)
                .map(CookingPotEmiRecipe::new)
                .forEach(registry::addRecipe);
        manager.listAllOfType(RecipeTypesRegistry.CUTTING_RECIPE_SERIALIZER.type())
                .stream()
                .map(CuttingBoardRecipe.class::cast)
                .map(CuttingEmiRecipe::new)
                .forEach(registry::addRecipe);
        registry.addRecipe(new DecompositionEmiRecipe(
                EmiStack.of(BlocksRegistry.ORGANIC_COMPOST.get()),
                EmiIngredient.of(TagsRegistry.COMPOST_ACTIVATORS),
                EmiStack.of(BlocksRegistry.RICH_SOIL.get())
        ));

        // recipe handlers
        registry.addRecipeHandler(ExtendedScreenTypesRegistry.COOKING_POT.get(), new CookingPotRecipeHandler());
    }
}
