package com.kneelawk.exmi.isns.recipe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import org.jetbrains.annotations.NotNull;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRecipeRegistry;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.CauldronPlatformHelper;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import com.kneelawk.exmi.core.api.ExMILog;
import com.kneelawk.exmi.isns.ISNSIntegration;

public class AlchemistCauldronEmiRecipe extends BasicEmiRecipe {
    private final boolean recycle;
    private final EmiIngredient input;
    private final EmiIngredient bottle;
    private final EmiStack output;

    public static Stream<AlchemistCauldronEmiRecipe> getRecipes() {
        List<ItemStack> visibleItems = getVisibleItems();
        return Streams.concat(getScrollRecipes(), getCustomRecipes(visibleItems), getPotionRecipes(visibleItems));
    }

    private static Stream<AlchemistCauldronEmiRecipe> getScrollRecipes() {
        return Arrays.stream(SpellRarity.values()).map(AlchemistCauldronEmiRecipe::recycleRecipe);
    }

    private static Stream<AlchemistCauldronEmiRecipe> getCustomRecipes(List<ItemStack> visibleItems) {
        List<ItemStack> reagents =
            visibleItems.stream().filter(AlchemistCauldronRecipeRegistry::isValidIngredient).toList();
        return reagents.stream().flatMap(reagent -> AlchemistCauldronRecipeRegistry.getRecipes().stream()
            .filter(recipe -> CauldronPlatformHelper.itemMatches(reagent, recipe.getIngredient())).map(recipe -> {
                ItemStack result = recipe.getResult();
                if (result.getCount() == 4) {
                    result.setCount(1);
                }

                ResourceLocation reagentKey = getKey(reagent);
                ResourceLocation inputKey = getKey(recipe.getInput());
                ResourceLocation resultKey = getKey(result);

                return new AlchemistCauldronEmiRecipe(IronsSpellbooks.id(
                    "/custom_cauldron_recipe/" + reagentKey.getNamespace() + "/" + reagentKey.getPath() + "/" +
                        inputKey.getNamespace() + "/" + inputKey.getPath() + "/" + resultKey.getNamespace() + "/" +
                        resultKey.getPath()), false, EmiStack.of(reagent), EmiStack.of(recipe.getInput()),
                    EmiStack.of(result));
            }));
    }

    private static Stream<AlchemistCauldronEmiRecipe> getPotionRecipes(List<ItemStack> visibleItems) {
        if (!ServerConfigs.ALLOW_CAULDRON_BREWING.get()) {
            return Stream.of();
        } else {
            List<ItemStack> reagents = visibleItems.stream().filter(AlchemistCauldronEmiRecipe::isIngredient).toList();
            List<ItemStack> potions = getPotionItems();
            ClientLevel level = Minecraft.getInstance().level;

            return level == null ? Stream.of() : reagents.stream().flatMap(
                reagent -> potions.stream().filter(potion -> level.potionBrewing().hasMix(potion, reagent))
                    .map(baseItem -> {
                        ResourceLocation reagentKey = getKey(reagent);
                        ResourceLocation baseKey = getKey(baseItem);
                        ItemStack mix = level.potionBrewing().mix(reagent, baseItem);
                        return new AlchemistCauldronEmiRecipe(IronsSpellbooks.id(
                            "/potion_brewing/" + reagentKey.getNamespace() + "/" + reagentKey.getPath() + "/" +
                                baseKey.getNamespace() + "/" + baseKey.getPath() + "/" +
                                getPotionContentsPath(baseItem) + getPotionContentsPath(mix)), false,
                            EmiStack.of(reagent), EmiStack.of(baseItem), EmiStack.of(mix));
                    }));
        }
    }

    private static String getPotionContentsPath(ItemStack potion) {
        PotionContents potionContents = potion.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) return "";

