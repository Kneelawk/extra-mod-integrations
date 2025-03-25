package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.RefineryRecipe;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.Categories;

public class RefineryEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_REFINERY, 6, 15, 166, 79);

    private final RefineryRecipe recipe;
    private final EmiIngredient input;
    private final List<EmiStack> outputs;
    
    public RefineryEmiRecipe(RecipeHolder<RefineryRecipe> holder) {
        super(holder.id());
        
        this.recipe = holder.value();
        this.input = NeoForgeEmiIngredient.of(recipe.getInput());
        this.outputs = recipe.getOutputs().stream().map(NeoForgeEmiStack::of).toList();
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.REFINERY;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of();
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        addTemperatureGauge(widgets, 26, 16, recipe.getOperatingTemp());
        
        int tankCapacity = (int) input.getAmount();
        
        widgets.addTank(input, 1, 9, 18, 66, tankCapacity)
            .drawBack(false);

        int n = 1;
        for (EmiStack out : outputs) {
            widgets.addTank(out, 68 + n * 20, 17 - n * 4, 18, 66, tankCapacity)
                .drawBack(false)
                .recipeContext(this);
            n++;
        }

    }
}
