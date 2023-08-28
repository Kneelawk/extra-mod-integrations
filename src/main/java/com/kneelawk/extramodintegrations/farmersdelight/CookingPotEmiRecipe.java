package com.kneelawk.extramodintegrations.farmersdelight;

import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CookingPotEmiRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier("farmersdelight", "textures/gui/cooking_pot.png");

    private final Identifier id;
    private final List<EmiIngredient> inputs;
    private final EmiIngredient containerInput;
    private final EmiStack output;
    private final int cookTime;

    public CookingPotEmiRecipe(CookingPotRecipe recipe) {
        this.id = recipe.getId();
        this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        this.output = EmiStack.of(recipe.getOutput(null));
        this.containerInput = EmiStack.of(recipe.getContainer());
        this.cookTime = recipe.getCookTime();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FDIntegration.COOKING_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> result = new ArrayList<>();
        result.add(containerInput);
        result.addAll(inputs);
        return result;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 116;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_TEXTURE, 0, 0, 116, 56, 29, 16);
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), i % 3 * 18, i / 3 * 18)
                    .drawBack(false);
        }
        widgets.addSlot(containerInput, 62, 38)
                .drawBack(false);
        widgets.addSlot(output, 94, 11)
                .drawBack(false)
                .recipeContext(this);
        widgets.addSlot(output, 94, 38)
                .drawBack(false)
                .recipeContext(this);

        widgets.addTexture(GUI_TEXTURE, 18, 39, 17, 15, 176, 0);
        widgets.addFillingArrow(61, 10, cookTime * 50)
                .tooltipText(List.of(Text.translatable("emi.cooking.time", cookTime / 20f)));
    }
}
