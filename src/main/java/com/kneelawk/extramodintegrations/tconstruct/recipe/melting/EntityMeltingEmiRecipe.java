package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import com.kneelawk.extramodintegrations.tconstruct.recipe.TiCTankWidget;
import com.kneelawk.extramodintegrations.util.stack.EntityEmiStack;
import com.kneelawk.extramodintegrations.util.widget.ScaledSlotWidget;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EntityMeltingEmiRecipe extends BasicEmiRecipe {
    public static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/melting.png");

    private final int damage;
    private final List<Supplier<List<Component>>> outputsTiCTooltip;

    public EntityMeltingEmiRecipe(EntityMeltingRecipe recipe) {
        super(TiCCategories.ENTITY_MELTING, recipe.getId(), 150, 62);

        this.inputs = List.of(
                EmiIngredient.of(recipe.getEntityInputs().stream().map(EntityEmiStack::new).toList()),
                EmiIngredient.of(recipe.getItemInputs().stream().map(EmiStack::of).toList())
        );
        this.outputs = List.of(Util.convertFluid(recipe.getOutput()));
        this.outputsTiCTooltip = List.of(Util.getFluidTiCTooltip(recipe.getOutput()));
        this.damage = recipe.getDamage();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND_LOC, 0, 0, 150, 62, 0, 41);

        widgets.addText(Component.literal(Float.toString(damage / 2f)), 84, 8, 0xff0000, false)
                .horizontalAlign(TextWidget.Alignment.END);

        widgets.add(new ScaledSlotWidget(inputs.get(0), 17, 9, 2))
                .drawBack(false);

        widgets.addSlot(EmiIngredient.of(MeltingFuelHandler.getUsableFuels(1).stream().map(Util::convertFluid).toList()), 74, 42)
                .drawBack(false);

        widgets.add(new TiCTankWidget(outputs.get(0), 114, 10, 18, 34, FluidValues.INGOT * 2))
                .setTiCTooltipSupplier(outputsTiCTooltip.get(0))
                .drawBack(false)
                .recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
