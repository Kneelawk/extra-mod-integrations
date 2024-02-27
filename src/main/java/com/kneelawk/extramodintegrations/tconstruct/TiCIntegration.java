package com.kneelawk.extramodintegrations.tconstruct;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import com.kneelawk.extramodintegrations.AbstractTiCIntegration;
import com.kneelawk.extramodintegrations.tconstruct.recipe.AlloyEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.MoldingEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.casting.CastingBasinEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.casting.CastingTableEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.entity.SeveringEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.melting.EntityMeltingEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.melting.FoundryEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.melting.MeltingEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.modifiers.ModifierEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.modifiers.ModifierWorktableEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.recipe.partbuilder.PartBuilderEmiRecipe;
import com.kneelawk.extramodintegrations.tconstruct.stack.ModifierEmiStack;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
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
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.plugin.jei.entity.DefaultEntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;
import slimeknights.tconstruct.plugin.jei.partbuilder.MaterialItemList;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.data.SmelteryCompat;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TiCIntegration extends AbstractTiCIntegration {
    @Override
    protected void registerImpl(EmiRegistry registry) {
        registerCategories(registry);
        registerComparisons(registry);
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
        registry.addWorkstation(TiCCategories.SEVERING, EmiIngredient.of(TinkerTags.Items.MELEE));
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
        optionalItem(registry, TinkerMaterials.necroniumBone, "uranium_ingots");
    }

    private static void registerRecipes(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();
        // casting
        RecipeHelper.getJEIRecipes(manager.listAllOfType(TinkerRecipeTypes.CASTING_BASIN.get()).stream(), IDisplayableCastingRecipe.class)
                .stream()
                .map(CastingBasinEmiRecipe::new)
                .forEach(registry::addRecipe);
        RecipeHelper.getJEIRecipes(manager.listAllOfType(TinkerRecipeTypes.CASTING_TABLE.get()).stream(), IDisplayableCastingRecipe.class)
                .stream()
                .map(CastingTableEmiRecipe::new)
                .forEach(registry::addRecipe);
        // melting
        List<MeltingRecipe> meltingRecipes = RecipeHelper.getJEIRecipes(manager.listAllOfType(TinkerRecipeTypes.MELTING.get()).stream(), MeltingRecipe.class);
        meltingRecipes.forEach(meltingRecipe -> registry.addRecipe(new MeltingEmiRecipe(meltingRecipe)));
        meltingRecipes.forEach(meltingRecipe -> registry.addRecipe(new FoundryEmiRecipe(meltingRecipe)));
        MeltingFuelHandler.setMeltngFuels(RecipeHelper.getRecipes(manager, TinkerRecipeTypes.FUEL.get(), MeltingFuel.class));

        // entity melting
        List<EntityMeltingRecipe> entityMeltingRecipes = manager.listAllOfType(TinkerRecipeTypes.ENTITY_MELTING.get());
        // generate a "default" recipe for all other entity types
        Streams.concat(entityMeltingRecipes.stream(), Stream.of(new DefaultEntityMeltingRecipe(entityMeltingRecipes)))
                .forEach(entityMeltingRecipe -> registry.addRecipe(new EntityMeltingEmiRecipe(entityMeltingRecipe)));

        // alloying
        manager.listAllOfType(TinkerRecipeTypes.ALLOYING.get())
                .forEach(alloyRecipe -> registry.addRecipe(new AlloyEmiRecipe(alloyRecipe)));

        // molding
        List<MoldingRecipe> moldingRecipes = ImmutableList.<MoldingRecipe>builder()
                .addAll(manager.listAllOfType(TinkerRecipeTypes.MOLDING_TABLE.get()))
                .addAll(manager.listAllOfType(TinkerRecipeTypes.MOLDING_BASIN.get()))
                .build();
        moldingRecipes.forEach(moldingRecipe -> registry.addRecipe(new MoldingEmiRecipe(moldingRecipe)));

        // modifiers
        RecipeHelper.getJEIRecipes(manager.listAllOfType(TinkerRecipeTypes.TINKER_STATION.get()).stream(), IDisplayModifierRecipe.class)
                .forEach(modifierRecipe -> registry.addRecipe(new ModifierEmiRecipe(modifierRecipe)));

        // beheading
        manager.listAllOfType(TinkerRecipeTypes.SEVERING.get())
                .forEach(severingRecipe -> registry.addRecipe(new SeveringEmiRecipe(severingRecipe)));

        // part builder
        List<MaterialRecipe> materialRecipes = manager.listAllOfType(TinkerRecipeTypes.MATERIAL.get());
        MaterialItemList.setRecipes(materialRecipes);
        manager.listAllOfType(TinkerRecipeTypes.PART_BUILDER.get())
                .forEach(partRecipe -> registry.addRecipe(new PartBuilderEmiRecipe(partRecipe)));

        // modifier worktable
        manager.listAllOfType(TinkerRecipeTypes.MODIFIER_WORKTABLE.get())
                .forEach(iModifierWorktableRecipe -> registry.addRecipe(new ModifierWorktableEmiRecipe(iModifierWorktableRecipe)));
    }

    private static void registerComparisons(EmiRegistry registry) {
        final Comparison compareToolMaterials = Comparison.compareData(s -> MaterialIdNBT.from(s.getItemStack()));
        final Comparison comparePartMaterial = Comparison.compareData(s -> IMaterialItem.getMaterialFromStack(s.getItemStack()));

        getTag(TinkerTags.Items.TOOL_PARTS)
                .forEach(i -> registry.setDefaultComparison(i.value(), comparePartMaterial));
        registry.setDefaultComparison(TinkerTools.slimesuit.get(ArmorSlotType.HELMET), compareToolMaterials);
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
