package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;

import java.util.List;

public abstract class AbstractMeltingEmiRecipe extends BasicEmiRecipe {
    protected static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/melting.png");
    protected static final String KEY_COOLING_TIME = TConstruct.makeTranslationKey("jei", "melting.time");
    protected static final String KEY_TEMPERATURE = TConstruct.makeTranslationKey("jei", "temperature");
    protected static final String KEY_MULTIPLIER = TConstruct.makeTranslationKey("jei", "melting.multiplier");
    protected static final Text TOOLTIP_ORE = Text.translatable(TConstruct.makeTranslationKey("jei", "melting.ore"));

    public AbstractMeltingEmiRecipe(EmiRecipeCategory category, Identifier id) {
        super(category, id, 132, 40);
    }

    protected abstract int getTime();

    protected abstract int getTemperature();

    protected abstract IMeltingContainer.OreRateType getOreType();

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 132, 40, 0, 0);
        widgets.addText(Text.translatable(KEY_TEMPERATURE, getTemperature()), 56, 3, 0x808080, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        widgets.addFillingArrow(56, 18, getTime() * 200)
                .tooltipText(List.of(Text.translatable(KEY_COOLING_TIME, getTime() / 5)));

        if (getOreType() != null)
            widgets.addTexture(BACKGROUND_LOC, 87, 31, 6, 6, 132, 34)
                    .tooltipText(List.of(TOOLTIP_ORE));
    }
}
