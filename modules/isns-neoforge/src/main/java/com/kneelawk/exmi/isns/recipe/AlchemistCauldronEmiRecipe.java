package com.kneelawk.exmi.isns.recipe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import net.neoforged.neoforge.fluids.FluidStack;

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
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluids;

import com.kneelawk.exmi.core.api.ExMILog;
import com.kneelawk.exmi.isns.ISNSConfig;
import com.kneelawk.exmi.isns.ISNSIntegration;

import static java.lang.Math.max;

public class AlchemistCauldronEmiRecipe extends BasicEmiRecipe {
    private final boolean recycle;
    private final EmiIngredient input;
    private final EmiIngredient fluidIn;
    private final List<EmiStack> output;

    public static Stream<AlchemistCauldronEmiRecipe> getRecipes(RecipeManager manager) {
        List<ItemStack> potionIngredients = getPotionIngredients();
        return Streams.concat(getScrollRecipes(), getCustomRecipes(manager), getPotionRecipes(potionIngredients));
    }

    private static Stream<AlchemistCauldronEmiRecipe> getScrollRecipes() {
        return Arrays.stream(SpellRarity.values()).map(AlchemistCauldronEmiRecipe::recycleRecipe);
    }

    private static Stream<AlchemistCauldronEmiRecipe> getCustomRecipes(RecipeManager manager) {
        return manager.getAllRecipesFor(RecipeRegistry.ALCHEMIST_CAULDRON_BREW_TYPE.get()).stream().map(
            holder -> new AlchemistCauldronEmiRecipe(holder.id(), false, EmiIngredient.of(holder.value().reagent()),
                EmiStack.of(holder.value().fluidIn().getFluid(), holder.value().fluidIn().getComponentsPatch()),
                Stream.concat(holder.value().byproduct().stream().map(EmiStack::of),
                    holder.value().results().stream()
                        .map(stack -> EmiStack.of(stack.getFluid(), stack.getComponentsPatch()))).toList()));
    }

