package com.kneelawk.extramodintegrations.tconstruct.recipe.modifiers;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.stack.ModifierEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.worktable.IModifierWorktableRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModifierWorktableEmiRecipe extends BasicEmiRecipe {
    private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");

    private final Text title;
    private final Text description;
    private final EmiIngredient inputTools;
    private final int inputCount;
    private final List<EmiIngredient> displayItems;
    private final EmiIngredient modifier;
    private final boolean isModifierOutput;

    public ModifierWorktableEmiRecipe(IModifierWorktableRecipe recipe) {
        super(TiCCategories.MODIFIER_WORKTABLE, recipe.getId(), 121, 35);

        this.inputTools = EmiIngredient.of(recipe.getInputTools().stream().map(EmiStack::of).toList());
        this.inputCount = recipe.getInputCount();
        List<EmiIngredient> displayItems = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            displayItems.add(EmiIngredient.of(recipe.getDisplayItems(i).stream().map(EmiStack::of).toList()));
        }
        this.displayItems = displayItems;
        this.modifier = EmiIngredient.of(recipe.getModifierOptions(null).stream().map(ModifierEmiStack::of).toList());
        this.isModifierOutput = recipe.isModifierOutput();

        this.title = recipe.getTitle();
        this.description = recipe.getDescription(null);

        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(inputTools);
        inputs.addAll(displayItems);
        if (!isModifierOutput) inputs.add(modifier);
        this.inputs = Collections.unmodifiableList(inputs);

        if (isModifierOutput) outputs = modifier.getEmiStacks();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 121, 35, 0, 166);

        widgets.addText(title, 3, 2, 0x404040, false);
        widgets.addTooltip(List.of(TooltipComponent.of(description.asOrderedText())), 0, 0, width, 12);

        widgets.addSlot(inputTools, 22, 15)
                .drawBack(false);
        int max = Math.min(2, inputCount);
        for (int i = 0; i < max; i++) {
            widgets.addSlot(displayItems.get(i), 42 + i * 18, 15)
                    .drawBack(false);
        }
        SlotWidget modifierSlot = widgets.addSlot(modifier, 81, 15)
                .drawBack(false);
        if (isModifierOutput) modifierSlot.recipeContext(this);

        if (inputTools.isEmpty()) {
            widgets.addTexture(BACKGROUND_LOC, 23, 16, 16, 16, 128, 0);
        }
        for (int i = 0; i < 2; i++) {
            if (displayItems.get(i).isEmpty()) {
                widgets.addTexture(BACKGROUND_LOC, 43 + i * 18, 16, 16, 16, 176 + 32 * i, 0);
            }
        }
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
