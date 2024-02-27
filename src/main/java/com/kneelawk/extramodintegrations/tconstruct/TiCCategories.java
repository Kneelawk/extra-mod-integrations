package com.kneelawk.extramodintegrations.tconstruct;

import com.kneelawk.extramodintegrations.util.NamedEmiRecipeCategory;
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
    public static final EmiRecipeCategory CASTING_BASIN = new NamedEmiRecipeCategory(
            TConstruct.getResource("casting_basin"),
            EmiStack.of(TinkerSmeltery.searedBasin),
            TConstruct.makeTranslation("jei", "casting.basin")
    );
    public static final EmiRecipeCategory CASTING_TABLE = new NamedEmiRecipeCategory(
            TConstruct.getResource("casting_table"),
            EmiStack.of(TinkerSmeltery.searedTable),
            TConstruct.makeTranslation("jei", "casting.table")
    );
    public static final EmiRecipeCategory MOLDING = new NamedEmiRecipeCategory(
            TConstruct.getResource("molding"),
            EmiStack.of(TinkerSmeltery.blankSandCast),
            TConstruct.makeTranslation("jei", "molding.title")
    );
    // melting and casting
    public static final EmiRecipeCategory MELTING = new NamedEmiRecipeCategory(
            TConstruct.getResource("melting"),
            EmiStack.of(TinkerSmeltery.searedMelter),
            TConstruct.makeTranslation("jei", "melting.title")
    );
    public static final EmiRecipeCategory ALLOY = new NamedEmiRecipeCategory(
            TConstruct.getResource("alloy"),
            EmiStack.of(TinkerSmeltery.smelteryController),
            TConstruct.makeTranslation("jei", "alloy.title")
    );
    public static final EmiRecipeCategory ENTITY_MELTING = new NamedEmiRecipeCategory(
            TConstruct.getResource("entity_melting"),
            new EmiTexture(TConstruct.getResource("textures/gui/jei/melting.png"), 174, 41, 16, 16),
            TConstruct.makeTranslation("jei", "entity_melting.title")
    );
    public static final EmiRecipeCategory FOUNDRY = new NamedEmiRecipeCategory(
            TConstruct.getResource("foundry"),
            EmiStack.of(TinkerSmeltery.foundryController),
            TConstruct.makeTranslation("jei", "foundry.title")
    );
    // tinker station
    public static final EmiRecipeCategory MODIFIERS = new NamedEmiRecipeCategory(
            TConstruct.getResource("modifier"),
            EmiStack.of(CreativeSlotItem.withSlot(new ItemStack(TinkerModifiers.creativeSlotItem), SlotType.UPGRADE)),
            TConstruct.makeTranslation("jei", "modifiers.title")
    );
    public static final EmiRecipeCategory SEVERING = new NamedEmiRecipeCategory(
            TConstruct.getResource("severing"),
            EmiStack.of(TinkerTools.cleaver.get().getRenderTool()),
            TConstruct.makeTranslation("jei", "severing.title")
    );
    // part builder
    public static final EmiRecipeCategory PART_BUILDER = new NamedEmiRecipeCategory(
            TConstruct.getResource("part_builder"),
            EmiStack.of(TinkerTables.partBuilder),
            TConstruct.makeTranslation("jei", "part_builder.title")
    );
    // modifier worktable
    public static final EmiRecipeCategory MODIFIER_WORKTABLE = new NamedEmiRecipeCategory(
            TConstruct.getResource("modifier_worktable"),
            EmiStack.of(TinkerTables.modifierWorktable),
            TConstruct.makeTranslation("jei", "modifier_worktable.title")
    );
}
