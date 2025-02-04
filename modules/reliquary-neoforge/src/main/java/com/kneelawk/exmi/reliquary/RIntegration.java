package com.kneelawk.exmi.reliquary;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.stream.Streams;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import reliquary.Reliquary;
import reliquary.crafting.AlkahestryChargingRecipe;
import reliquary.crafting.AlkahestryCraftingRecipe;
import reliquary.crafting.AlkahestryRecipeRegistry;
import reliquary.init.ModBlocks;
import reliquary.init.ModItems;
import reliquary.util.potions.PotionEssence;
import reliquary.util.potions.PotionHelper;
import reliquary.util.potions.PotionMap;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;

import com.kneelawk.exmi.core.api.ExMIConstants;
import com.kneelawk.exmi.core.api.ExMIPlugin;
import com.kneelawk.exmi.core.api.util.RecipeReverseLookup;
import com.kneelawk.exmi.reliquary.recipe.AlkahestryChargingEmiRecipe;
import com.kneelawk.exmi.reliquary.recipe.AlkahestryCraftingEmiRecipe;
import com.kneelawk.exmi.reliquary.recipe.CauldronEmiRecipe;
import com.kneelawk.exmi.reliquary.recipe.EmiItemDescriptionBuilder;
import com.kneelawk.exmi.reliquary.recipe.MortarEmiRecipe;

public class RIntegration implements ExMIPlugin {
    public static final EmiStack CRAFTING_TABLE = EmiStack.of(Blocks.CRAFTING_TABLE);
    public static final EmiStack ALKAHESTRY_TOME = EmiStack.of(ModItems.ALKAHESTRY_TOME.get());
    public static final EmiStack MORTAR_STACK = EmiStack.of(ModBlocks.APOTHECARY_MORTAR_ITEM.get());
    public static final EmiStack CAULDRON_STACK = EmiStack.of(ModBlocks.APOTHECARY_CAULDRON_ITEM.get());
    public static final EmiStack POTION_ESSENCE = EmiStack.of(ModItems.POTION_ESSENCE.get());
    public static final EmiStack MAGAZINE = EmiStack.of(ModItems.NEUTRAL_MAGAZINE.get());
    public static final EmiStack BULLET = EmiStack.of(ModItems.NEUTRAL_BULLET.get());
    public static final EmiStack LINGERING_POTION = EmiStack.of(ModItems.LINGERING_POTION.get());
    public static final EmiStack EMPTY_MAGAZINE = EmiStack.of(ModItems.EMPTY_MAGAZINE.get());
    public static final EmiStack[] POTIONS = {
        EmiStack.of(ModItems.POTION.get()),
        EmiStack.of(ModItems.SPLASH_POTION.get()),
        EmiStack.of(ModItems.LINGERING_POTION.get())
    };
    public static final EmiStack[][] POTION_INPUTS = {
        {
            EmiStack.of(Items.NETHER_WART), EmiStack.of(ModItems.EMPTY_POTION_VIAL.get())
        },
        {
            EmiStack.of(Items.GUNPOWDER), EmiStack.of(Items.NETHER_WART), EmiStack.of(ModItems.EMPTY_POTION_VIAL.get())
        },
        {
            EmiStack.of(Items.GUNPOWDER), EmiStack.of(Items.DRAGON_BREATH), EmiStack.of(Items.NETHER_WART),
            EmiStack.of(ModItems.EMPTY_POTION_VIAL.get())
        }
    };
    public static final EmiStack[] BULLETS = {
        EmiStack.of(ModItems.NEUTRAL_BULLET.get()), EmiStack.of(ModItems.EXORCISM_BULLET.get()),
        EmiStack.of(ModItems.BLAZE_BULLET.get()), EmiStack.of(ModItems.ENDER_BULLET.get()),
        EmiStack.of(ModItems.CONCUSSIVE_BULLET.get()), EmiStack.of(ModItems.BUSTER_BULLET.get()),
        EmiStack.of(ModItems.SEEKER_BULLET.get()), EmiStack.of(ModItems.SAND_BULLET.get()),
        EmiStack.of(ModItems.STORM_BULLET.get())
    };
    public static final EmiStack[] MAGAZINES = {
        EmiStack.of(ModItems.NEUTRAL_MAGAZINE.get()), EmiStack.of(ModItems.EXORCISM_MAGAZINE.get()),
        EmiStack.of(ModItems.BLAZE_MAGAZINE.get()), EmiStack.of(ModItems.ENDER_MAGAZINE.get()),
        EmiStack.of(ModItems.CONCUSSIVE_MAGAZINE.get()), EmiStack.of(ModItems.BUSTER_MAGAZINE.get()),
        EmiStack.of(ModItems.SEEKER_MAGAZINE.get()), EmiStack.of(ModItems.SAND_MAGAZINE.get()),
        EmiStack.of(ModItems.STORM_MAGAZINE.get())
    };

    public static final EmiRecipeCategory ALKAHESTRY_CHARGING =
        new EmiRecipeCategory(Reliquary.getRL("alkahestry_charging"), ALKAHESTRY_TOME);
    public static final EmiRecipeCategory ALKAHESTRY_CRAFTING =
        new EmiRecipeCategory(Reliquary.getRL("alkahestry_crafting"), ALKAHESTRY_TOME);
    public static final EmiRecipeCategory APOTHECARY_MORTAR =
        new EmiRecipeCategory(Reliquary.getRL("apothecary_mortar"), MORTAR_STACK);
    public static final EmiRecipeCategory APOTHECARY_CAULDRON =
        new EmiRecipeCategory(Reliquary.getRL("apothecary_cauldron"), CAULDRON_STACK);

