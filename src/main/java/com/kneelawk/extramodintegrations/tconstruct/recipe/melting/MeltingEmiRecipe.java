package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import com.kneelawk.extramodintegrations.tconstruct.recipe.TiCTankWidget;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;
import slimeknights.tconstruct.smeltery.block.entity.module.FuelModule;
import slimeknights.tconstruct.common.config.Config;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MeltingEmiRecipe extends AbstractMeltingEmiRecipe {
    private final int time;
    private final int temperature;
    private final IMeltingContainer.OreRateType oreRateType;
    private final List<Supplier<List<Component>>> outputsTiCTooltip;
    
    public static MeltingEmiRecipe of(MeltingRecipe recipe) {
        ItemStack[] inputStacks = recipe.getInput().getItems();
        ResourceLocation id;
        if (inputStacks.length > 0) {
            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(inputStacks[0].getItem());
            id = recipe.getId().withSuffix("/" + inputId.getNamespace() + "/" + inputId.getPath());
        } else {
            id = recipe.getId();
        }
        return new MeltingEmiRecipe(recipe, id, Config.COMMON.smelteryOreRate);
    }

    private MeltingEmiRecipe(MeltingRecipe recipe, ResourceLocation id, IMeltingContainer.IOreRate oreRate) {
        super(TiCCategories.MELTING, id);

        this.time = recipe.getTime();
        this.temperature = recipe.getTemperature();
        this.oreRateType = recipe.getOreType();

        this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        FluidStack originalOutput = recipe.getOutput();
        if (this.oreRateType != null) {
            originalOutput = oreRate.applyOreBoost(oreRateType, originalOutput);
        }
        this.outputs = List.of(Util.convertFluid(originalOutput));
        this.outputsTiCTooltip = List.of(Util.getFluidTiCTooltip(originalOutput));
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

        widgets.add(new TiCTankWidget(outputs.get(0), 95, 3, 34, 34, FluidValues.METAL_BLOCK))
                .setTiCTooltipSupplier(outputsTiCTooltip.get(0))
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
