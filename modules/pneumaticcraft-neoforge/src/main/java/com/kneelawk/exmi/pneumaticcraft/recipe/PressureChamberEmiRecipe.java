package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;
import java.util.Random;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.PressureChamberRecipe;
import me.desht.pneumaticcraft.common.recipes.machine.PressureDisenchantingRecipe;
import me.desht.pneumaticcraft.common.recipes.machine.PressureEnchantingRecipe;
import me.desht.pneumaticcraft.lib.PneumaticValues;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

public class PressureChamberEmiRecipe extends AbstractPNCEmiRecipe {

    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_PRESSURE_CHAMBER, 5, 11, 166, 116);

    private final PressureChamberRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;
    private final int uniq = new Random().nextInt();

    public PressureChamberEmiRecipe(RecipeHolder<PressureChamberRecipe> holder) {
        super(holder.id());

        this.recipe = holder.value();
        this.inputs = recipe.getInputs().stream().map(NeoForgeEmiIngredient::of).toList();
        this.outputs = recipe.getOutputs().stream().map(EmiStack::of).toList();
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.PRESSURE_CHAMBER;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        addPressureGauge(widgets, 130, 27, -1, PneumaticValues.MAX_PRESSURE_PRESSURE_CHAMBER,
            PneumaticValues.DANGER_PRESSURE_PRESSURE_CHAMBER, recipe.getCraftingPressureForDisplay());

        if (recipe instanceof PressureEnchantingRecipe || recipe instanceof PressureDisenchantingRecipe) {
            addTooltip(widgets.addGeneratedSlot(r -> getSlot(r, 0), uniq, 18, 77), true, "in0").drawBack(false);
            addTooltip(widgets.addGeneratedSlot(r -> getSlot(r, 1), uniq, 35, 77), true, "in1").drawBack(false);

            addTooltip(widgets.addGeneratedSlot(r -> getSlot(r, 2), uniq, 100, 58), false, "out0").drawBack(false)
                .recipeContext(this);
            addTooltip(widgets.addGeneratedSlot(r -> getSlot(r, 3), uniq, 118, 58), false, "out1").drawBack(false)
                .recipeContext(this);
        } else {
            for (int i = 0; i < inputs.size(); i++) {
                int posX = 18 + i % 3 * 17;
                int posY = 77 - i / 3 * 17;
                widgets.addSlot(inputs.get(i), posX, posY).drawBack(false);
            }

            for (int i = 0; i < outputs.size(); i++) {
                widgets.addSlot(outputs.get(i), 100 + i % 3 * 18, 58 + i / 3 * 18).drawBack(false).recipeContext(this);
            }
        }
    }

    private SlotWidget addTooltip(SlotWidget slot, boolean input, String slotName) {
        String translationKey = this.recipe.getTooltipKey(input, slotName);
        if (!translationKey.isEmpty()) {
            slot.appendTooltip(Component.translatable(translationKey));
        }
        return slot;
    }

    private EmiIngredient getSlot(Random random, int idx) {
        EmiIngredient inputItem;
        EmiIngredient inputBook;
        EmiIngredient outputItem;
        EmiIngredient outputBook;

        Registry<Enchantment> enchantmentRegistry =
            Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        // todo only get enchantment valid for item
        Holder<Enchantment> enchantmentHolder =
            enchantmentRegistry.getHolder(random.nextInt(enchantmentRegistry.size())).orElseThrow();

        int level = random.nextInt(enchantmentHolder.value().getMaxLevel()) + 1;

        EnchantmentInstance enchantmentInstance = new EnchantmentInstance(enchantmentHolder, level);

        ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(enchantmentInstance);
        ItemStack enchantedPickaxe = Items.DIAMOND_PICKAXE.getDefaultInstance();
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(enchantmentHolder, level);
        EnchantmentHelper.setEnchantments(enchantedPickaxe, enchantments.toImmutable());

        if (this.recipe instanceof PressureEnchantingRecipe) {
            inputItem = EmiStack.of(Items.DIAMOND_PICKAXE);
            inputBook = EmiStack.of(enchantedBook);

            outputItem = EmiStack.of(enchantedPickaxe);
            outputBook = EmiStack.of(Items.BOOK);
        } else if (this.recipe instanceof PressureDisenchantingRecipe) {
            inputItem = EmiStack.of(enchantedPickaxe);
            inputBook = EmiStack.of(Items.BOOK);

            outputItem = EmiStack.of(Items.DIAMOND_PICKAXE);
            outputBook = EmiStack.of(enchantedBook);
        } else {
            throw new IllegalStateException();
        }

        return new EmiIngredient[]{
            inputItem, inputBook, outputItem, outputBook
        }[idx];
    }

    @Override
    public boolean supportsRecipeTree() {
        return !(recipe instanceof PressureEnchantingRecipe || recipe instanceof PressureDisenchantingRecipe);
    }
}
