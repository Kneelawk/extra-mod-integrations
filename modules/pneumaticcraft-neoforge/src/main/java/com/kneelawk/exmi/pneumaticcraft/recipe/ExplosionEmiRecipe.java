package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.ExplosionCraftingRecipe;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.List;

public class ExplosionEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_MISC_RECIPES, 0, 0, 82, 18);
    public static final EmiTexture ICON = new EmiTexture(Textures.JEI_EXPLOSION, 0, 0, 16, 16, 16, 16, 16, 16);
    
    private final EmiIngredient input;
    private final List<EmiStack> outputs;
    
    public ExplosionEmiRecipe(RecipeHolder<ExplosionCraftingRecipe> holder) {
        super(holder.id());
        
        ExplosionCraftingRecipe recipe = holder.value();
        this.input = NeoForgeEmiIngredient.of(recipe.getInput());
        float chance = (100 - recipe.getLossRate()) / 100f;
        this.outputs = recipe.getOutputs().stream().map(EmiStack::of).map(s -> s.setChance(chance)).toList();
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.EXPLOSION_CRAFTING;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addTexture(ICON, 30, 0);
        
        widgets.addSlot(input, 0, 0);
        widgets.addSlot(outputs.get(0), 64, 0)
            .recipeContext(this);
    }
}
