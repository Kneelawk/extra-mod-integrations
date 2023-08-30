package com.kneelawk.extramodintegrations.conjuring;

import com.glisco.conjuring.Conjuring;
import com.glisco.conjuring.blocks.ConjuringBlocks;
import com.glisco.conjuring.blocks.gem_tinkerer.GemTinkererRecipe;
import com.glisco.conjuring.blocks.soul_weaver.SoulWeaverRecipe;
import com.glisco.conjuring.blocks.soulfire_forge.SoulfireForgeRecipe;
import com.glisco.conjuring.items.ConjuringItems;
import com.kneelawk.extramodintegrations.AbstractConjuringIntegration;
import com.kneelawk.extramodintegrations.ExMIMod;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class ConjuringIntegration extends AbstractConjuringIntegration {
    public static final EmiRecipeCategory GEM_TINKERING = new EmiRecipeCategory(
            new Identifier("conjuring", "gem_tinkering"),
            EmiStack.of(ConjuringBlocks.GEM_TINKERER)
    ) {
        @Override
        public Text getName() {
            return Text.translatable("conjuring.gui.gem_tinkerer");
        }
    };

    public static final EmiRecipeCategory SOUL_WEAVING = new EmiRecipeCategory(
            new Identifier("conjuring", "soul_weaving"),
            EmiStack.of(ConjuringBlocks.SOUL_WEAVER)
    ) {
        @Override
        public Text getName() {
            return Text.translatable("conjuring.gui.soul_weaver");
        }
    };

    public static final EmiRecipeCategory SOULFIRE_FORGE = new EmiRecipeCategory(
            new Identifier("conjuring", "soulfire_forge"),
            EmiStack.of(ConjuringBlocks.SOULFIRE_FORGE)
    ) {
        @Override
        public Text getName() {
            return Text.translatable("conjuring.gui.soulfire_forge");
        }
    };

    @Override
    protected void registerImpl(EmiRegistry registry) {
        ExMIMod.LOGGER.info("[Extra Mod Integrations] Loading Conjuring Integration...");

        // categories
        registry.addCategory(GEM_TINKERING);
        registry.addCategory(SOUL_WEAVING);
        registry.addCategory(SOULFIRE_FORGE);

        // workstations
        registry.addWorkstation(SOULFIRE_FORGE, EmiStack.of(ConjuringBlocks.SOULFIRE_FORGE));
        registry.addWorkstation(GEM_TINKERING, EmiStack.of(ConjuringBlocks.GEM_TINKERER));
        registry.addWorkstation(SOUL_WEAVING, EmiStack.of(ConjuringBlocks.SOUL_WEAVER));
        registry.addWorkstation(SOUL_WEAVING, EmiStack.of(ConjuringBlocks.BLACKSTONE_PEDESTAL));

        // recipes
        RecipeManager manager = registry.getRecipeManager();
        manager.listAllOfType(SoulfireForgeRecipe.Type.INSTANCE)
                .stream()
                .filter(r -> r.getOutput(null).getItem() != ConjuringItems.PIZZA)
                .map(SoulfireForgeEmiRecipe::new)
                .forEach(registry::addRecipe);
        manager.listAllOfType(GemTinkererRecipe.Type.INSTANCE)
                .stream()
                .filter(r -> r.getOutput(null).getItem() != Items.COOKIE)
                .map(GemTinkeringEmiRecipe::new)
                .forEach(registry::addRecipe);
        manager.listAllOfType(SoulWeaverRecipe.Type.INSTANCE)
                .stream()
                .map(SoulWeavingEmiRecipe::new)
                .forEach(registry::addRecipe);

        // recipe handlers
        registry.addRecipeHandler(Conjuring.SOULFIRE_FORGE_SCREEN_HANDLER_TYPE, new SoulfireForgeRecipeHandler());

        // stacks
        registry.removeEmiStacks(EmiStack.of(ConjuringItems.PIZZA));
    }
}
