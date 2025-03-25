package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import net.neoforged.neoforge.fluids.FluidType;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.FluidMixerRecipe;
import me.desht.pneumaticcraft.lib.PneumaticValues;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.Categories;

public class FluidMixerEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_FLUID_MIXER, 0, 0, 166, 70);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_FLUID_MIXER, 180, 0, 44, 30);

    private final FluidMixerRecipe recipe;
    private final EmiIngredient input1;
    private final EmiIngredient input2;
    private final EmiStack outputFluid;
    private final EmiStack outputItem;

    public FluidMixerEmiRecipe(RecipeHolder<FluidMixerRecipe> holder) {
        super(holder.id());
        
        this.recipe = holder.value();
        this.input1 = NeoForgeEmiIngredient.of(recipe.getInput1());
        this.input2 = NeoForgeEmiIngredient.of(recipe.getInput2());
        this.outputFluid = NeoForgeEmiStack.of(recipe.getOutputFluid());
        this.outputItem = EmiStack.of(recipe.getOutputItem());
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.FLUID_MIXER;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input1, input2);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(outputFluid, outputItem);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        addPressureGauge(widgets, 138, 35, -1, PneumaticValues.MAX_PRESSURE_TIER_ONE, PneumaticValues.DANGER_PRESSURE_TIER_ONE, recipe.getRequiredPressure());

        widgets.addAnimatedTexture(PROGRESS_BAR, 45, 20, recipe.getProcessingTime() * 50, true, false, false)
            .tooltipText(List.of(
                Component.literal(recipe.getProcessingTime() / 20f + "s"),
                Component.translatable("pneumaticcraft.gui.jei.tooltip.processingTime").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
            ));
        
        widgets.addTank(input1, 4, 2, 18, 66, FluidType.BUCKET_VOLUME)
            .drawBack(false);
        widgets.addTank(input2, 27, 2, 18, 66, FluidType.BUCKET_VOLUME)
            .drawBack(false);
        widgets.addSlot(outputItem, 63, 50)
            .drawBack(false)
            .recipeContext(this);
        widgets.addTank(outputFluid, 89, 2, 18, 66, FluidType.BUCKET_VOLUME)
            .drawBack(false)
            .recipeContext(this);
    }
}
