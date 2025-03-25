package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.exmi.pneumaticcraft.Categories;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.List;

public class EtchingTankEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_ETCHING_TANK, 0, 0, 83, 42);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_JEI_ETCHING_TANK, 83, 0, 42, 42);
    
    private final EmiIngredient input;
    private final EmiStack output;
    private final EmiStack failed;
    private final EmiIngredient etchingFluid;

    public EtchingTankEmiRecipe(ResourceLocation id, EmiIngredient input, EmiStack output, EmiStack failed, EmiIngredient etchingFluid) {
        super(id);
        
        this.input = input;
        this.output = output;
        this.failed = failed;
        this.etchingFluid = etchingFluid;
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.ETCHING_TANK;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input, etchingFluid);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addAnimatedTexture(PROGRESS_BAR, 20, 0, 60 * 50, true, false, false);
        
        widgets.addSlot(input, 0, 12)
            .drawBack(false);
        widgets.addSlot(etchingFluid, 25, 12)
            .drawBack(false);
        widgets.addSlot(output, 65, 0)
            .drawBack(false)
            .recipeContext(this);
        widgets.addSlot(failed, 65, 24)
            .drawBack(false)
            .recipeContext(this);
    }
}
