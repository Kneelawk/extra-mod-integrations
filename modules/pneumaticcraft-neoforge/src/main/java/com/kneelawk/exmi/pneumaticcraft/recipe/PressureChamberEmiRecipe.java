package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.PressureChamberRecipe;
import me.desht.pneumaticcraft.common.recipes.machine.PressureDisenchantingRecipe;
import me.desht.pneumaticcraft.common.recipes.machine.PressureEnchantingRecipe;
import me.desht.pneumaticcraft.lib.PneumaticValues;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.Categories;

public class PressureChamberEmiRecipe extends AbstractPNCEmiRecipe {

    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_PRESSURE_CHAMBER, 5, 11, 166, 116);
    
    private final PressureChamberRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public PressureChamberEmiRecipe(RecipeHolder<PressureChamberRecipe> holder) {
        super(holder.id());
        
        this.recipe = holder.value();
        this.inputs = recipe.getInputs()
            .stream()
            .map(NeoForgeEmiIngredient::of)
            .toList();
        this.outputs = recipe.getOutputs().stream().map(EmiStack::of).toList();
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.PRESSURE_CHAMBER;
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
        
        addPressureGauge(widgets, 130, 27, -1, PneumaticValues.MAX_PRESSURE_PRESSURE_CHAMBER, PneumaticValues.DANGER_PRESSURE_PRESSURE_CHAMBER, recipe.getCraftingPressureForDisplay());
        
        for (int i = 0; i < inputs.size(); i++) {
            int posX = 18 + i % 3 * 17;
            int posY = 77 - i / 3 * 17;
            widgets.addSlot(inputs.get(i), posX, posY)
                .drawBack(false);
        }

        for (int i = 0; i < outputs.size(); i++) {
            widgets.addSlot(outputs.get(i), 100 + i % 3 * 18, 58 + i / 3 * 18)
                .drawBack(false)
                .recipeContext(this);
        }
        
        // TODO improve
        if (recipe instanceof PressureEnchantingRecipe ench) {
            widgets.addText(Component.literal("Enchant"), 0, 0, 0x80ff20, true);
        } else if (recipe instanceof PressureDisenchantingRecipe disench) {
            widgets.addText(Component.literal("Disenchant"), 0, 0, 0x80ff20, true);
        }

    }

    @Override
    public boolean supportsRecipeTree() {
        return !(recipe instanceof PressureEnchantingRecipe || recipe instanceof PressureDisenchantingRecipe);
    }
}