    private static Stream<AlchemistCauldronEmiRecipe> getPotionRecipes(List<ItemStack> potionIngredients) {
        if (!ServerConfigs.ALLOW_CAULDRON_BREWING.get()) {
            return Stream.of();
        } else {
            List<ItemStack> potions = getPotionItems();
            ClientLevel level = Minecraft.getInstance().level;

            return level == null ? Stream.of() : potionIngredients.stream().flatMap(
                reagent -> potions.stream().filter(potion -> level.potionBrewing().hasMix(potion, reagent))
                    .map(baseItem -> {
                        ResourceLocation reagentKey = getKey(reagent);
                        ResourceLocation baseKey = getKey(baseItem);
                        ItemStack mix = level.potionBrewing().mix(reagent, baseItem);
                        FluidStack baseFluid = PotionFluid.from(baseItem);
                        FluidStack mixFluid = PotionFluid.from(mix);
                        return new AlchemistCauldronEmiRecipe(IronsSpellbooks.id(
                            "/potion_brewing/" + reagentKey.getNamespace() + "/" + reagentKey.getPath() + "/" +
                                baseKey.getNamespace() + "/" + baseKey.getPath() + "/" +
                                getPotionContentsPath(baseItem) + getPotionContentsPath(mix)), false,
                            EmiStack.of(reagent), EmiStack.of(baseFluid.getFluid(), baseFluid.getComponentsPatch()),
                            List.of(EmiStack.of(mixFluid.getFluid(), mixFluid.getComponentsPatch())));
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
        EmiStack ink = EmiStack.of(InkItem.getInkForRarity(rarity).fluid().value(), 250)
            .setChance(ServerConfigs.SCROLL_RECYCLE_CHANCE.get().floatValue());
        return new AlchemistCauldronEmiRecipe(IronsSpellbooks.id("/scroll_recycle/" + rarity.getValue()), true, scrolls,
            EmiStack.of(Fluids.WATER), List.of(ink));
    }

    private static ItemStack getScrollStack(ItemStack stack, AbstractSpell spell, int level) {
        ItemStack scrollStack = stack.copy();
        ISpellContainer.createScrollContainer(spell, level, scrollStack);
        return scrollStack;
    }

    private static boolean isIngredient(ItemStack stack) {
        try {
            // This isn't great performance-wise, but it's the only non-hacky way to get all potion ingredients
            return Minecraft.getInstance().level.potionBrewing().isIngredient(stack);
        } catch (LinkageError | RuntimeException e) {
            ExMILog.LOG.error("Failed to check if item is a potion reagent {}.", stack, e);
            return false;
        }
    }

    private static @NotNull ResourceLocation getKey(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    private static List<ItemStack> getPotionIngredients() {
        // This isn't great performance-wise, but it's the only non-hacky way to get all potion ingredients
        return ISNSConfig.limitMaxGatheredPotionIngredients(BuiltInRegistries.ITEM.stream().map(ItemStack::new)
            .filter(stack -> CreativeModeTabs.allTabs().stream().anyMatch(tab -> tab.contains(stack)))
            .filter(AlchemistCauldronEmiRecipe::isIngredient)).toList();
    }

    private static List<ItemStack> getPotionItems() {
        return ISNSConfig.limitMaxGatheredPotions(
            CreativeModeTabs.allTabs().stream().flatMap(tab -> tab.getDisplayItems().stream())
                .filter(stack -> stack.getItem() instanceof PotionItem).filter(filterDuplicates())).toList();
    }

    private static Predicate<ItemStack> filterDuplicates() {
        ObjectLinkedOpenCustomHashSet<ItemStack> set = new ObjectLinkedOpenCustomHashSet<>(new Hash.Strategy<>() {
            @Override
            public int hashCode(ItemStack o) {
                return o.getItem().hashCode() * 31 + o.getComponentsPatch().hashCode();
            }

            @Override
            public boolean equals(ItemStack a, ItemStack b) {
                if (a == b) return true;
                if (a == null || b == null) return false;
                return a.getItem() == b.getItem() && a.getComponentsPatch().equals(b.getComponentsPatch());
            }
        });
        return set::add;
    }

    public AlchemistCauldronEmiRecipe(ResourceLocation id, boolean recycle, EmiIngredient input, EmiIngredient fluidIn,
                                      List<EmiStack> output) {
        super(ISNSIntegration.ALCHEMIST_CAULDRON, id, 18 + 4 + 26 + 28 + 26 + 5 + 18,
            max(18 + 2 + 18, output.size() * 18));
        this.recycle = recycle;
        this.input = input;
        this.fluidIn = fluidIn;
        this.output = output;

        inputs = List.of(input, fluidIn);
        outputs = List.copyOf(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(IronsSpellbooks.id("textures/gui/jei_alchemist_cauldron.png"), 0, 0,
            18 + 4 + 26 + 28 + 26 + 5 + 18, 18, 0, 0);

        widgets.addSlot(ISNSIntegration.ALCHEMIST_CAULDRON_BLOCK, 18 + 4 + 26 + 4, 18 + 2).drawBack(false);
        widgets.addSlot(input, 0, 0);
        widgets.addSlot(fluidIn, 18 + 4 + 26 + 5, 0);
        for (int i = 0; i < output.size(); i++) {
            EmiStack o = output.get(i);
            widgets.addSlot(o, 18 + 4 + 26 + 28 + 26 + 5, i * 18).recipeContext(this);
        }

        if (recycle) {
            double chance = ServerConfigs.SCROLL_RECYCLE_CHANCE.get();
            widgets.addText(Component.literal((chance * 100) + "%"), 18 + 4 + 26 + 28 + 13, 18 + 2,
                    chance >= 1.0 ? 5635925 : 16733525, true).horizontalAlign(TextWidget.Alignment.CENTER)
                .verticalAlign(TextWidget.Alignment.START);
        }
    }
}
