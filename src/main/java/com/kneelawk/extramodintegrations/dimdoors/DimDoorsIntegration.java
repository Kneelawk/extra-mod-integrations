package com.kneelawk.extramodintegrations.dimdoors;

import com.kneelawk.extramodintegrations.AbstractDimDoorsIntegration;
import com.kneelawk.extramodintegrations.dimdoors.handler.TesselatingLoomRecipeHandler;
import com.kneelawk.extramodintegrations.dimdoors.recipe.DecaysIntoEmiRecipe;
import com.kneelawk.extramodintegrations.dimdoors.recipe.ShapedTesselatingEmiRecipe;
import com.kneelawk.extramodintegrations.dimdoors.recipe.ShapelessTesselatingEmiRecipe;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.dimdev.dimdoors.block.ModBlocks;
import org.dimdev.dimdoors.recipe.ModRecipeTypes;
import org.dimdev.dimdoors.screen.ModScreenHandlerTypes;
import org.dimdev.dimdoors.world.decay.Decay;

public class DimDoorsIntegration extends AbstractDimDoorsIntegration {
    @Override
    protected void registerImpl(EmiRegistry registry) {
        registry.addCategory(DimDoorsCategories.TESSELATING);
        registry.addCategory(DimDoorsCategories.DECAYS_INTO);

        registry.addWorkstation(DimDoorsCategories.TESSELATING, EmiStack.of(ModBlocks.TESSELATING_LOOM.get()));
        registry.addWorkstation(DimDoorsCategories.DECAYS_INTO, EmiStack.of(ModBlocks.UNRAVELLED_FABRIC.get()));

        RecipeManager manager = registry.getRecipeManager();

        manager.getAllRecipesFor(ModRecipeTypes.SHAPED_TESSELATING.get())
                .stream()
                .map(ShapedTesselatingEmiRecipe::new)
                .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ModRecipeTypes.SHAPELESS_TESSELATING.get())
                .stream()
                .map(ShapelessTesselatingEmiRecipe::new)
                .forEach(registry::addRecipe);
        Decay.DecayLoader.getInstance().getBlockPatterns().forEach((block, patterns) ->
                registry.addRecipe(new DecaysIntoEmiRecipe<>(block, patterns)));

        registry.addRecipeHandler(ModScreenHandlerTypes.TESSELATING_LOOM.get(), new TesselatingLoomRecipeHandler());
    }
}