    public static final Comparison COMPARE_POTION_CONTENTS =
        Comparison.compareData(s -> s.getComponentChanges().<PotionContents>get(DataComponents.POTION_CONTENTS));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ALKAHESTRY_CHARGING);
        registry.addWorkstation(ALKAHESTRY_CHARGING, CRAFTING_TABLE);
        for (AlkahestryChargingRecipe recipe : AlkahestryRecipeRegistry.getChargingRecipes()) {
            ResourceLocation normalId = RecipeReverseLookup.getId(recipe);
            if (normalId == null) continue; // filter out duplicates

            ResourceLocation id =
                Reliquary.getRL("/alkahestry_charging/" + normalId.getNamespace() + "/" + normalId.getPath());
            registry.addRecipe(new AlkahestryChargingEmiRecipe(recipe, id));
        }

        registry.addCategory(ALKAHESTRY_CRAFTING);
        registry.addWorkstation(ALKAHESTRY_CRAFTING, CRAFTING_TABLE);
        for (AlkahestryCraftingRecipe recipe : AlkahestryRecipeRegistry.getCraftingRecipes()) {
            ResourceLocation normalId = RecipeReverseLookup.getId(recipe);
            if (normalId == null) continue; // filter out duplicates

            ResourceLocation id =
                Reliquary.getRL("/alkahestry_crafting/" + normalId.getNamespace() + "/" + normalId.getPath());
            registry.addRecipe(new AlkahestryCraftingEmiRecipe(recipe, id));
        }

        registry.addCategory(APOTHECARY_MORTAR);
        registry.addCategory(APOTHECARY_CAULDRON);
        registry.addWorkstation(APOTHECARY_MORTAR, MORTAR_STACK);
        registry.addWorkstation(APOTHECARY_CAULDRON, CAULDRON_STACK);
        registry.setDefaultComparison(POTION_ESSENCE, COMPARE_POTION_CONTENTS);
        for (EmiStack potion : POTIONS) {
            registry.setDefaultComparison(potion, COMPARE_POTION_CONTENTS);
        }
        Registry<Item> items = BuiltInRegistries.ITEM;
        for (PotionEssence essence : PotionMap.potionCombinations) {
            String combined = essence.getIngredients().stream().map(i -> {
                ResourceLocation key = items.getKey(i.getItem().getItem());
                return key.getNamespace() + "/" + key.getPath();
            }).sorted().collect(Collectors.joining("/"));

            // do mortar recipes
            ResourceLocation mortarId = Reliquary.getRL("/apothecary_mortar/" + combined);
            registry.addRecipe(new MortarEmiRecipe(essence, mortarId));
        }

        for (PotionEssence essence : PotionMap.uniquePotionEssences) {
            String combined = Streams.of(essence.getPotionContents().getAllEffects()).map(e -> {
                ResourceKey<MobEffect> key1 = e.getEffect().getKey();
                ResourceLocation key;
                if (key1 == null) {
                    key = ExMIConstants.rl("unknown_potion_effect");
                } else {
                    key = key1.location();
                }
                return key.getNamespace() + "/" + key.getPath() + "/" + e.getAmplifier() + "_" + e.getDuration();
            }).sorted().collect(Collectors.joining("/"));

            // do cauldron recipes
            for (int i = 0; i < POTIONS.length; i++) {
                EmiStack potion = POTIONS[i];
                ResourceLocation cauldronId = Reliquary.getRL(
                    "/apothecary_cauldron/" + combined + "/" + potion.getId().getNamespace() + "/" +
                        potion.getId().getPath());
                registry.addRecipe(new CauldronEmiRecipe(essence, POTION_INPUTS[i], potion, cauldronId));
            }

            // do bullet and magazine recipes
            ItemStack lingeringStack = LINGERING_POTION.getItemStack();
            PotionHelper.addPotionContentsToStack(lingeringStack, essence.getPotionContents());
            EmiStack lingering = EmiStack.of(lingeringStack);
            PotionContents bulletContents = PotionHelper.changePotionEffectsDuration(essence.getPotionContents(), 0.2f);
            for (int i = 0; i < BULLETS.length; i++) {
                EmiStack b = BULLETS[i];
                EmiStack m = MAGAZINES[i];
                ItemStack bulletStack = b.getItemStack();
                PotionHelper.addPotionContentsToStack(bulletStack, bulletContents);
                EmiStack bullet = EmiStack.of(bulletStack);
                ResourceLocation bulletId = Reliquary.getRL(
                    "/bullet_crafting/" + combined + "/" + b.getId().getNamespace() + "/" + b.getId().getPath());
                registry.addRecipe(new EmiCraftingRecipe(List.of(b, b, b, b, lingering, b, b, b, b), bullet.copy().setAmount(8), bulletId));

                ItemStack magazineStack = m.getItemStack();
                PotionHelper.addPotionContentsToStack(magazineStack, bulletContents);
                EmiStack magazine = EmiStack.of(magazineStack);
                ResourceLocation magazineId = Reliquary.getRL(
                    "/magazine_crafting/" + combined + "/" + m.getId().getNamespace() + "/" + m.getId().getPath());
                registry.addRecipe(new EmiCraftingRecipe(
                    List.of(bullet, bullet, bullet, bullet, EMPTY_MAGAZINE, bullet, bullet, bullet, bullet), magazine,
                    magazineId));
            }
        }

        for (EmiStack bullet : BULLETS) {
            registry.setDefaultComparison(bullet, COMPARE_POTION_CONTENTS);
        }
        for (EmiStack magazine : MAGAZINES) {
            registry.setDefaultComparison(magazine, COMPARE_POTION_CONTENTS);
        }

        EmiItemDescriptionBuilder.addIngredientInfo(registry);
    }
}
