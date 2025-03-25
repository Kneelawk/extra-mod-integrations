package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.List;

public class PlasticSolidifyingEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_MISC_RECIPES, 0, 0, 82, 18);
    
    private final EmiIngredient input;
    private final EmiStack output;
    
    public PlasticSolidifyingEmiRecipe(ResourceLocation id, EmiIngredient input, EmiStack output) {
        super(id);
        
        this.input = input;
        this.output = output;
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.PLASTIC_SOLIDIFYING;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addSlot(input, 0, 0)
            .drawBack(false);
        widgets.addSlot(output, 64, 0)
            .drawBack(false)
            .recipeContext(this);
        
        widgets.addTooltipText(
            List.of(Component.translatable("pneumaticcraft.gui.jei.tooltip.plasticSolidifying")),
            23, 0, 37, getBackground().height 
        );
    }
}
