package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import com.kneelawk.extramodintegrations.tconstruct.Util;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.melting.MeltingFuelHandler;

import java.util.Collection;
import java.util.List;

public class FoundryEmiRecipe extends AbstractMeltingEmiRecipe {
    private final int time;
    private final int temperature;
    private final IMeltingContainer.OreRateType oreRateType;
    
    public static FoundryEmiRecipe of(MeltingRecipe recipe) {
        ItemStack[] inputStacks = recipe.getInput().getMatchingStacks();
        Identifier id;
        if (inputStacks.length > 0) {
            Identifier inputId = Registries.ITEM.getId(recipe.getInput().getMatchingStacks()[0].getItem());
            id = new Identifier(ExMIMod.MOD_ID, "/tconstruct/foundry/" + recipe.getId().getNamespace() + "/" + recipe.getId().getPath() + "/" + inputId.getNamespace() + "/" + inputId.getPath());
        } else {
            id = new Identifier(ExMIMod.MOD_ID, "/tconstruct/foundry/" + recipe.getId().getNamespace() + "/" + recipe.getId().getPath());
        }
        return new FoundryEmiRecipe(recipe, id);
    }

    private FoundryEmiRecipe(MeltingRecipe recipe, Identifier id) {
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
            widgets.add(new TankWidget(outputs.get(i), x, 3, w + 2, 34, FluidValues.METAL_BLOCK))
                    .drawBack(false)
                    .recipeContext(this);
        }

        widgets.add(new TankWidget(EmiIngredient.of(MeltingFuelHandler.getUsableFuels(temperature).stream().map(Util::convertFluid).toList()), 3, 3, 14, 34, 1))
                .drawBack(false);
    }
}
