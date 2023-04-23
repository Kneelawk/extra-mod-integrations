package com.kneelawk.extramodintegrations.hephaestus;

import com.google.common.collect.ImmutableList;
import com.kneelawk.extramodintegrations.AbstractHephaestusIntegration;
import com.kneelawk.extramodintegrations.hephaestus.casting.CastingBasinEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.casting.CastingTableEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.entity.EntityMeltingEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.entity.SeveringEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.melting.FoundryEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.melting.MeltingEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.modifiers.ModifierEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.modifiers.ModifierEmiStack;
import com.kneelawk.extramodintegrations.hephaestus.modifiers.ModifierWorktableEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.partbuilder.PartBuilderEmiRecipe;
import com.kneelawk.extramodintegrations.hephaestus.transfer.*;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.EmiStackProvider;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import dev.emi.emi.api.widget.Bounds;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.common.accessor.RecipeManagerAccessor;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.block.Oxidizable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import slimeknights.mantle.client.screen.MultiModuleScreen;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.IDisplayPartBuilderRecipe;
import slimeknights.tconstruct.library.recipe.worktable.IModifierWorktableRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.plugin.jei.entity.DefaultEntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;
import slimeknights.tconstruct.plugin.jei.partbuilder.MaterialItemList;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.client.screen.HeatingStructureScreen;
import slimeknights.tconstruct.smeltery.client.screen.IScreenWithFluidTank;
import slimeknights.tconstruct.smeltery.client.screen.MelterScreen;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.CreativeSlotItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class HephaestusIntegration extends AbstractHephaestusIntegration {
  public static final EmiRecipeCategory SEVERING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("severing"),
      EmiStack.of(TinkerTools.cleaver.get().getRenderTool()));
  public static final EmiRecipeCategory MODIFIER_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("modifiers"),
      EmiStack.of(CreativeSlotItem.withSlot(new ItemStack(TinkerModifiers.creativeSlotItem), SlotType.UPGRADE)));
  public static final EmiRecipeCategory CASTING_BASIN_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("casting_basin"),
      EmiStack.of(TinkerSmeltery.searedBasin));
  public static final EmiRecipeCategory CASTING_TABLE_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("casting_table"),
      EmiStack.of(TinkerSmeltery.searedTable));
  public static final EmiRecipeCategory MOLDING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("molding"),
      EmiStack.of(TinkerSmeltery.blankSandCast));
  public static final EmiRecipeCategory MELTING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("melting"),
      EmiStack.of(TinkerSmeltery.searedMelter));
  public static final EmiRecipeCategory FOUNDRY_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("foundry"),
      EmiStack.of(TinkerSmeltery.foundryController));
  public static final EmiRecipeCategory ALLOY_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("alloying"),
      EmiStack.of(TinkerSmeltery.smelteryController));
  public static final EmiRecipeCategory ENTITY_MELTING_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("entity_melting"),
      EntityMeltingEmiRecipe.icon);
  public static final EmiRecipeCategory PART_BUILDER_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("part_builder"),
      EmiStack.of(TinkerTables.partBuilder));
  public static final EmiRecipeCategory MODIFIER_WORKTABLE_CATEGORY =
    new EmiRecipeCategory(TConstruct.getResource("modifier_worktable"),
      EmiStack.of(TinkerTables.modifierWorktable));

  @Override
  public void registerImpl(EmiRegistry registry) {
    RecipeManager manager = registry.getRecipeManager();

    // ========== CATEGORIES ==========
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
    // modifier worktable
    registry.addCategory(MODIFIER_WORKTABLE_CATEGORY);

    // ========== INGREDIENTS ==========
    List<ModifierEntry> modifiers = Config.CLIENT.showModifiersInJEI.get()
            ? ModifierRecipeLookup.getRecipeModifierList()
            : Collections.emptyList();
    modifiers.forEach(m -> registry.addEmiStack(new ModifierEmiStack(m)));

    // ========== WORKSTATIONS ==========
    // tables
    registry.addWorkstation(PART_BUILDER_CATEGORY, EmiStack.of(TinkerTables.partBuilder));
    registry.addWorkstation(MODIFIER_CATEGORY, EmiStack.of(TinkerTables.tinkerStation));
    registry.addWorkstation(MODIFIER_CATEGORY, EmiStack.of(TinkerTables.tinkersAnvil));
    registry.addWorkstation(MODIFIER_CATEGORY, EmiStack.of(TinkerTables.scorchedAnvil));
    registry.addWorkstation(MODIFIER_WORKTABLE_CATEGORY, EmiStack.of(TinkerTables.modifierWorktable));

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
    for (RegistryEntry<Item> item : Objects.requireNonNull(Registries.ITEM.iterateEntries(TinkerTags.Items.MELEE))) {
      // add any tools with a severing trait
      if (item instanceof IModifiable modifiable && modifiable.getToolDefinition().getData().getTraits().stream().anyMatch(entry -> entry.matches(TinkerModifiers.severing.getId()))) {
        registry.addWorkstation(SEVERING_CATEGORY, EmiStack.of(IModifiableDisplay.getDisplayStack(item.value())));
      }
    }
    registry.addWorkstation(SEVERING_CATEGORY, new ModifierEmiStack(new ModifierEntry(TinkerModifiers.severing, 1)));
    registry.addWorkstation(MELTING_CATEGORY, new ModifierEmiStack(new ModifierEntry(TinkerModifiers.melting, 1)));
    registry.addWorkstation(ENTITY_MELTING_CATEGORY, new ModifierEmiStack(new ModifierEntry(TinkerModifiers.melting, 1)));

    // ========== RECIPES ==========
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

    // modifier worktable
    //     register.addRecipes(TConstructJEIConstants.MODIFIER_WORKTABLE, RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.MODIFIER_WORKTABLE.get(), IModifierWorktableRecipe.class));
    RecipeHelper.getJEIRecipes(manager, TinkerRecipeTypes.MODIFIER_WORKTABLE.get(), IModifierWorktableRecipe.class)
            .forEach(recipe -> registry.addRecipe(new ModifierWorktableEmiRecipe(recipe)));


    // add world interaction recipes for waxing and stripping copper platforms
    for (Oxidizable.OxidationLevel state : Oxidizable.OxidationLevel.values()) {
      String stateId = state == Oxidizable.OxidationLevel.UNAFFECTED ? ""
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
        .rightInput(EmiIngredient.of(ItemTags.AXES), true)
        .output(EmiStack.of(TinkerCommons.copperPlatform.get(state)))
        .build());
    }

    // ========== EXCLUSION AREAS ==========
    // TODO: find a better way of doing this than just catching the npe
    registry.addGenericExclusionArea((screen, consumer) -> {
      if (screen instanceof MultiModuleScreen<?> multiModuleScreen)
        try {
          multiModuleScreen.getModuleAreas()
                  .stream()
                  .map(r -> new Bounds(r.getX(), r.getY(), r.getWidth(), r.getHeight()))
                  .forEach(consumer);
        } catch (NullPointerException ignored) {}
    });

    // ========== RECIPE HANDLERS ==========
    registry.addRecipeHandler(TinkerTables.craftingStationContainer.get(), new CraftingStationRecipeHandler());
    registry.addRecipeHandler(TinkerTables.tinkerStationContainer.get(), new TinkerStationRecipeHandler());
    registry.addRecipeHandler(TinkerTables.partBuilderContainer.get(), new PartBuilderRecipeHandler());
    registry.addRecipeHandler(TinkerSmeltery.smelteryContainer.get(), new SmelteryRecipeHandler());
    registry.addRecipeHandler(TinkerSmeltery.melterContainer.get(), new MelterRecipeHandler());

    // ========== COMPARISONS ==========
    Function<Comparison, Comparison> compareNbt = c -> c.copy().nbt(true).build();

    // retexturable blocks
    // these should be a tag or something
