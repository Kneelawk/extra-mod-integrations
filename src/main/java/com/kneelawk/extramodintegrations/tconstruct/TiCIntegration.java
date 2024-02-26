package com.kneelawk.extramodintegrations.tconstruct;

import com.google.common.collect.Iterables;
import com.kneelawk.extramodintegrations.AbstractTiCIntegration;
import com.kneelawk.extramodintegrations.tconstruct.stack.ModifierEmiStack;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.data.SmelteryCompat;
import slimeknights.tconstruct.tables.TinkerTables;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TiCIntegration extends AbstractTiCIntegration {
    @Override
    protected void registerImpl(EmiRegistry registry) {
        registerCategories(registry);
        registerEntries(registry);
        registerRecipes(registry);
    }

    private static void registerCategories(EmiRegistry registry) {
        // casting
        registry.addCategory(TiCCategories.CASTING_BASIN);
        registry.addCategory(TiCCategories.CASTING_TABLE);
        registry.addCategory(TiCCategories.MOLDING);
        // melting and casting
        registry.addCategory(TiCCategories.MELTING);
        registry.addCategory(TiCCategories.ALLOY);
        registry.addCategory(TiCCategories.ENTITY_MELTING);
        registry.addCategory(TiCCategories.FOUNDRY);
        // tinker station
        registry.addCategory(TiCCategories.MODIFIERS);
        registry.addCategory(TiCCategories.SEVERING);
        // part builder
        registry.addCategory(TiCCategories.PART_BUILDER);
        // modifier worktable
        registry.addCategory(TiCCategories.MODIFIER_WORKTABLE);

        // tables
        registry.addWorkstation(TiCCategories.PART_BUILDER, EmiStack.of(TinkerTables.partBuilder));
        registry.addWorkstation(TiCCategories.MODIFIERS, EmiStack.of(TinkerTables.tinkerStation));
        registry.addWorkstation(TiCCategories.MODIFIERS, EmiStack.of(TinkerTables.tinkersAnvil));
        registry.addWorkstation(TiCCategories.MODIFIERS, EmiStack.of(TinkerTables.scorchedAnvil));
        registry.addWorkstation(TiCCategories.MODIFIER_WORKTABLE, EmiStack.of(TinkerTables.modifierWorktable));

        // smeltery
        registry.addWorkstation(TiCCategories.MELTING, EmiStack.of(TinkerSmeltery.searedMelter));
        registry.addWorkstation(VanillaEmiRecipeCategories.FUEL, EmiStack.of(TinkerSmeltery.searedHeater));
        addCastingCatalyst(registry, TinkerSmeltery.searedTable, TiCCategories.CASTING_TABLE, TinkerRecipeTypes.MOLDING_TABLE.get());
        addCastingCatalyst(registry, TinkerSmeltery.searedBasin, TiCCategories.CASTING_BASIN, TinkerRecipeTypes.MOLDING_BASIN.get());
        addCatalysts(registry, EmiStack.of(TinkerSmeltery.smelteryController), TiCCategories.MELTING, TiCCategories.ALLOY, TiCCategories.ENTITY_MELTING);

        // foundry
        registry.addWorkstation(TiCCategories.ALLOY, EmiStack.of(TinkerSmeltery.scorchedAlloyer));
        addCastingCatalyst(registry, TinkerSmeltery.scorchedTable, TiCCategories.CASTING_TABLE, TinkerRecipeTypes.MOLDING_TABLE.get());
        addCastingCatalyst(registry, TinkerSmeltery.scorchedBasin, TiCCategories.CASTING_BASIN, TinkerRecipeTypes.MOLDING_BASIN.get());
        registry.addWorkstation(TiCCategories.FOUNDRY, EmiStack.of(TinkerSmeltery.foundryController));

        // modifiers
        for (RegistryEntry<Item> item : Objects.requireNonNull(Registries.ITEM.iterateEntries(TinkerTags.Items.MELEE))) {
            registry.addWorkstation(TiCCategories.SEVERING, EmiStack.of(IModifiableDisplay.getDisplayStack(item.value())));
        }
    }

    private static void registerEntries(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();
        List<ModifierEntry> modifiers = Collections.emptyList();
        if (Config.CLIENT.showModifiersInJEI.get()) {
            modifiers = RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.TINKER_STATION.get(), IDisplayModifierRecipe.class)
                    .stream()
                    .map(recipe -> recipe.getDisplayResult().getModifier())
                    .distinct()
                    .sorted(Comparator.comparing(Modifier::getId))
                    .map(mod -> new ModifierEntry(mod, 1))
                    .collect(Collectors.toList());
        }

        modifiers.forEach(entry -> registry.addEmiStack(new ModifierEmiStack(entry)));

        // hide knightslime and slimesteel until implemented
        removeFluid(registry, TinkerFluids.moltenSoulsteel.get(), TinkerFluids.moltenSoulsteel.asItem());
        removeFluid(registry, TinkerFluids.moltenKnightslime.get(), TinkerFluids.moltenKnightslime.asItem());
        // hide compat that is not present
        for (SmelteryCompat compat : SmelteryCompat.values()) {
            Iterable<RegistryEntry<Item>> ingot = getTag(new Identifier("c", compat.getName() + "_ingots"));
            if (Iterables.isEmpty(ingot)) {
                removeFluid(registry, compat.getFluid().get(), compat.getBucket());
            }
        }
        if (!FabricLoader.getInstance().isModLoaded("ceramics")) {
            removeFluid(registry, TinkerFluids.moltenPorcelain.get(), TinkerFluids.moltenPorcelain.asItem());
        }
        optionalCast(registry, TinkerSmeltery.plateCast);
        optionalCast(registry, TinkerSmeltery.gearCast);
        optionalCast(registry, TinkerSmeltery.coinCast);
        optionalCast(registry, TinkerSmeltery.wireCast);
        optionalItem(registry, TinkerMaterials.necroniumBone, "uranium_ingots");
    }

    private static void registerRecipes(EmiRegistry registry) {
        // todo
    }

    private static void removeFluid(EmiRegistry manager, Fluid fluid, Item bucket) {
        manager.removeEmiStacks(EmiStack.of(fluid));
        manager.removeEmiStacks(EmiStack.of(bucket));
    }

    private static Iterable<RegistryEntry<Item>> getTag(Identifier name) {
        return getTag(TagKey.of(RegistryKeys.ITEM, name));
    }

    private static Iterable<RegistryEntry<Item>> getTag(TagKey<Item> name) {
        return Objects.requireNonNull(Registries.ITEM.iterateEntries(name));
    }

    private static void optionalItem(EmiRegistry manager, ItemConvertible item, String tagName) {
        Iterable<RegistryEntry<Item>> tag = getTag(new Identifier("c", tagName));
        if (Iterables.isEmpty(tag)) {
            manager.removeEmiStacks(EmiStack.of(item));
        }
    }

    private static void optionalCast(EmiRegistry manager, CastItemObject cast) {
        Iterable<RegistryEntry<Item>> tag = getTag(new Identifier("c", cast.getName().getPath() + "_blocks"));
        if (Iterables.isEmpty(tag)) {
            cast.values().stream().map(EmiStack::of).forEach(manager::addEmiStack);
        }
    }

    private static <T extends Recipe<C>, C extends Inventory> void addCastingCatalyst(EmiRegistry registry, ItemConvertible item, EmiRecipeCategory ownCategory, RecipeType<MoldingRecipe> type) {
        EmiStack stack = EmiStack.of(item);
        registry.addWorkstation(ownCategory, stack);
        if (!registry.getRecipeManager().listAllOfType(type).isEmpty()) {
            registry.addWorkstation(TiCCategories.MOLDING, stack);
        }
    }

    private static void addCatalysts(EmiRegistry registry, EmiStack entryStack, EmiRecipeCategory ...categoryIdentifiers) {
        for (EmiRecipeCategory categoryIdentifier : categoryIdentifiers)
            registry.addWorkstation(categoryIdentifier, entryStack);
    }

}