        StringBuilder builder = new StringBuilder();
        Optional<Holder<Potion>> optionalPotion = potionContents.potion();
        if (optionalPotion.isPresent()) {
            Holder<Potion> potionHolder = optionalPotion.get();
            Optional<ResourceKey<Potion>> optionalKey = potionHolder.unwrapKey();
            if (optionalKey.isPresent()) {
                ResourceLocation value = optionalKey.get().location();
                builder.append(value.getNamespace()).append('/').append(value.getPath()).append('/');
            } else {
                builder.append("unknown_not_registered_potion/");
            }
        } else {
            for (var instance : potionContents.getAllEffects()) {
                Holder<MobEffect> effect = instance.getEffect();
                Optional<ResourceKey<MobEffect>> optionalKey = effect.unwrapKey();
                if (optionalKey.isPresent()) {
                    ResourceLocation value = optionalKey.get().location();
                    builder.append(value.getNamespace()).append('/').append(value.getPath()).append('/');
                } else {
                    builder.append("unknown_not_registered/");
                }
                builder.append(instance.getDuration()).append('/').append(instance.getAmplifier()).append('/');
            }
        }
        return builder.toString();
    }

    private static AlchemistCauldronEmiRecipe recycleRecipe(SpellRarity rarity) {
        ItemStack scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
        EmiIngredient scrolls = EmiIngredient.of(SpellRegistry.getEnabledSpells().stream().flatMap(
            spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel())
                .filter(level -> spell.getRarity(level) == rarity)
                .mapToObj(level -> EmiStack.of(getScrollStack(scrollStack, spell, level)))).toList());
        EmiStack ink = EmiStack.of(InkItem.getInkForRarity(rarity))
            .setChance(ServerConfigs.SCROLL_RECYCLE_CHANCE.get().floatValue());
        EmiStack waterBottle = EmiStack.of(Items.POTION,
            DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
                .build());
        return new AlchemistCauldronEmiRecipe(IronsSpellbooks.id("/scroll_recycle/" + rarity.getValue()), true, scrolls,
            waterBottle, ink);
    }

    private static ItemStack getScrollStack(ItemStack stack, AbstractSpell spell, int level) {
        ItemStack scrollStack = stack.copy();
        ISpellContainer.createScrollContainer(spell, level, scrollStack);
        return scrollStack;
    }

    private static boolean isIngredient(ItemStack stack) {
        try {
            return Minecraft.getInstance().level.potionBrewing().isIngredient(stack);
        } catch (LinkageError | RuntimeException e) {
            ExMILog.LOG.error("Failed to check if item is a potion reagent {}.", stack, e);
            return false;
        }
    }

    private static @NotNull ResourceLocation getKey(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    private static List<ItemStack> getVisibleItems() {
        return BuiltInRegistries.ITEM.stream().map(ItemStack::new)
            .filter(stack -> CreativeModeTabs.allTabs().stream().anyMatch(tab -> tab.contains(stack))).toList();
    }

    private static List<ItemStack> getPotionItems() {
        return CreativeModeTabs.allTabs().stream().flatMap(tab -> tab.getDisplayItems().stream())
            .filter(stack -> stack.getItem() instanceof PotionItem).toList();
    }

    public AlchemistCauldronEmiRecipe(ResourceLocation id, boolean recycle, EmiIngredient input, EmiIngredient bottle,
                                      EmiStack output) {
        super(ISNSIntegration.ALCHEMIST_CAULDRON, id, 18 + 4 + 26 + 28 + 26 + 5 + 18, 18 + 2 + 18);
        this.recycle = recycle;
        this.input = input;
        this.bottle = bottle;
        this.output = output;

        inputs = List.of(input, bottle);
        outputs = List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(IronsSpellbooks.id("textures/gui/jei_alchemist_cauldron.png"), 0, 0,
            18 + 4 + 26 + 28 + 26 + 5 + 18, 18, 0, 0);

        widgets.addSlot(ISNSIntegration.ALCHEMIST_CAULDRON_BLOCK, 18 + 4 + 26 + 4, 18 + 2).drawBack(false);
        widgets.addSlot(input, 0, 0).drawBack(false);
        widgets.addSlot(bottle, 18 + 4 + 26 + 4, 0).drawBack(false);
        widgets.addSlot(output, 18 + 4 + 26 + 28 + 26 + 5, 0).drawBack(false).recipeContext(this);

        if (recycle) {
            Double chance = ServerConfigs.SCROLL_RECYCLE_CHANCE.get();
            widgets.addText(Component.literal((chance * 100) + "%"), 18 + 4 + 26 + 28 + 13, 18 + 2,
                    chance >= 1.0 ? 5635925 : 16733525, true).horizontalAlign(TextWidget.Alignment.CENTER)
                .verticalAlign(TextWidget.Alignment.START);
        }
    }
}