//    registry.setDefaultComparison(TinkerTables.craftingStation.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerTables.partBuilder.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerTables.tinkerStation.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerTables.tinkersAnvil.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerTables.modifierWorktable.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerTables.scorchedAnvil.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.smelteryController.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.searedDrain.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.searedDuct.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.searedChute.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.foundryController.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.scorchedDrain.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.scorchedDuct.asItem(), compareNbt);
//    registry.setDefaultComparison(TinkerSmeltery.scorchedChute.asItem(), compareNbt);

    // tool parts
    Registries.ITEM.iterateEntries(TinkerTags.Items.TOOL_PARTS)
            .forEach(i -> registry.setDefaultComparison(i.value(), compareNbt));

    // tools
    Registries.ITEM.iterateEntries(TinkerTags.Items.MULTIPART_TOOL)
            .forEach(i -> registry.setDefaultComparison(i.value(), compareNbt));

    // tanks
    Registries.ITEM.iterateEntries(TinkerTags.Items.TANKS)
            .forEach(i -> registry.setDefaultComparison(i.value(), compareNbt));

    // misc
    registry.setDefaultComparison(TinkerSmeltery.copperCan.get(), compareNbt);
    registry.setDefaultComparison(TinkerFluids.potionBucket.asItem(), compareNbt);
    registry.setDefaultComparison(TinkerModifiers.creativeSlotItem.get(), compareNbt);
    registry.setDefaultComparison(TinkerModifiers.modifierCrystal.get(), compareNbt);

    // ========== STACK PROVIDERS ==========
    registry.addStackProvider(MelterScreen.class, new TankStackProvider<>());
    registry.addStackProvider(HeatingStructureScreen.class, new TankStackProvider<>());
  }

  private static class TankStackProvider<T extends Screen & IScreenWithFluidTank> implements EmiStackProvider<T> {
    @Override
    public EmiStackInteraction getStackAt(T screen, int mouseX, int mouseY) {
      if (screen.getIngredientUnderMouse(mouseX, mouseY) instanceof FluidStack f) {
        return new EmiStackInteraction(EmiStack.of(f.getFluid(), f.getAmount()));
      } else return EmiStackInteraction.EMPTY;
    }
  }

  private static <T extends Recipe<C>, C extends Inventory> void addCastingCatalyst(EmiRegistry registry, RecipeManager manager, EmiRecipeCategory ownCategory, EmiIngredient workstation, RecipeType<MoldingRecipe> type) {
    registry.addWorkstation(ownCategory, workstation);
    if (!((RecipeManagerAccessor)manager).port_lib$byType(type).isEmpty()) {
      registry.addWorkstation(MOLDING_CATEGORY, workstation);
    }
  }
}
