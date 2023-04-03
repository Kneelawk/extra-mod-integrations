package com.kneelawk.extramodintegrations.hephaestus;

import com.google.common.collect.ImmutableList;
import com.kneelawk.extramodintegrations.hephaestus.casting.CastingBasinEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.casting.CastingTableEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.entity.EntityMeltingEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.entity.SeveringEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.melting.FoundryEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.melting.MeltingEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.modifiers.ModifierEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.modifiers.ModifierEmiStack;
import com.kneelawk.extramodintegrations.hephaestus.partbuilder.PartBuilderEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.transfer.*;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import io.github.fabricators_of_create.porting_lib.mixin.common.accessor.RecipeManagerAccessor;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.WeatheringCopper;
import slimeknights.mantle.client.screen.MultiModuleScreen;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.IDisplayPartBuilderRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.plugin.jei.entity.DefaultEntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;
import slimeknights.tconstruct.plugin.jei.partbuilder.MaterialItemList;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.CreativeSlotItem;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class HephaestusIntegrationImpl extends HephaestusIntegration {
  public static final EmiRecipeCategory SEVERING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("severing"),
      EmiStack.of(TinkerTools.cleaver.get().getRenderTool()));
  public static final EmiRecipeCategory MODIFIER_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("modifiers"),
      EmiStack.of(CreativeSlotItem.withSlot(new ItemStack(TinkerModifiers.creativeSlotItem), SlotType.UPGRADE)));
  public static final EmiRecipeCategory CASTING_BASIN_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("casting_basin"),
      EmiStack.of(TinkerSmeltery.searedBasin.get()));
  public static final EmiRecipeCategory CASTING_TABLE_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("casting_table"),
      EmiStack.of(TinkerSmeltery.searedTable.get()));
  public static final EmiRecipeCategory MOLDING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("molding"),
      EmiStack.of(TinkerSmeltery.blankSandCast.get()));
  public static final EmiRecipeCategory MELTING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("melting"),
      EmiStack.of(TinkerSmeltery.searedMelter.get()));
  public static final EmiRecipeCategory FOUNDRY_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("foundry"),
      EmiStack.of(TinkerSmeltery.foundryController.get()));
  public static final EmiRecipeCategory ALLOY_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("alloying"),
      EmiStack.of(TinkerSmeltery.smelteryController.get()));
  public static final EmiRecipeCategory ENTITY_MELTING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("entity_melting"),
      EntityMeltingEmiRecipe.icon);
  public static final EmiRecipeCategory PART_BUILDER_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("part_builder"),
      EmiStack.of(TinkerTables.partBuilder.get()));

  @Override
  public void registerImpl(EmiRegistry registry) {
    RecipeManager manager = registry.getRecipeManager();

    // categories
    // casting
    registry.addCategory(CASTING_BASIN_CATEGORY);
    registry.addCategory(CASTING_TABLE_CATEGORY);
    registry.addCategory(MOLDING_CATEGORY);
    // melting and casting
    registry.addCategory(MELTING_CATEGORY);
    registry.addCategory(ALLOY_CATEGORY);
    registry.addCategory(ENTITY_MELTING_CATEGORY);
    registry.addCategory(FOUNDRY_CATEGORY);
    // tinker station
    registry.addCategory(MODIFIER_CATEGORY);
    registry.addCategory(SEVERING_CATEGORY);
    // part builder
    registry.addCategory(PART_BUILDER_CATEGORY);

    // ingredients
    if (Config.CLIENT.showModifiersInJEI.get()) {
      RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.TINKER_STATION.get(), IDisplayModifierRecipe.class)
        .stream()
        .map(recipe -> recipe.getDisplayResult().getModifier())
        .distinct()
        .sorted(Comparator.comparing(Modifier::getId))
        .map(mod -> new ModifierEntry(mod, 1))
        .forEach(mod -> registry.addEmiStack(new ModifierEmiStack(mod)));
    }

    // workstations
    // tables
    registry.addWorkstation(PART_BUILDER_CATEGORY, EmiStack.of(TinkerTables.partBuilder.get()));
    registry.addWorkstation(MODIFIER_CATEGORY, EmiStack.of(TinkerTables.tinkerStation.get()));
    registry.addWorkstation(MODIFIER_CATEGORY, EmiStack.of(TinkerTables.tinkersAnvil.get()));
    registry.addWorkstation(MODIFIER_CATEGORY, EmiStack.of(TinkerTables.scorchedAnvil.get()));

    // smeltery
    registry.addWorkstation(MELTING_CATEGORY, EmiStack.of(TinkerSmeltery.searedMelter.get()));
