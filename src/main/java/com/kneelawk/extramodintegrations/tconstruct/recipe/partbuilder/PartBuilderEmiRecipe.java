package com.kneelawk.extramodintegrations.tconstruct.recipe.partbuilder;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.stack.PatternEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.recipe.partbuilder.IDisplayPartBuilderRecipe;
import slimeknights.tconstruct.plugin.jei.partbuilder.MaterialItemList;

import java.util.List;

public class PartBuilderEmiRecipe extends BasicEmiRecipe {
    private static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");
    private static final String KEY_COST = TConstruct.makeTranslationKey("jei", "part_builder.cost");

    private final EmiIngredient pattern;
    private final MaterialVariant material;
    private final EmiIngredient materialIngredient;
    private final int cost;
    private final EmiIngredient patternItems;

    public PartBuilderEmiRecipe(IDisplayPartBuilderRecipe recipe) {
        super(TiCCategories.PART_BUILDER, recipe.getId(), 121, 46);

        this.material = recipe.getMaterial();
        this.materialIngredient = EmiIngredient.of(MaterialItemList.getItems(material.getId()).stream().map(EmiStack::of).toList());
        this.cost = recipe.getCost();
        this.patternItems = EmiIngredient.of(recipe.getPatternItems().stream().map(EmiStack::of).toList());
        this.pattern = new PatternEmiStack(recipe.getPattern());

        this.inputs = List.of(materialIngredient, patternItems);
        this.outputs = List.of(EmiStack.of(recipe.getOutput(null)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 121, 46, 0, 117);

        widgets.addText(MaterialTooltipCache.getColoredDisplayName(material.getId()), 3, 2, -1, true);
        widgets.addText(Text.translatable(KEY_COST, cost), 3, 35, 0x808080, false);

        widgets.addSlot(materialIngredient, 24, 15)
                .drawBack(false);
        widgets.addSlot(patternItems, 3, 15)
                .drawBack(false);
        widgets.addSlot(pattern, 45, 15)
                .drawBack(false);

        widgets.addSlot(outputs.get(0), 91, 10)
                .drawBack(false)
                .large(true)
                .recipeContext(this);
    }
}
