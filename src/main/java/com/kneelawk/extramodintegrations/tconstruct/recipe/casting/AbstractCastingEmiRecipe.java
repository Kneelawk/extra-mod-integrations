package com.kneelawk.extramodintegrations.tconstruct.recipe.casting;

import com.kneelawk.extramodintegrations.tconstruct.Util;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

import java.util.List;

public abstract class AbstractCastingEmiRecipe extends BasicEmiRecipe {
    public static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/casting.png");
    private static final String KEY_COOLING_TIME = TConstruct.makeTranslationKey("jei", "time");
    private static final String KEY_CAST_KEPT = TConstruct.makeTranslationKey("jei", "casting.cast_kept");
    private static final String KEY_CAST_CONSUMED = TConstruct.makeTranslationKey("jei", "casting.cast_consumed");
    protected final EmiIngredient fluidInput;
    protected final EmiIngredient castItem;
    protected final boolean castIsConsumed;
    protected final boolean hasCast;
    protected final int coolingTime;
    protected final EmiTexture block;

    public AbstractCastingEmiRecipe(EmiRecipeCategory category, IDisplayableCastingRecipe recipe, EmiTexture block) {
        super(category, null, 117, 54);

        this.fluidInput = EmiIngredient.of(recipe.getFluids().stream().map(Util::convertFluid).toList());
        this.castItem = EmiIngredient.of(recipe.getCastItems().stream().map(EmiStack::of).toList());
        this.outputs = List.of(EmiStack.of(recipe.getOutput()));
        this.castIsConsumed = recipe.isConsumed();
        this.hasCast = recipe.hasCast();
        this.coolingTime = recipe.getCoolingTime();
        this.block = block;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return castIsConsumed ? List.of(fluidInput, castItem) : List.of(fluidInput);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return castIsConsumed ? List.of() : List.of(castItem);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 117, 54, 0, 0);

        if (!castItem.isEmpty()) {
            widgets.addSlot(castItem, 37, 18)
                    .drawBack(false)
                    .catalyst(!castIsConsumed);
        }

        widgets.addSlot(outputs.get(0), 92, 17)
                .drawBack(false)
                .recipeContext(this);

        widgets.add(new TankWidget(fluidInput, 2, 2, 34, 34, FluidValues.METAL_BLOCK))
                .drawBack(false);

        int h = hasCast ? 11 : 27;
        widgets.add(new TankWidget(fluidInput, 42, 7, 8, h + 2, 1))
                .drawBack(false);

        widgets.addFillingArrow(58, 18, coolingTime * 50)
                .tooltipText(List.of(Text.translatable(KEY_COOLING_TIME, coolingTime / 20)));
        widgets.addTexture(block, 38, 35);
    }
}
