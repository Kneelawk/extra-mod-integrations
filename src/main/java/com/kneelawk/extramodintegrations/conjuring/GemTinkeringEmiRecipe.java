package com.kneelawk.extramodintegrations.conjuring;

import com.glisco.conjuring.blocks.gem_tinkerer.GemTinkererRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GemTinkeringEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;

    public GemTinkeringEmiRecipe(GemTinkererRecipe recipe) {
        this.id = recipe.getId();
        this.inputs = recipe.getInputs().stream().map(EmiIngredient::of).toList();
        this.output = EmiStack.of(recipe.getOutput(null));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ConjuringIntegration.GEM_TINKERING;
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
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 112;
    }

    @Override
    public int getDisplayHeight() {
        return 58;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 20, 20);
        widgets.addSlot(inputs.get(1), 20, 0);
        widgets.addSlot(inputs.get(2), 40, 20);
        widgets.addSlot(inputs.get(3), 20, 40);
        widgets.addSlot(inputs.get(4), 0, 20);

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 20);

        widgets.addSlot(output, 86, 16)
                .large(true)
                .recipeContext(this);
    }
}
