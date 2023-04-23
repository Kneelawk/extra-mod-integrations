package com.kneelawk.extramodintegrations.hephaestus.modifiers;

import com.kneelawk.extramodintegrations.hephaestus.HephaestusIntegration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.worktable.IModifierWorktableRecipe;

import java.util.ArrayList;
import java.util.List;

public class ModifierWorktableEmiRecipe implements EmiRecipe {
    private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");
    private static final EmiTexture background = new EmiTexture(BACKGROUND_LOC, 0, 166, 121, 35);
    private static final EmiTexture toolIcon = new EmiTexture(BACKGROUND_LOC, 128, 0, 16, 16);
    private static final EmiTexture[] slotIcons = {
            new EmiTexture(BACKGROUND_LOC, 176, 0, 16, 16),
            new EmiTexture(BACKGROUND_LOC, 208, 0, 16, 16)
    };

    private final IModifierWorktableRecipe recipe;
    private final EmiIngredient inputTools;
    public ModifierWorktableEmiRecipe(IModifierWorktableRecipe recipe) {
        this.recipe = recipe;
        inputTools = EmiIngredient.of(recipe.getInputTools().stream().map(EmiStack::of).toList());
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return HephaestusIntegration.MODIFIER_WORKTABLE_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> result = new ArrayList<>();
        int max = Math.min(2, recipe.getInputCount());
        for (int i = 0; i < max; i++) {
            result.add(EmiIngredient.of(recipe.getDisplayItems(i).stream().map(EmiStack::of).toList()));
        }
        return result;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(inputTools);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 121;
    }

    @Override
    public int getDisplayHeight() {
        return 35;
    }

    private EmiIngredient getDisplayIngredient(int slot) {
        return EmiIngredient.of(recipe.getDisplayItems(slot).stream().map(EmiStack::of).toList());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(background, 0, 0);
         if (recipe.getInputTools().isEmpty()) {
          widgets.addTexture(toolIcon, 23, 16);
        }
        for (int i = 0; i < 2; i++) {
          List<ItemStack> stacks = recipe.getDisplayItems(i);
          if (stacks.isEmpty()) {
            widgets.addTexture(slotIcons[i], 43 + i * 18, 16);
          }
        }
        widgets.addText(recipe.getTitle(), 3, 2, 0x404040, false);

        // items
        widgets.addSlot(inputTools, 22, 15).drawBack(false).catalyst(true);
        int max = Math.min(2, recipe.getInputCount());
        for (int i = 0; i < max; i++) {
            widgets.addSlot(getDisplayIngredient(i), 42 + i*18, 15).drawBack(false);
        }
        // modifier input
        widgets.addSlot(EmiIngredient.of(recipe.getModifierOptions(null).stream().map(ModifierEmiStack::new).toList()), 81, 15)
                .drawBack(false)
                .catalyst(!recipe.isModifierOutput());
    }
}
