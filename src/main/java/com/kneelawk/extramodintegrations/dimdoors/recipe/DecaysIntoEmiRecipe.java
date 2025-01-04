package com.kneelawk.extramodintegrations.dimdoors.recipe;

import com.kneelawk.extramodintegrations.dimdoors.DimDoorsCategories;
import dev.architectury.fluid.FluidStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import org.dimdev.dimdoors.world.decay.DecayPattern;
import org.dimdev.dimdoors.world.decay.DecayProcessor;

import java.util.List;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public class DecaysIntoEmiRecipe<T> extends BasicEmiRecipe {

    public DecaysIntoEmiRecipe(T key, List<DecayPattern> patterns) {
        super(DimDoorsCategories.DECAYS_INTO, null, 66, 18);

        inputs = List.of(toEmiStack(DecayProcessor.defaultProduces(key)));
        outputs = patterns.stream()
                .map(pattern -> pattern.willBecome(key))
                .filter(Objects::nonNull)
                .map(DecaysIntoEmiRecipe::toEmiStack)
                .toList();
    }

    private static EmiStack toEmiStack(Object object) {
        if (object instanceof ItemStack stack) {
            return EmiStack.of(stack);
        } else if (object instanceof FluidStack stack) {
            return EmiStack.of(stack.getFluid(), stack.getTag(), stack.getAmount());
        } else {
            return EmiStack.EMPTY;
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 20, 0);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 48, 0).recipeContext(this);
    }
}
