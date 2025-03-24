package com.kneelawk.exmi.isns.recipe;

import java.util.List;
import java.util.stream.Stream;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.kneelawk.exmi.isns.ISNSIntegration;

public class ScrollForgeEmiRecipe extends BasicEmiRecipe {
    private final EmiIngredient ink;
    private final EmiIngredient paper;
    private final EmiIngredient catalyst;
    private final EmiStack output;

    public static Stream<ScrollForgeEmiRecipe> getRecipes() {
        List<InkItem> inkItems =
            BuiltInRegistries.ITEM.stream().filter(item -> item instanceof InkItem).map(item -> (InkItem) item)
                .toList();
        return BuiltInRegistries.ITEM.stream().filter(item -> item.builtInRegistryHolder().is(ModTags.SCHOOL_FOCUS))
            .flatMap(focusItem -> {
                EmiStack paper = EmiStack.of(Items.PAPER);
                ItemStack focus = new ItemStack(focusItem);
                SchoolType school = SchoolRegistry.getSchoolFromFocus(focus);
                List<AbstractSpell> spells = SpellRegistry.getSpellsForSchool(school);
                return inkItems.stream().flatMap(inkItem -> spells.stream().filter(
                    spell -> spell != SpellRegistry.none() && spell.isEnabled() && spell.allowCrafting() &&
                        spell.getMinLevelForRarity(inkItem.getRarity()) > 0).map(spell -> {
                    EmiStack inkStack = EmiStack.of(inkItem);
                    ItemStack scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
                    int level = spell.getMinLevelForRarity(inkItem.getRarity());
                    ISpellContainer.createScrollContainer(spell, level, scrollStack);
                    return new ScrollForgeEmiRecipe(
                        IronsSpellbooks.id("/scroll_forge/" + spell.getSpellId().replace(':', '/') + "/" + inkItem.getRarity().getValue()),
                        inkStack, paper, EmiStack.of(focus), EmiStack.of(scrollStack));
                }));
            });
    }

    public ScrollForgeEmiRecipe(ResourceLocation id, EmiIngredient ink, EmiIngredient paper, EmiIngredient catalyst,
                                EmiStack output) {
        super(ISNSIntegration.SCROLL_FORGE, id, 18 * 3 + 5 * 2, 18 + 11 + 20);
        this.ink = ink;
        this.paper = paper;
        this.catalyst = catalyst;
        this.output = output;

        inputs = List.of(ink, paper, catalyst);
        outputs = List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(IronsSpellbooks.id("textures/gui/scroll_forge.png"), 0, 0, 18 * 3 + 5 * 2, 18 + 11 + 20, 11,
            16);

        widgets.addSlot(ink, 0, 0).drawBack(false);
        widgets.addSlot(paper, 18 + 5, 0).drawBack(false);
        widgets.addSlot(catalyst, 18 * 2 + 5 * 2, 0).drawBack(false);

        widgets.addSlot(output, 18 + 5, 18 + 12).drawBack(false).recipeContext(this);
    }
}
