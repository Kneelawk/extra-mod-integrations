package com.kneelawk.extramodintegrations.tconstruct.recipe;

import com.google.common.collect.Streams;
import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.util.List;

public class AlloyEmiRecipe extends BasicEmiRecipe {
    private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/alloy.png");
    private static final String KEY_TEMPERATURE = TConstruct.makeTranslationKey("jei", "temperature");

    private final int temperature;

    public AlloyEmiRecipe(AlloyRecipe recipe) {
        super(TiCCategories.ALLOY, recipe.getId(), 172, 62);

        this.inputs = recipe.getDisplayInputs()
                .stream()
                .map(l -> l.stream().map(Util::convertFluid).toList())
                .map(EmiIngredient::of)
                .toList();
        this.outputs = List.of(Util.convertFluid(recipe.getOutput()));

        this.temperature = recipe.getTemperature();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 172, 62, 0, 0);

        widgets.addText(Text.translatable(KEY_TEMPERATURE, temperature), 102, 5, 0x808080, false)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        long maxAmount = Streams.concat(inputs.stream(), outputs.stream())
                .mapToLong(EmiIngredient::getAmount)
                .max()
                .orElse(FluidConstants.BUCKET);

        int w = 48 / inputs.size();
        for (int i = 0; i < inputs.size(); i++) {
            int x = 19 + i * w;
            widgets.add(new TankWidget(inputs.get(i), x - 1, 10, w + 2, 34, maxAmount))
                    .drawBack(false);
        }

        widgets.add(new TankWidget(outputs.get(0), 136, 10, 18, 34, maxAmount))
                .drawBack(false)
                .recipeContext(this);

        widgets.addSlot(EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature).stream().map(Util::convertFluid).toList()), 93, 42)
                .drawBack(false);
    }
}
