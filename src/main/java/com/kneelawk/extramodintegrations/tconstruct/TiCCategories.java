package com.kneelawk.extramodintegrations.tconstruct;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.CreativeSlotItem;

public class TiCCategories {
    // casting
    public static final EmiRecipeCategory CASTING_BASIN = new EmiRecipeCategory(
            TConstruct.getResource("casting_basin"),
            EmiStack.of(TinkerSmeltery.searedBasin)
    );
    public static final EmiRecipeCategory CASTING_TABLE = new EmiRecipeCategory(
            TConstruct.getResource("casting_table"),
            EmiStack.of(TinkerSmeltery.searedTable)
    );
    public static final EmiRecipeCategory MOLDING = new EmiRecipeCategory(
            TConstruct.getResource("molding"),
            EmiStack.of(TinkerSmeltery.blankSandCast)
    );
    // melting and casting
    public static final EmiRecipeCategory MELTING = new EmiRecipeCategory(
            TConstruct.getResource("melting"),
            EmiStack.of(TinkerSmeltery.searedMelter)
    );
    public static final EmiRecipeCategory ALLOY = new EmiRecipeCategory(
            TConstruct.getResource("alloy"),
            EmiStack.of(TinkerSmeltery.smelteryController)
    );
    public static final EmiRecipeCategory ENTITY_MELTING = new EmiRecipeCategory(
            TConstruct.getResource("entity_melting"),
            new EmiTexture(TConstruct.getResource("textures/gui/jei/melting.png"), 174, 41, 16, 16)
    );
    public static final EmiRecipeCategory FOUNDRY = new EmiRecipeCategory(
            TConstruct.getResource("foundry"),
            EmiStack.of(TinkerSmeltery.foundryController)
    );
    // tinker station
    public static final EmiRecipeCategory MODIFIER = new EmiRecipeCategory(
            TConstruct.getResource("modifier"),
            EmiStack.of(CreativeSlotItem.withSlot(new ItemStack(TinkerModifiers.creativeSlotItem), SlotType.UPGRADE))
    );
    public static final EmiRecipeCategory SEVERING = new EmiRecipeCategory(
            TConstruct.getResource("severing"),
            EmiStack.of(TinkerTools.cleaver.get().getRenderTool())
    );
    // part builder
    public static final EmiRecipeCategory PART_BUILDER = new EmiRecipeCategory(
            TConstruct.getResource("part_builder"),
            EmiStack.of(TinkerTables.partBuilder)
    );
    // modifier worktable
    public static final EmiRecipeCategory MODIFIER_WORKTABLE = new EmiRecipeCategory(
            TConstruct.getResource("modifier_worktable"),
            EmiStack.of(TinkerTables.modifierWorktable)
    );
}
