package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.Categories;

import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.HeatFrameCoolingRecipe;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.ArrayList;
import java.util.List;

public class HeatFrameCoolingEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_MISC_RECIPES, 0, 0, 82, 18);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_JEI_MISC_RECIPES, 82, 0, 38, 17);
    private static final EmiTexture BONUS_ICON = new EmiTexture(Textures.GUI_JEI_BONUS, 0, 0, 16, 16, 16, 16, 16, 16);
    
    private final HeatFrameCoolingRecipe recipe;
    private final EmiIngredient input;
    private final EmiStack output;
    
    public HeatFrameCoolingEmiRecipe(RecipeHolder<HeatFrameCoolingRecipe> holder) {
        super(holder.id());
        
        this.recipe = holder.value();
        this.input = recipe.getInput().map(EmiIngredient::of, f -> f.either().map(NeoForgeEmiStack::of, twa -> EmiIngredient.of(twa.tag(), twa.amount())));
        this.output = EmiStack.of(recipe.getOutput());
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.HEAT_FRAME_COOLING;
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
        
        widgets.addAnimatedTexture(PROGRESS_BAR, 22, 0, 30 * 50, true, false, false);
        if (recipe.getBonusMultiplier() > 0f) {
            widgets.addTexture(BONUS_ICON, 30, 0);
        }
        
        widgets.addSlot(input, 0, 0)
            .drawBack(false);
        widgets.addSlot(output, 64, 0)
            .drawBack(false)
            .recipeContext(this);
        
        List<Component> tooltip = new ArrayList<>();
        tooltip.addAll(PneumaticCraftUtils.splitStringComponent(I18n.get("pneumaticcraft.gui.nei.recipe.heatFrameCooling",
            recipe.getThresholdTemperature() - 273
        )));
        if (recipe.getBonusMultiplier() > 0f) {
            String bonus = ChatFormatting.YELLOW + I18n.get("pneumaticcraft.gui.nei.recipe.heatFrameCooling.bonus",
                recipe.getBonusMultiplier() * 100,
                recipe.getOutput().getHoverName().getString(),
                recipe.getThresholdTemperature() - 273,
                recipe.getBonusLimit() + 1
            );
            tooltip.addAll(PneumaticCraftUtils.splitStringComponent(bonus));
        }
        widgets.addTooltipText(tooltip, 23, 0, 37, getBackground().height);
    }
}
