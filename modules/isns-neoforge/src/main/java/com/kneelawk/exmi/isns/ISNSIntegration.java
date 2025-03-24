package com.kneelawk.exmi.isns;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;

import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.isns.recipe.ArcaneAnvilEmiRecipe;

public class ISNSIntegration implements ExMIPlugin {
    public static final EmiStack SCROLL_FORGE_BLOCK = EmiStack.of(BlockRegistry.SCROLL_FORGE_BLOCK.get());
    public static final EmiStack ARCANE_ANVIL_BLOCK = EmiStack.of(BlockRegistry.ARCANE_ANVIL_BLOCK.get());
    public static final EmiStack ALCHEMIST_CAULDRON_BLOCK = EmiStack.of(BlockRegistry.ALCHEMIST_CAULDRON.get());

    public static final EmiRecipeCategory SCROLL_FORGE =
        new EmiRecipeCategory(IronsSpellbooks.id("scroll_forge"), SCROLL_FORGE_BLOCK);
    public static final EmiRecipeCategory ARCANE_ANVIL =
        new EmiRecipeCategory(IronsSpellbooks.id("arcane_anvil"), ARCANE_ANVIL_BLOCK);
    public static final EmiRecipeCategory ALCHEMIST_CAULDRON =
        new EmiRecipeCategory(IronsSpellbooks.id("alchemist_cauldron"), ALCHEMIST_CAULDRON_BLOCK);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(SCROLL_FORGE);
        registry.addWorkstation(SCROLL_FORGE, SCROLL_FORGE_BLOCK);

        registry.addCategory(ARCANE_ANVIL);
        registry.addWorkstation(ARCANE_ANVIL, ARCANE_ANVIL_BLOCK);
        ArcaneAnvilEmiRecipe.getRecipes().forEach(registry::addRecipe);

        registry.addCategory(ALCHEMIST_CAULDRON);
        registry.addWorkstation(ALCHEMIST_CAULDRON, ALCHEMIST_CAULDRON_BLOCK);
        
        registry.setDefaultComparison(EmiStack.of(ItemRegistry.SCROLL.get()), Comparison.compareComponents());
        registry.setDefaultComparison(EmiStack.of(ItemRegistry.AFFINITY_RING.get()), Comparison.compareComponents());
        registry.setDefaultComparison(EmiStack.of(ItemRegistry.UPGRADE_ORB.get()), Comparison.compareComponents());
    }
}
