package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;

import net.neoforged.neoforge.fluids.FluidType;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

public class YeastCraftingEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_YEAST_CRAFTING, 0, 0, 128, 40);
    
    private final EmiIngredient itemInput;
    private final EmiIngredient fluidInput;
    private final EmiIngredient water;
    private final EmiStack output;

    public YeastCraftingEmiRecipe(ResourceLocation id, EmiIngredient itemInput, EmiIngredient fluidInput, EmiStack output) {
        super(id);
        this.itemInput = itemInput;
        this.fluidInput = fluidInput;
        this.output = output;
        this.water = EmiStack.of(Fluids.WATER, FluidType.BUCKET_VOLUME);
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.YEAST_CRAFTING;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(itemInput, fluidInput);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addSlot(itemInput, 0, 0)
            .drawBack(false);
        widgets.addSlot(fluidInput, 15, 15)
            .drawBack(false);
        widgets.addSlot(water, 31, 15)
            .drawBack(false);
        widgets.addSlot(fluidInput, 79, 15)
            .drawBack(false)
            .recipeContext(this);
        widgets.addSlot(fluidInput, 95, 15)
            .drawBack(false)
            .recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
