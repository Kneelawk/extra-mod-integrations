package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import com.kneelawk.extramodintegrations.util.stack.EntityEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.util.List;

public class EntityMeltingEmiRecipe extends BasicEmiRecipe {
    public static final Identifier BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/melting.png");

    private final int damage;

    public EntityMeltingEmiRecipe(EntityMeltingRecipe recipe) {
        super(TiCCategories.ENTITY_MELTING, recipe.getId(), 150, 62);

        this.inputs = List.of(
                EmiIngredient.of(recipe.getEntityInputs().stream().map(EntityEmiStack::new).toList()),
                EmiIngredient.of(recipe.getItemInputs().stream().map(EmiStack::of).toList())
        );
        this.outputs = List.of(Util.convertFluid(recipe.getOutput()));
        this.damage = recipe.getDamage();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 150, 62, 0, 41);

        widgets.addText(Text.literal(Float.toString(damage / 2f)), 84, 8, 0xff0000, false)
                .horizontalAlign(TextWidget.Alignment.END);

        widgets.addSlot(inputs.get(0), 18, 10)
                .drawBack(false);

        widgets.addSlot(EmiIngredient.of(MeltingFuelHandler.getUsableFuels(1).stream().map(Util::convertFluid).toList()), 74, 42)
                .drawBack(false);

        widgets.add(new TankWidget(outputs.get(0), 114, 10, 18, 34, FluidValues.INGOT * 2))
                .drawBack(false)
                .recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
