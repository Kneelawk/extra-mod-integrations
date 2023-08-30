package com.kneelawk.extramodintegrations.conjuring;

import com.glisco.conjuring.blocks.soulfire_forge.SoulfireForgeRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulfireForgeEmiRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier("conjuring", "textures/gui/soulfire_forge.png");

    private final Identifier id;
    private final int smeltTime;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;

    public SoulfireForgeEmiRecipe(SoulfireForgeRecipe recipe) {
        this.id = recipe.getId();
        this.smeltTime = recipe.getSmeltTime();
        this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        this.output = EmiStack.of(recipe.getOutput(null));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ConjuringIntegration.SOULFIRE_FORGE;
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
        return 118;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for (int i = 0; i < 9; i++) {
            widgets.addSlot(inputs.get(i), i % 3 * 18, i / 3 * 18);
        }

        widgets.addTexture(GUI_TEXTURE, 58, 13, 32, 32, 90, 25);
        widgets.addAnimatedTexture(GUI_TEXTURE, 57, 12, 32, 32, 176, 0, smeltTime * 50, false, true, false)
                .tooltipText(List.of(Text.translatable("emi.cooking.time", smeltTime / 20f)));

        widgets.addSlot(output, 92, 14)
                .large(true)
                .recipeContext(this);
    }
}
