package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.ThermoPlantRecipe;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.Categories;

public class ThermoPlantEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_THERMOPNEUMATIC_PROCESSING_PLANT, 0, 0, 166, 70);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_THERMOPNEUMATIC_PROCESSING_PLANT, 176, 0, 48, 30);
    
    private final ThermoPlantRecipe recipe;
    private final EmiIngredient inputFluid;
    private final EmiIngredient inputItem;
    private final EmiStack outputFluid;
    private final EmiStack outputItem;
    
    public ThermoPlantEmiRecipe(RecipeHolder<ThermoPlantRecipe> holder) {
        super(holder.id());
        
        this.recipe = holder.value();
        this.inputFluid = recipe.getInputFluid().map(NeoForgeEmiIngredient::of).orElse(EmiStack.EMPTY);
        this.inputItem = recipe.getInputItem().map(EmiIngredient::of).orElse(EmiStack.EMPTY);
        this.outputFluid = NeoForgeEmiStack.of(recipe.getOutputFluid());
        this.outputItem = EmiStack.of(recipe.getOutputItem());
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.THERMO_PLANT;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(inputFluid, inputItem);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(outputFluid, outputItem);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        if (recipe.getRequiredPressure() != 0) {
            addPressureGauge(widgets, 141, 42, -1, PressureTier.TIER_ONE_HALF.getCriticalPressure(), PressureTier.TIER_ONE_HALF.getDangerPressure(), recipe.getRequiredPressure(), recipe.getAirUseMultiplier());
        }

        if (!recipe.getOperatingTemperature().isAny()) {
            addTemperatureGauge(widgets, 100, 12, recipe.getOperatingTemperature());
        }
        
        widgets.addAnimatedTexture(PROGRESS_BAR, 25, 20, 60 * 50, true, false, false);

        widgets.addTank(inputFluid, 7, 2, 18, 66, 1000)
            .drawBack(false);
        widgets.addSlot(inputItem, 32, 2)
            .drawBack(false);
        widgets.addTank(outputFluid, 73, 2, 18, 66, 1000)
            .drawBack(false)
            .recipeContext(this);
        widgets.addSlot(outputItem, 47, 50)
            .drawBack(false)
            .recipeContext(this);
    }
}
