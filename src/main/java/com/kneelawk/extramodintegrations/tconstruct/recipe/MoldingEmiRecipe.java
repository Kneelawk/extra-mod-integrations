package com.kneelawk.extramodintegrations.tconstruct.recipe;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;

import java.util.List;

public class MoldingEmiRecipe extends BasicEmiRecipe {
    private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/casting.png");
    private final EmiIngredient pattern;
    private final EmiIngredient material;
    private final boolean isPatternConsumed;
    RecipeType<?> type;

    public MoldingEmiRecipe(MoldingRecipe recipe) {
        super(TiCCategories.MOLDING, recipe.getId(), 70, 57);

        this.material = EmiIngredient.of(recipe.getMaterial());
        this.pattern = EmiIngredient.of(recipe.getPattern());
        this.outputs = List.of(EmiStack.of(recipe.getOutput(null)));
        this.isPatternConsumed = recipe.isPatternConsumed();
        this.type = recipe.getType();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return isPatternConsumed ? List.of(material, pattern) : List.of(material);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return isPatternConsumed ? List.of() : List.of(pattern);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 70, 57, 0, 55);

        widgets.addSlot(material, 2, 23)
                .drawBack(false);
        widgets.addSlot(outputs.get(0), 50, 23)
                .drawBack(false)
                .recipeContext(this);

        if (!pattern.isEmpty()) {
            widgets.addSlot(pattern, 2, 0)
                    .drawBack(false)
                    .catalyst(!isPatternConsumed);
        }

        widgets.addTexture(BACKGROUND_LOC, 3, 40, 16, 16, 117, type == TinkerRecipeTypes.MOLDING_BASIN.get() ? 16 : 0);
        widgets.addTexture(BACKGROUND_LOC, 8, 17, 6, 6, pattern.isEmpty() ? 76 : 70, 55);
    }
}
