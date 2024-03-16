package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;
import slimeknights.tconstruct.smeltery.block.entity.module.FuelModule;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class MeltingEmiRecipe extends AbstractMeltingEmiRecipe {
    private final int time;
    private final int temperature;
    private final IMeltingContainer.OreRateType oreRateType;
    
    public static MeltingEmiRecipe of(MeltingRecipe recipe) {
        ItemStack[] inputStacks = recipe.getInput().getMatchingStacks();
        Identifier id;
        if (inputStacks.length > 0) {
            Identifier inputId = Registries.ITEM.getId(inputStacks[0].getItem());
            id = recipe.getId().withSuffixedPath("/" + inputId.getNamespace() + "/" + inputId.getPath());
        } else {
            id = recipe.getId();
        }
        return new MeltingEmiRecipe(recipe, id);
    }

    private MeltingEmiRecipe(MeltingRecipe recipe, Identifier id) {
        super(TiCCategories.MELTING, id);

        this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        this.outputs = List.of(Util.convertFluid(recipe.getOutput()));

        this.time = recipe.getTime();
        this.temperature = recipe.getTemperature();
        this.oreRateType = recipe.getOreType();
    }

    @Override
    protected int getTime() {
        return time;
    }

    @Override
    protected int getTemperature() {
        return temperature;
    }

    @Override
    protected IMeltingContainer.OreRateType getOreType() {
        return oreRateType;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        if (temperature <= FuelModule.SOLID_TEMPERATURE) {
            widgets.addTexture(BACKGROUND_LOC, 1, 19, 18, 20, 164, 0);
        }

        widgets.addSlot(inputs.get(0), 23, 17)
                .drawBack(false);

        widgets.add(new TankWidget(outputs.get(0), 95, 3, 34, 34, FluidValues.METAL_BLOCK))
                .drawBack(false)
                .recipeContext(this);

        int fuelHeight;
        if (temperature <= FuelModule.SOLID_TEMPERATURE) {
            fuelHeight = 15;
            widgets.addSlot(EmiIngredient.of(MeltingFuelHandler.SOLID_FUELS.get().stream().map(EmiStack::of).toList()), 1, 21)
                    .drawBack(false);
        } else {
            fuelHeight = 32;
        }

        widgets.add(new TankWidget(EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature).stream().map(Util::convertFluid).toList()),
                3, 3, 14, fuelHeight + 2, 1))
                .drawBack(false);
    }
}
