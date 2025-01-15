package com.kneelawk.exmi.actuallyadditions;

import java.util.Arrays;
import java.util.Optional;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.inventory.ActuallyContainers;
import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiCoffeeMachine;
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems;
import de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy;
import de.ellpeck.actuallyadditions.mod.util.CapHelper;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;

import com.kneelawk.exmi.actuallyadditions.handler.CoffeeMachineHandler;
import com.kneelawk.exmi.actuallyadditions.handler.CrusherHandler;
import com.kneelawk.exmi.actuallyadditions.handler.PoweredFurnaceHandler;
import com.kneelawk.exmi.actuallyadditions.recipe.CoffeeMachineEmiRecipe;
import com.kneelawk.exmi.actuallyadditions.recipe.CrushingEmiRecipe;
import com.kneelawk.exmi.actuallyadditions.recipe.EmpoweringEmiRecipe;
import com.kneelawk.exmi.actuallyadditions.recipe.FermentingEmiRecipe;
import com.kneelawk.exmi.actuallyadditions.recipe.LaserEmiRecipe;
import com.kneelawk.exmi.actuallyadditions.recipe.MiningLensEmiRecipe;
import com.kneelawk.exmi.actuallyadditions.recipe.PressingEmiRecipe;
import com.kneelawk.exmi.core.api.ExMIPlugin;

import static de.ellpeck.actuallyadditions.mod.ActuallyAdditions.modLoc;

public class AAIntegration implements ExMIPlugin {
    public static final EmiRecipeCategory FERMENTING =
        new EmiRecipeCategory(modLoc("fermenting"), EmiStack.of(ActuallyBlocks.FERMENTING_BARREL.getItem()));
    public static final EmiRecipeCategory PRESSING =
        new EmiRecipeCategory(modLoc("pressing"), EmiStack.of(ActuallyBlocks.CANOLA_PRESS.getItem()));
    public static final EmiRecipeCategory LASER =
        new EmiRecipeCategory(modLoc("laser"), EmiStack.of(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.getItem()));
    public static final EmiRecipeCategory EMPOWERER =
        new EmiRecipeCategory(modLoc("empowerer"), EmiStack.of(ActuallyBlocks.EMPOWERER.getItem()));
    public static final EmiRecipeCategory COFFEE_MACHINE =
        new EmiRecipeCategory(modLoc("coffee_machine"), EmiStack.of(ActuallyBlocks.COFFEE_MACHINE.getItem()));
    public static final EmiRecipeCategory CRUSHING =
        new EmiRecipeCategory(modLoc("crushing"), EmiStack.of(ActuallyBlocks.CRUSHER.getItem()));
    public static final EmiRecipeCategory MINING_LENS =
        new EmiRecipeCategory(modLoc("mining_lens"), EmiStack.of(ActuallyItems.LENS_OF_THE_MINER.get()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(COFFEE_MACHINE);
        registry.addCategory(CRUSHING);
        registry.addCategory(EMPOWERER);
        registry.addCategory(FERMENTING);
        registry.addCategory(LASER);
        registry.addCategory(MINING_LENS);
        registry.addCategory(PRESSING);

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(ActuallyItems.CRAFTER_ON_A_STICK));
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING,
            EmiStack.of(ActuallyBlocks.POWERED_FURNACE.getItem()));
        registry.addWorkstation(FERMENTING, EmiStack.of(ActuallyBlocks.FERMENTING_BARREL.getItem()));
        registry.addWorkstation(LASER, EmiStack.of(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.getItem()));
        registry.addWorkstation(EMPOWERER, EmiStack.of(ActuallyBlocks.EMPOWERER.getItem()));
        registry.addWorkstation(COFFEE_MACHINE, EmiStack.of(ActuallyBlocks.COFFEE_MACHINE.getItem()));
        registry.addWorkstation(PRESSING, EmiStack.of(ActuallyBlocks.CANOLA_PRESS.getItem()));
        registry.addWorkstation(CRUSHING, EmiStack.of(ActuallyBlocks.CRUSHER.getItem()));
        registry.addWorkstation(CRUSHING, EmiStack.of(ActuallyBlocks.CRUSHER_DOUBLE.getItem()));
        registry.addWorkstation(MINING_LENS, EmiStack.of(ActuallyItems.LENS_OF_THE_MINER.get()));

        RecipeManager manager = registry.getRecipeManager();
        manager.getAllRecipesFor(ActuallyRecipes.Types.FERMENTING.get())
            .stream()
            .map(FermentingEmiRecipe::new)
            .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ActuallyRecipes.Types.LASER.get())
            .stream()
            .map(LaserEmiRecipe::new)
            .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ActuallyRecipes.Types.EMPOWERING.get())
            .stream()
            .map(EmpoweringEmiRecipe::new)
            .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ActuallyRecipes.Types.COFFEE_INGREDIENT.get())
            .stream()
            .map(CoffeeMachineEmiRecipe::new)
            .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ActuallyRecipes.Types.PRESSING.get())
            .stream()
            .map(PressingEmiRecipe::new)
            .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ActuallyRecipes.Types.CRUSHING.get())
            .stream()
            .map(CrushingEmiRecipe::new)
            .forEach(registry::addRecipe);
        manager.getAllRecipesFor(ActuallyRecipes.Types.MINING_LENS.get())
            .stream()
            .map(MiningLensEmiRecipe::new)
            .forEach(registry::addRecipe);

        Comparison energyComparison = Comparison.compareData(stack ->
            CapHelper.getEnergyStorage(stack.getItemStack())
                .flatMap(storage ->
                    storage.getEnergyStored() == storage.getMaxEnergyStored() ? Optional.of("charged") :
                        Optional.of("uncharged")
                ).orElse("uncharged")
        );
        ActuallyItems.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.get();
            if (item instanceof ItemEnergy) {
                registry.setDefaultComparison(item, energyComparison);
            }
        });
        registry.setDefaultComparison(ActuallyItems.COFFEE_CUP.get(), Comparison.compareData(stack -> {
            MobEffectInstance[] effects = ActuallyAdditionsAPI.methodHandler.getEffectsFromStack(stack.getItemStack());
            if (effects == null) return null;
            return Arrays.asList(effects);
        }));

        registry.addExclusionArea(GuiCoffeeMachine.class, (screen, consumer) -> {
            consumer.accept(new Bounds(screen.getGuiLeft() - 30, screen.getGuiTop() + 1, 26, 93));
        });

        registry.addRecipeHandler(ActuallyContainers.COFFEE_MACHINE_CONTAINER.get(), new CoffeeMachineHandler());
        registry.addRecipeHandler(ActuallyContainers.FURNACE_DOUBLE_CONTAINER.get(), new PoweredFurnaceHandler());
        registry.addRecipeHandler(ActuallyContainers.GRINDER_CONTAINER.get(), new CrusherHandler());
    }
}
