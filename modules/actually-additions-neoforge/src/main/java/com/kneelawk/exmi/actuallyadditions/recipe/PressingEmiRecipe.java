package com.kneelawk.exmi.actuallyadditions.recipe;

import java.util.List;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.crafting.PressingRecipe;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.actuallyadditions.AAIntegration;

public class PressingEmiRecipe extends BasicEmiRecipe {
    public PressingEmiRecipe(RecipeHolder<PressingRecipe> holder) {
        super(AAIntegration.PRESSING, holder.id(), 93, 85);

        PressingRecipe recipe = holder.value();
        this.inputs = List.of(EmiIngredient.of(recipe.getInput()));
        this.outputs = List.of(NeoForgeEmiStack.of(recipe.getOutput()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(ActuallyAdditions.modLoc("textures/gui/gui_canola_press.png"), 0, 0, 93, 85, 41, 4);

        widgets.addTexture(AssetUtil.GUI_INVENTORY_LOCATION, 75, 0, 18, 85, 0, 171);

        widgets.addSlot(inputs.get(0), 39, 5)
            .drawBack(false);
        widgets.addTank(outputs.get(0), 75, 0, 18, 85, 1000)
            .drawBack(false)
            .recipeContext(this);
    }
}