//    registry.addWorkstation(FUELING, EmiStack.of(TinkerSmeltery.searedHeater.get()));
    addCastingCatalyst(registry, manager, CASTING_TABLE_CATEGORY, EmiStack.of(TinkerSmeltery.searedTable.get()), TinkerRecipeTypes.MOLDING_TABLE.get());
    addCastingCatalyst(registry, manager, CASTING_BASIN_CATEGORY, EmiStack.of(TinkerSmeltery.searedBasin.get()), TinkerRecipeTypes.MOLDING_BASIN.get());
    registry.addWorkstation(MELTING_CATEGORY, EmiStack.of(TinkerSmeltery.smelteryController.get()));
    registry.addWorkstation(ALLOY_CATEGORY, EmiStack.of(TinkerSmeltery.smelteryController.get()));
    registry.addWorkstation(ENTITY_MELTING_CATEGORY, EmiStack.of(TinkerSmeltery.smelteryController.get()));

    // foundry
    registry.addWorkstation(ALLOY_CATEGORY, EmiStack.of(TinkerSmeltery.scorchedAlloyer.get()));
    addCastingCatalyst(registry, manager, CASTING_TABLE_CATEGORY, EmiStack.of(TinkerSmeltery.scorchedTable.get()), TinkerRecipeTypes.MOLDING_TABLE.get());
    addCastingCatalyst(registry, manager, CASTING_BASIN_CATEGORY, EmiStack.of(TinkerSmeltery.scorchedBasin.get()), TinkerRecipeTypes.MOLDING_BASIN.get());
    registry.addWorkstation(FOUNDRY_CATEGORY, EmiStack.of(TinkerSmeltery.foundryController.get()));

    // modifiers
    for (Holder<Item> item : Objects.requireNonNull(Registry.ITEM.getTagOrEmpty(TinkerTags.Items.MELEE))) {
      // add any tools with a severing trait
      if (item instanceof IModifiable modifiable && modifiable.getToolDefinition().getData().getTraits().stream().anyMatch(entry -> entry.matches(TinkerModifiers.severing.getId()))) {
        registry.addWorkstation(SEVERING_CATEGORY, EmiStack.of(IModifiableDisplay.getDisplayStack(item.value())));
      }
    }
    registry.addWorkstation(SEVERING_CATEGORY, new ModifierEmiStack(new ModifierEntry(TinkerModifiers.severing, 1)));
    registry.addWorkstation(MELTING_CATEGORY, new ModifierEmiStack(new ModifierEntry(TinkerModifiers.melting, 1)));
    registry.addWorkstation(ENTITY_MELTING_CATEGORY, new ModifierEmiStack(new ModifierEntry(TinkerModifiers.melting, 1)));

    // recipes
    // casting
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.CASTING_BASIN.get(), IDisplayableCastingRecipe.class)
        .forEach(recipe -> registry.addRecipe(new CastingBasinEmiRecipe(recipe)));
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.CASTING_TABLE.get(), IDisplayableCastingRecipe.class)
        .forEach(recipe -> registry.addRecipe(new CastingTableEmiRecipe(recipe)));

    // melting
    List<MeltingRecipe> meltingRecipes = RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.MELTING.get(), MeltingRecipe.class);
    meltingRecipes.forEach(recipe -> registry.addRecipe(new MeltingEmiRecipe(recipe)));
    meltingRecipes.forEach(recipe -> registry.addRecipe(new FoundryEmiRecipe(recipe)));
    MeltingFuelHandler.setMeltngFuels(RecipeHelper.getRecipes(manager, TinkerRecipeTypes.FUEL.get(), MeltingFuel.class));

    // entity melting
    List<EntityMeltingRecipe> entityMeltingRecipes = RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.ENTITY_MELTING.get(), EntityMeltingRecipe.class);
    // generate a "default" recipe for all other entity types
    entityMeltingRecipes.add(new DefaultEntityMeltingRecipe(entityMeltingRecipes));
    entityMeltingRecipes.forEach(recipe -> registry.addRecipe(new EntityMeltingEmiRecipe(recipe)));

    // alloying
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.ALLOYING.get(), AlloyRecipe.class)
        .forEach(recipe -> registry.addRecipe(new AlloyEmiRecipe(recipe)));

    // molding
    ImmutableList.<MoldingRecipe>builder()
      .addAll(RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.MOLDING_TABLE.get(), MoldingRecipe.class))
      .addAll(RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.MOLDING_BASIN.get(), MoldingRecipe.class))
      .build()
      .forEach(recipe -> registry.addRecipe(new MoldingEmiRecipe(recipe)));

    // modifiers
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.TINKER_STATION.get(), IDisplayModifierRecipe.class)
      .stream()
      .sorted((r1, r2) -> {
        SlotType t1 = r1.getSlotType();
        SlotType t2 = r2.getSlotType();
        String n1 = t1 == null ? "zzzzzzzzzz" : t1.getName();
        String n2 = t2 == null ? "zzzzzzzzzz" : t2.getName();
        return n1.compareTo(n2);
      }).forEach(recipe -> registry.addRecipe(new ModifierEmiRecipe(recipe)));

    // beheading
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.SEVERING.get(), SeveringRecipe.class)
      .forEach(severingRecipe -> registry.addRecipe(new SeveringEmiRecipe(severingRecipe)));

    // part builder
    List<MaterialRecipe> materialRecipes = RecipeHelper.getRecipes(manager, TinkerRecipeTypes.MATERIAL.get(), MaterialRecipe.class);
    MaterialItemList.setRecipes(materialRecipes);
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.PART_BUILDER.get(), IDisplayPartBuilderRecipe.class)
      .forEach(recipe -> registry.addRecipe(new PartBuilderEmiRecipe(recipe)));

    // add world interaction recipes for waxing and stripping copper platforms
    for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values()) {
      String stateId = state == WeatheringCopper.WeatherState.UNAFFECTED ? ""
        : state.toString().toLowerCase()+"_";

      registry.addRecipe(EmiWorldInteractionRecipe.builder()
        .id(TConstruct.getResource("waxing/"+stateId+"copper_platform"))
        .leftInput(EmiStack.of(TinkerCommons.copperPlatform.get(state)))
        .rightInput(EmiStack.of(Items.HONEYCOMB), false)
        .output(EmiStack.of(TinkerCommons.waxedCopperPlatform.get(state)))
        .build());
      registry.addRecipe(EmiWorldInteractionRecipe.builder()
          .id(TConstruct.getResource("stripping/"+stateId+"copper_platform"))
        .leftInput(EmiStack.of(TinkerCommons.waxedCopperPlatform.get(state)))
        .rightInput(EmiIngredient.of(ConventionalItemTags.AXES), true)
        .output(EmiStack.of(TinkerCommons.copperPlatform.get(state)))
        .build());
    }

    // exclusion areas
    registry.addGenericExclusionArea((screen, consumer) -> {
      if (screen instanceof MultiModuleScreen<?> multiModuleScreen)
        multiModuleScreen.getModuleAreas()
          .stream()
          .map(r -> new Bounds(r.getX(), r.getY(), r.getWidth(), r.getHeight()))
          .forEach(consumer);
    });

    // recipe handlers
    registry.addRecipeHandler(TinkerTables.craftingStationContainer.get(), new CraftingStationRecipeHandler());
    registry.addRecipeHandler(TinkerTables.tinkerStationContainer.get(), new TinkerStationRecipeHandler());
    registry.addRecipeHandler(TinkerTables.partBuilderContainer.get(), new PartBuilderRecipeHandler());
    registry.addRecipeHandler(TinkerSmeltery.smelteryContainer.get(), new SmelteryRecipeHandler());
    registry.addRecipeHandler(TinkerSmeltery.melterContainer.get(), new MelterRecipeHandler());

    // comparisons
    Function<Comparison, Comparison> compareNbt = c -> c.copy().nbt(true).build();

    // tool parts
    registry.setDefaultComparison(TinkerToolParts.repairKit.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.pickHead.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.hammerHead.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.smallAxeHead.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.broadAxeHead.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.smallBlade.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.broadBlade.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.roundPlate.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.largePlate.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.toolBinding.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.toolHandle.get(), compareNbt);
    registry.setDefaultComparison(TinkerToolParts.toughHandle.get(), compareNbt);

    // tools
    registry.setDefaultComparison(TinkerTools.pickaxe.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.sledgeHammer.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.veinHammer.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.mattock.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.pickadze.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.excavator.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.handAxe.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.broadAxe.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.kama.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.scythe.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.dagger.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.sword.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.cleaver.get(), compareNbt);
    registry.setDefaultComparison(TinkerTools.flintAndBrick.get(), compareNbt);
  }

  private static <T extends Recipe<C>, C extends Container> void addCastingCatalyst(EmiRegistry registry, RecipeManager manager, EmiRecipeCategory ownCategory, EmiIngredient workstation, RecipeType<MoldingRecipe> type) {
    registry.addWorkstation(ownCategory, workstation);
    if (!((RecipeManagerAccessor)manager).port_lib$byType(type).isEmpty()) {
      registry.addWorkstation(MOLDING_CATEGORY, workstation);
    }
  }
}
