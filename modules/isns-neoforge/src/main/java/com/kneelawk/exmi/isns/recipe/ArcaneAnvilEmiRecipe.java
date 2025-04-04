package com.kneelawk.exmi.isns.recipe;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import org.jetbrains.annotations.NotNull;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.exmi.core.api.ExMITextures;
import com.kneelawk.exmi.isns.ISNSConfig;
import com.kneelawk.exmi.isns.ISNSIntegration;

public class ArcaneAnvilEmiRecipe extends BasicEmiRecipe {
    private final EmiIngredient in1;
    private final EmiIngredient in2;
    private final EmiStack out;

    public static Stream<ArcaneAnvilEmiRecipe> getRecipes() {
        List<ItemStack> visibleItems = getVisibleItems();
        return Streams.concat(getScrollRecipes(), getImbueRecipes(visibleItems), getUpgradeRecipes(visibleItems),
            getAffinityAttuneRecipes());
    }

    private static Stream<ArcaneAnvilEmiRecipe> getScrollRecipes() {
        return ServerConfigs.SPEC.isLoaded() && !ServerConfigs.SCROLL_MERGING.get() ? Stream.empty() :
            SpellRegistry.getEnabledSpells().stream().sorted(Comparator.comparing(AbstractSpell::getSpellId)).flatMap(
                spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel())
                    .mapToObj(i -> ArcaneAnvilEmiRecipe.ofScrollUpgrade(spell, i)));
    }

    private static Stream<ArcaneAnvilEmiRecipe> getImbueRecipes(List<ItemStack> visibleItems) {
        Stream<ArcaneAnvilEmiRecipe> stream =
            visibleItems.stream().filter(Utils::canImbue).flatMap(stack -> SpellRegistry.getEnabledSpells().stream()
                .flatMap(spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel())
                    .mapToObj(level -> ofImbue(stack, spell, level))));
        int maxImbueRecipes = ISNSConfig.getMaxImbueRecipes();
        if (maxImbueRecipes >= 0) {
            return stream.limit(maxImbueRecipes);
        }
        return stream;
    }

    private static Stream<ArcaneAnvilEmiRecipe> getUpgradeRecipes(List<ItemStack> visibleItems) {
        List<ItemStack> upgradable = visibleItems.stream().filter(Utils::canBeUpgraded).toList();
        return BuiltInRegistries.ITEM.stream().filter(item -> item instanceof UpgradeOrbItem)
            .flatMap(upgradeOrb -> upgradable.stream().map(item -> ofItemUpgrade(item, new ItemStack(upgradeOrb))));
    }

    private static Stream<ArcaneAnvilEmiRecipe> getAffinityAttuneRecipes() {
        return SpellRegistry.getEnabledSpells().stream().sorted(Comparator.comparing(AbstractSpell::getSpellId))
            .map(ArcaneAnvilEmiRecipe::ofAffinityRingAttune);
    }

    private static List<ItemStack> getVisibleItems() {
        return BuiltInRegistries.ITEM.stream().map(ItemStack::new)
            .filter(stack -> CreativeModeTabs.allTabs().stream().anyMatch(tab -> tab.contains(stack))).toList();
    }

    private static ArcaneAnvilEmiRecipe ofScrollUpgrade(AbstractSpell spell, int baseLevel) {
        ItemStack scroll1 = new ItemStack(ItemRegistry.SCROLL.get());
        ItemStack scroll2 = new ItemStack(ItemRegistry.SCROLL.get());
        ItemStack ink = new ItemStack(InkItem.getInkForRarity(spell.getRarity(baseLevel + 1)));
        ISpellContainer.createScrollContainer(spell, baseLevel, scroll1);
        ISpellContainer.createScrollContainer(spell, baseLevel + 1, scroll2);
        return new ArcaneAnvilEmiRecipe(
            IronsSpellbooks.id("/scroll_upgrade/" + spell.getSpellId().replace(':', '/') + "/" + baseLevel),
            EmiStack.of(scroll1), EmiStack.of(ink), EmiStack.of(scroll2));
    }

    private static ArcaneAnvilEmiRecipe ofImbue(ItemStack stack, AbstractSpell spell, int level) {
        ItemStack scroll = new ItemStack(ItemRegistry.SCROLL.get());
        ISpellContainer.createScrollContainer(spell, level, scroll);
        ItemStack result = stack.copy();
        ISpellContainer.createScrollContainer(spell, level, result);
        ResourceLocation itemId = getKey(stack);
        return new ArcaneAnvilEmiRecipe(IronsSpellbooks.id(
            "/imbue/" + spell.getSpellId().replace(':', '/') + "/" + level + "/" + itemId.getNamespace() + "/" +
                itemId.getPath()), EmiStack.of(stack), EmiStack.of(scroll), EmiStack.of(result));
    }

    private static ArcaneAnvilEmiRecipe ofItemUpgrade(ItemStack stack, ItemStack upgrade) {
        ItemStack result = stack.copy();
        ResourceKey<UpgradeOrbType> orbType = upgrade.get(ComponentRegistry.UPGRADE_ORB_TYPE);
        result.set(ComponentRegistry.UPGRADE_DATA,
            UpgradeData.NONE.addUpgrade(result, Minecraft.getInstance().level.registryAccess().holderOrThrow(orbType),
                UpgradeUtils.getRelevantEquipmentSlot(stack)));
        ResourceLocation itemId = getKey(stack);
        return new ArcaneAnvilEmiRecipe(IronsSpellbooks.id(
            "/item_upgrade/" + orbType.location().getNamespace() + "/" + orbType.location().getPath() + "/" +
                itemId.getNamespace() + "/" + itemId.getPath()), EmiStack.of(stack), EmiStack.of(upgrade),
            EmiStack.of(result));
    }

    private static ArcaneAnvilEmiRecipe ofAffinityRingAttune(AbstractSpell spell) {
        ItemStack result = new ItemStack(ItemRegistry.AFFINITY_RING.get());
        result.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(spell));
        EmiIngredient ring =
            EmiIngredient.of(Stream.concat(SpellRegistry.getEnabledSpells().stream().map(randomSpell -> {
                ItemStack baseRing = new ItemStack(ItemRegistry.AFFINITY_RING.get());
                baseRing.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(randomSpell));
                return EmiStack.of(baseRing);
            }), Stream.of(EmiStack.of(ItemRegistry.AFFINITY_RING.get()))).toList());
        EmiIngredient scroll =
            EmiIngredient.of(IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).mapToObj(level -> {
                ItemStack scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
                ISpellContainer.createScrollContainer(spell, level, scrollStack);
                return EmiStack.of(scrollStack);
            }).toList());
        return new ArcaneAnvilEmiRecipe(
            IronsSpellbooks.id("/affinity_ring_attune/" + spell.getSpellId().replace(':', '/')), ring, scroll,
            EmiStack.of(result));
    }

    private static @NotNull ResourceLocation getKey(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    public ArcaneAnvilEmiRecipe(ResourceLocation id, EmiIngredient in1, EmiIngredient in2, EmiStack out) {
        super(ISNSIntegration.ARCANE_ANVIL, id, 18 + 24 + 18 + 24 + 18, 18);
        this.in1 = in1;
        this.in2 = in2;
        this.out = out;

        inputs = List.of(in1, in2);
        outputs = List.of(out);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(in1, 0, 0);
        widgets.addTexture(ExMITextures.PLUS_LARGE_SYMBOL, 18 + 4, 1);
        widgets.addSlot(in2, 18 + 24, 0);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 24 + 18 + 4, 1);
        widgets.addSlot(out, 18 + 24 + 18 + 24, 0).recipeContext(this);
    }
}
