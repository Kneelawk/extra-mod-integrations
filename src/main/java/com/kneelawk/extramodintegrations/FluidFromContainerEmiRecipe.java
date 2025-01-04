package com.kneelawk.extramodintegrations;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class FluidFromContainerEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiStack fluid;
    private final EmiStack container;
    private final EmiStack emptyContainer;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public FluidFromContainerEmiRecipe(ResourceLocation id, EmiStack fluid, EmiStack container, EmiStack emptyContainer) {
        this.id = id;
        this.fluid = fluid;
        this.container = container;
        this.emptyContainer = emptyContainer;
        inputs = List.of(container);
        if (!emptyContainer.isEmpty()) {
            outputs = List.of(fluid, emptyContainer);
        } else {
            outputs = List.of(fluid);
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ExMIPlugin.FLUID_FROM_CONTAINER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
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
        widgets.addSlot(container, 0, 0);
        widgets.addTexture(ExMITextures.RIGHT_ARROW, 18 + 4, 1);
        widgets.addSlot(fluid, 18 + 24, 0).recipeContext(this);

        if (!emptyContainer.isEmpty()) {
            widgets.addTexture(ExMITextures.PLUS_LARGE_SYMBOL, 18 + 24 + 18 + 4, 1);
            widgets.addSlot(emptyContainer, 18 + 24 + 18 + 24, 0).recipeContext(this);
        }
    }
}
