package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.mod.crafting.FermentingRecipe;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class FermentingEmiRecipe extends BasicEmiRecipe {
    private static final EmiTexture fluidBackground = new EmiTexture(AssetUtil.GUI_INVENTORY_LOCATION, 0, 171, 18, 85);
    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_fermenting_barrel");

    private final int time;

    public FermentingEmiRecipe(RecipeHolder<FermentingRecipe> holder) {
        super(AAIntegration.FERMENTING, holder.id(), 94, 86);

        FermentingRecipe recipe = holder.value();
        this.inputs = List.of(NeoForgeEmiStack.of(recipe.getInput()));
        this.outputs = List.of(NeoForgeEmiStack.of(recipe.getOutput()));
        this.time = recipe.getTime();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(RES_LOC, 0, 0, 94, 86, 41, 4);

        widgets.addTexture(fluidBackground, 19, 0);
        widgets.addTexture(fluidBackground, 57, 0);
        widgets.addAnimatedTexture(RES_LOC, 41, 30, 12, 29, 176, 0, time * 50, false, false, false);

        widgets.addTank(inputs.get(0), 19, 0, 18, 85, 1000)
            .drawBack(false);
        widgets.addTank(outputs.get(0), 57, 0, 18, 85, 1000)
            .drawBack(false)
            .recipeContext(this);
    }
}
