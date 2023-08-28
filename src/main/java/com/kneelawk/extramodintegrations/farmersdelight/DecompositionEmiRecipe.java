package com.kneelawk.extramodintegrations.farmersdelight;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DecompositionEmiRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier("farmersdelight", "textures/gui/rei/decomposition.png");

    private final EmiIngredient base;
    private final EmiStack enriched;
    private final EmiIngredient modifier;

    public DecompositionEmiRecipe(EmiIngredient base, EmiIngredient modifier, EmiStack enriched) {
        this.base = base;
        this.modifier = modifier;
        this.enriched = enriched;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FDIntegration.DECOMPOSITION_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(base);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(modifier);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(enriched);
    }

    @Override
    public int getDisplayWidth() {
        return 102;
    }

    @Override
    public int getDisplayHeight() {
        return 62;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_TEXTURE, 0, 0, 102, 40, 8, 9);

        widgets.addSlot(base, 0, 16)
                .drawBack(false);
        widgets.addSlot(enriched, 84, 16)
                .drawBack(false)
                .recipeContext(this);
        widgets.addTexture(GUI_TEXTURE, 55, 44, 18, 18, 119, 0);
        widgets.addSlot(modifier, 55, 44)
                .drawBack(false)
                .catalyst(true);

        widgets.addTooltipText(
                List.of(Text.translatable("farmersdelight.rei.decomposition.light")),
                33, 30, 10, 10
        );
        widgets.addTooltipText(
                List.of(Text.translatable("farmersdelight.rei.decomposition.fluid")),
                46, 30, 10, 10
        );
        widgets.addTooltipText(
                List.of(Text.translatable("farmersdelight.rei.decomposition.accelerators")),
                59, 30, 10, 10
        );
    }
}
