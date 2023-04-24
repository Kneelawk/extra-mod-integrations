package com.kneelawk.extramodintegrations.techreborn;

import com.kneelawk.extramodintegrations.util.DynamicFluidSlotWidget;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import techreborn.api.generator.FluidGeneratorRecipe;

import java.util.Collections;
import java.util.List;

import static com.kneelawk.extramodintegrations.ExMIMod.tooltip;

public class FluidGeneratorEmiRecipe implements EmiRecipe {
    private final FluidGeneratorRecipe recipe;
    private final EmiRecipeCategory category;
    private final Identifier id;
    private final EmiIngredient input;
    private final Fluid fluid;
    private final int fluidCapacity;
    private final int height;

    public FluidGeneratorEmiRecipe(FluidGeneratorRecipe recipe, EmiRecipeCategory category, Identifier id,
                                   int fluidCapacity, int energyCapacity) {
        this.recipe = recipe;
        this.category = category;
        this.id = id;
        input = EmiStack.of(recipe.fluid(), 1000 * 81);
        fluid = recipe.fluid();
        this.fluidCapacity = fluidCapacity;
        height = MathHelper.clamp(recipe.getEnergyPerBucket() * 48 / energyCapacity, 0, 48);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return Collections.emptyList();
    }

    @Override
    public int getDisplayWidth() {
        return 22 + 24 + 14;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TRTextures.TANK_BASE, 0, 0);
        widgets.add(new DynamicFluidSlotWidget(fluid, 1000 * 81, 4, 4, 14, 48, (long) fluidCapacity * 1000 * 81))
                .overlay(TRTextures.TANK_GRADUATION);

        widgets.addTexture(TRTextures.ARROW_RIGHT_EMPTY, 22 + 4, (56 - 10) / 2);
        widgets.addAnimatedTexture(TRTextures.ARROW_RIGHT_FULL, 22 + 4, (56 - 10) / 2,  1000, true, false, false);

        widgets.addTexture(TRTextures.ENERGY_BAR_EMPTY, 22 + 24, 3).tooltip((mx, my) -> List.of(
            TooltipComponent.of(tooltip("techreborn.recipe_power_per_bucket", recipe.getEnergyPerBucket()).asOrderedText())));
        widgets.addTexture(TRTextures.ENERGY_BAR_FULL.texture, 22 + 24 + 1, 4 + 48 - height, 12, height, 141, 151 + 48 - height);
    }
}
