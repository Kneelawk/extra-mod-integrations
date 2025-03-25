package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import com.kneelawk.exmi.pneumaticcraft.Categories;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.List;

public class SpawnerExtractionEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_SPAWNER_EXTRACTION, 0, 0, 120, 64);
    
    private final EmiIngredient itemInput;
    private final EmiIngredient cores;
    private final EmiStack itemOutput;
    private final EmiStack spawner;

    public SpawnerExtractionEmiRecipe(ResourceLocation id, EmiIngredient itemInput, EmiIngredient cores, EmiStack itemOutput) {
        super(id);
        
        this.itemInput = itemInput;
        this.cores = cores;
        this.itemOutput = itemOutput;
        this.spawner = EmiStack.of(Blocks.SPAWNER);
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.SPAWNER_EXTRACTION;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(itemInput, spawner);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(itemOutput);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addSlot(itemInput, 52, 2)
            .drawBack(false);
        widgets.addSlot(spawner, 52, 33)
            .drawBack(false);
        widgets.addSlot(cores, 17,33)
            .drawBack(false)
            .recipeContext(this);
        widgets.addSlot(itemOutput, 87, 33)
            .drawBack(false)
            .recipeContext(this);
    }
}
