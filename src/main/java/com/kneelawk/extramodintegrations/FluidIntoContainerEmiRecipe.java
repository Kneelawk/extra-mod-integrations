package com.kneelawk.extramodintegrations;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidIntoContainerEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiStack fluid;
    private final EmiStack container;
    private final EmiStack emptyContainer;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public FluidIntoContainerEmiRecipe(Identifier id, EmiStack fluid, EmiStack container, EmiStack emptyContainer) {
        this.id = id;
        this.fluid = fluid;
        this.container = container;
        this.emptyContainer = emptyContainer;
        if (!emptyContainer.isEmpty()) {
            inputs = List.of(fluid, emptyContainer);
        } else {
            inputs = List.of(fluid);
        }
        outputs = List.of(container);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ExMIPlugin.FLUID_INTO_CONTAINER_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        if (emptyContainer.isEmpty()) {
            return 18 + 24 + 18;
        } else {
            return 18 + 24 + 18 + 24 + 18;
        }
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int offset;
        if (!emptyContainer.isEmpty()) {
            widgets.addSlot(emptyContainer, 0, 0);
            widgets.addTexture(ExMITextures.PLUS_LARGE_SYMBOL, 18 + 4, 1);
            offset = 18 + 24;
        } else {
            offset = 0;
        }

        widgets.addSlot(fluid, offset, 0);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, offset + 18 + 4, 1);
        widgets.addSlot(container, offset + 18 + 24, 0).recipeContext(this);
    }
}
