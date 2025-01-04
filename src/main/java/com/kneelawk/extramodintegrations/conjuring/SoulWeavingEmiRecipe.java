package com.kneelawk.extramodintegrations.conjuring;

import com.glisco.conjuring.blocks.soul_weaver.SoulWeaverRecipe;
import com.glisco.conjuring.items.ConjuringItems;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class SoulWeavingEmiRecipe implements EmiRecipe {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("conjuring", "textures/gui/soul_weaver.png");

    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;
    private final EmiIngredient essence = EmiStack.of(ConjuringItems.CONJURATION_ESSENCE);

    public SoulWeavingEmiRecipe(SoulWeaverRecipe recipe) {
        this.id = recipe.getId();
        this.inputs = recipe.getInputs().stream().map(EmiIngredient::of).toList();
        this.output = EmiStack.of(recipe.getResultItem(null));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ConjuringIntegration.SOUL_WEAVING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> result = new ArrayList<>(inputs);
        result.add(essence);
        return result;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 156;
    }

    @Override
    public int getDisplayHeight() {
        return 100;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_TEXTURE, 1, 1, 154, 96, 0, 0);

        widgets.addSlot(inputs.get(0), 69, 21);
        widgets.addSlot(inputs.get(1), 0, 0);
        widgets.addSlot(inputs.get(2), 0, 42);
        widgets.addSlot(inputs.get(3), 138, 0);
        widgets.addSlot(inputs.get(4), 138, 42);

        widgets.addSlot(essence, 16, 78);

        widgets.addSlot(output, 65, 74)
                .large(true)
                .recipeContext(this);
    }
}
