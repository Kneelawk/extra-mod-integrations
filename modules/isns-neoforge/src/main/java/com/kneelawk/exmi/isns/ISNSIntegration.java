package com.kneelawk.exmi.isns;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.core.api.SimpleRecipeHandler;
import com.kneelawk.exmi.isns.recipe.AlchemistCauldronEmiRecipe;
import com.kneelawk.exmi.isns.recipe.ArcaneAnvilEmiRecipe;
import com.kneelawk.exmi.isns.recipe.ScrollForgeEmiRecipe;

public class ISNSIntegration implements ExMIPlugin {
    public static final EmiStack SCROLL_FORGE_BLOCK = EmiStack.of(BlockRegistry.SCROLL_FORGE_BLOCK.get());
    public static final EmiStack ARCANE_ANVIL_BLOCK = EmiStack.of(BlockRegistry.ARCANE_ANVIL_BLOCK.get());
    public static final EmiStack ALCHEMIST_CAULDRON_BLOCK = EmiStack.of(BlockRegistry.ALCHEMIST_CAULDRON.get());
    public static final EmiStack AFFINITY_RING = EmiStack.of(ItemRegistry.AFFINITY_RING.get());

    public static final EmiRecipeCategory SCROLL_FORGE =
        new EmiRecipeCategory(IronsSpellbooks.id("scroll_forge"), SCROLL_FORGE_BLOCK);
    public static final EmiRecipeCategory ARCANE_ANVIL =
        new EmiRecipeCategory(IronsSpellbooks.id("arcane_anvil"), ARCANE_ANVIL_BLOCK);
    public static final EmiRecipeCategory ALCHEMIST_CAULDRON =
        new EmiRecipeCategory(IronsSpellbooks.id("alchemist_cauldron"), ALCHEMIST_CAULDRON_BLOCK);

    @Override
    public void register(EmiRegistry registry) {
        registry.setDefaultComparison(EmiStack.of(ItemRegistry.SCROLL.get()), Comparison.compareComponents());
        registry.setDefaultComparison(AFFINITY_RING, Comparison.compareComponents());
        registry.setDefaultComparison(EmiStack.of(ItemRegistry.UPGRADE_ORB.get()), Comparison.compareComponents());

        registry.addCategory(SCROLL_FORGE);
        registry.addWorkstation(SCROLL_FORGE, SCROLL_FORGE_BLOCK);
        ScrollForgeEmiRecipe.getRecipes().forEach(registry::addRecipe);

        registry.addCategory(ARCANE_ANVIL);
        registry.addWorkstation(ARCANE_ANVIL, ARCANE_ANVIL_BLOCK);
        ArcaneAnvilEmiRecipe.getRecipes().forEach(registry::addRecipe);

        registry.addCategory(ALCHEMIST_CAULDRON);
        registry.addWorkstation(ALCHEMIST_CAULDRON, ALCHEMIST_CAULDRON_BLOCK);
        AlchemistCauldronEmiRecipe.getRecipes(registry.getRecipeManager()).forEach(registry::addRecipe);

        SpellRegistry.getEnabledSpells().forEach(spell -> {
            if (spell.isEnabled() && spell != SpellRegistry.none()) {
                // add items to emi side-bar
                ItemStack newRing = new ItemStack(ItemRegistry.AFFINITY_RING.get());
                newRing.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(spell));
                EmiStack ringStack = EmiStack.of(newRing);
                registry.addEmiStack(ringStack);

                // add scroll info
                List<EmiIngredient> scrolls =
                    IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).mapToObj(level -> {
                        ItemStack scroll = new ItemStack(ItemRegistry.SCROLL.get());
                        ISpellContainer.createScrollContainer(spell, level, scroll);
                        return (EmiIngredient) EmiStack.of(scroll);
                    }).toList();
                registry.addRecipe(
                    new EmiInfoRecipe(scrolls, List.of(Component.translatable(spell.getComponentId() + ".guide")),
                        IronsSpellbooks.id("/spell_info/" + spell.getSpellId().replace(':', '/'))));
            }
        });

        registry.addRecipeHandler(MenuRegistry.ARCANE_ANVIL_MENU.get(),
            new SimpleRecipeHandler<>(ARCANE_ANVIL, 0, 2, 3, 36, OptionalInt.of(2)));

        addInfo(registry, ItemRegistry.LIGHTNING_BOTTLE, "lightning_bottle");
        addInfo(registry, ItemRegistry.BLOOD_VIAL, "blood_vial");
        addInfo(registry, ItemRegistry.FROZEN_BONE_SHARD, "frozen_bone");
        addInfo(registry, ItemRegistry.HOGSKIN, "hogskin");
        addInfo(registry, ItemRegistry.DRAGONSKIN, "dragonskin");
        addInfo(registry, ItemRegistry.RUINED_BOOK, "ruined_book");
        addInfo(registry, ItemRegistry.CINDER_ESSENCE, "cinder_essence");
        addInfo(registry, ItemRegistry.LIGHTNING_ROD_STAFF, "lightning_rod");
    }

    private static void addInfo(EmiRegistry registry, Supplier<Item> item, String name) {
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(item.get())),
            List.of(Component.translatable("item.irons_spellbooks." + name + ".guide")),
            IronsSpellbooks.id("/info/" + name)));
    }
}
