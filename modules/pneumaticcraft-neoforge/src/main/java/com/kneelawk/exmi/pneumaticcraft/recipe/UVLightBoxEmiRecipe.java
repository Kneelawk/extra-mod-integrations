package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.common.recipes.machine.UVLightBoxRecipe;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

public class UVLightBoxEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_MISC_RECIPES, 0, 0, 82, 18);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_JEI_MISC_RECIPES, 82, 0, 38, 17);
    
    private final EmiIngredient input;
    private final EmiStack output;
    
    public UVLightBoxEmiRecipe(ResourceLocation id, UVLightBoxRecipe recipe) {
        super(id);
        
        this.input = EmiIngredient.of(recipe.in());
        this.output = EmiStack.of(recipe.out());
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.UV_LIGHT_BOX;
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
        
        widgets.addAnimatedTexture(PROGRESS_BAR, 22, 0, 60 * 50, true, false, false);
        
        widgets.addSlot(input, 0, 0)
            .drawBack(false);
        widgets.addSlot(output, 64, 0)
            .drawBack(false)
            .recipeContext(this);
    }
}
