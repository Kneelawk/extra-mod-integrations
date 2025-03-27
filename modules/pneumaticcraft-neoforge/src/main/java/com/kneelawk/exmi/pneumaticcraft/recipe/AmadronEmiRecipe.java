package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.AmadronRecipe;
import me.desht.pneumaticcraft.client.gui.widget.WidgetAmadronOffer;
import me.desht.pneumaticcraft.common.recipes.amadron.AmadronOffer;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

public class AmadronEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.WIDGET_AMADRON_OFFER, 0, 0, 73, 35);
    private static final EmiTexture LIMITED_ICON = new EmiTexture(Textures.GUI_OK_LOCATION, 0, 0, 16, 16, 16, 16, 16, 16);
    
    private final EmiIngredient input;
    private final EmiStack output;
    private final AmadronRecipe recipe;

    public AmadronEmiRecipe(RecipeHolder<AmadronRecipe> holder) {
        super(holder.id());
        
        this.recipe = holder.value();
        this.input = recipe.getInput().resource().map(EmiStack::of, NeoForgeEmiStack::of);
        this.output = recipe.getOutput().resource().map(EmiStack::of, NeoForgeEmiStack::of);
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.AMADRON_TRADE;
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

        if (recipe.isLocationLimited()) {
            widgets.addTexture(LIMITED_ICON, 60, -4);
        }
        widgets.addText(recipe.getVendorName(), getBackground().width / 2, 3, 0xFF404040, false)
            .horizontalAlign(TextWidget.Alignment.CENTER);
        
        widgets.addSlot(input, 5, 14)
            .drawBack(false);
        widgets.addSlot(output, 50, 14)
            .drawBack(false)
            .recipeContext(this);
        
        if (recipe instanceof AmadronOffer offer) {
            widgets.addTooltipText(WidgetAmadronOffer.makeTooltip(offer, -1), 22, 0, 29, getBackground().height);
        }
    }
}
