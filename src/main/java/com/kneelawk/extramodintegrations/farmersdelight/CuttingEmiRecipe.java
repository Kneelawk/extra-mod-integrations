package com.kneelawk.extramodintegrations.farmersdelight;

import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class CuttingEmiRecipe implements EmiRecipe {
    private static final Identifier GUI_TEXTURE = new Identifier("farmersdelight", "textures/gui/rei/cutting_board.png");

    private final Identifier id;
    private final EmiIngredient input;
    private final EmiIngredient tool;
    private final List<EmiStack> resultList;

    public CuttingEmiRecipe(CuttingBoardRecipe recipe) {
        this.id = recipe.getId();
        this.input = EmiIngredient.of(recipe.getIngredients().get(0));
        this.tool = EmiIngredient.of(recipe.getTool());
        this.resultList = Stream.concat(
                recipe.getMandatoryResult().stream().map(EmiStack::of),
                recipe.getVariableResult().stream().map(r -> EmiStack.of(r.stack()).setChance(r.chance()))
        ).toList();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FDIntegration.CUTTING_CATEGORY;
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
    public List<EmiIngredient> getCatalysts() {
        return List.of(tool);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return resultList;
    }

    @Override
    public int getDisplayWidth() {
        return 110;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_TEXTURE, 0, 0, 73, 44, 4, 7);

        widgets.addSlot(tool, 11, 0)
                .drawBack(false)
                .catalyst(true);
        widgets.addSlot(input, 11, 19)
                .drawBack(false);

        int maxOutputWidth = resultList.size() <= 1 ? 18 : 37;
        int maxOutputHeight = 18 + (resultList.size() - 1) / 2 * 19;
        Bounds outputBounds = centeredInto(
                new Bounds(73, 4, 36, 36),
                maxOutputWidth,
                maxOutputHeight
        );

        for (int i = 0; i < resultList.size(); i++) {
            int x = outputBounds.x() + i % 2 * 19;
            int y = outputBounds.y() + i / 2 * 19;
            widgets.addTexture(GUI_TEXTURE, x, y, 18, 18,
                    resultList.get(i).getChance() < 1 ? 18 : 0,
                    58);
            widgets.addSlot(resultList.get(i), x, y)
                    .drawBack(false)
                    .recipeContext(this);
        }
    }

    private static Bounds centeredInto(Bounds origin, int width, int height) {
        return new Bounds(
                origin.x() + (origin.width() - width) / 2,
                origin.y() + (origin.height() - height) / 2,
                width,
                height
        );
    }
}
