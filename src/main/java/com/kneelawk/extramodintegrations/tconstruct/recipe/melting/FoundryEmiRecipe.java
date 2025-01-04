package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import com.kneelawk.extramodintegrations.tconstruct.recipe.TiCTankWidget;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FoundryEmiRecipe extends AbstractMeltingEmiRecipe {
    private final int time;
    private final int temperature;
    private final IMeltingContainer.OreRateType oreRateType;
    private final List<Supplier<List<Component>>> outputsTiCTooltip;
    
    public static FoundryEmiRecipe of(MeltingRecipe recipe) {
        ItemStack[] inputStacks = recipe.getInput().getItems();
        ResourceLocation id;
        if (inputStacks.length > 0) {
            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(recipe.getInput().getItems()[0].getItem());
            id = new ResourceLocation(ExMIMod.MOD_ID, "/tconstruct/foundry/" + recipe.getId().getNamespace() + "/" + recipe.getId().getPath() + "/" + inputId.getNamespace() + "/" + inputId.getPath());
        } else {
            id = new ResourceLocation(ExMIMod.MOD_ID, "/tconstruct/foundry/" + recipe.getId().getNamespace() + "/" + recipe.getId().getPath());
        }
        return new FoundryEmiRecipe(recipe, id);
    }

    private FoundryEmiRecipe(MeltingRecipe recipe, ResourceLocation id) {
        super(TiCCategories.FOUNDRY, id);

        this.time = recipe.getTime();
        this.temperature = recipe.getTemperature();
        this.oreRateType = recipe.getOreType();

        this.inputs = List.of(EmiIngredient.of(recipe.getInput()));
        this.outputs = recipe.getOutputWithByproducts()
                .stream()
                .flatMap(Collection::stream)
                .map(Util::convertFluid)
                .toList();

        this.outputsTiCTooltip = recipe.getOutputWithByproducts()
                .stream()
                .flatMap(Collection::stream)
                .map(Util::getFluidTiCTooltip)
                .toList();
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

        widgets.addSlot(inputs.get(0), 23, 17)
                .drawBack(false);

        int w = 32 / outputs.size();
        for (int i = 0; i < outputs.size(); i++) {
            int x = 95 + i * w;
            int idx = i;
            widgets.add(new TiCTankWidget(outputs.get(i), x, 3, w + 2, 34, FluidValues.METAL_BLOCK))
                    .setTiCTooltipSupplier(outputsTiCTooltip.get(idx))
                    .drawBack(false)
                    .recipeContext(this);
        }

        widgets.add(new TankWidget(EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature).stream().map(Util::convertFluid).toList()), 3, 3, 14, 34, 1))
                .drawBack(false);
    }
}
