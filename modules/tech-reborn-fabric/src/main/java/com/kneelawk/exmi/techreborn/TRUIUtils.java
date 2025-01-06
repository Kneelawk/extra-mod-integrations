package com.kneelawk.exmi.techreborn;

import java.util.List;

import dev.emi.emi.api.widget.WidgetHolder;
import reborncore.common.crafting.RebornRecipe;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import static com.kneelawk.exmi.techreborn.TRConstants.tooltip;

public class TRUIUtils {
    public static void energyBar(WidgetHolder widgets, RebornRecipe recipe, int machineEnergy, int x, int y) {
        widgets.addTexture(TRTextures.ENERGY_BAR_EMPTY, x, y).tooltip((mx, my) -> List.of(
            ClientTooltipComponent.create(tooltip("recipe_power", recipe.power()).getVisualOrderText())));
        widgets.addAnimatedTexture(TRTextures.ENERGY_BAR_FULL, x, y, machineEnergy * 1000 / recipe.power() * 50, false,
            true, true);
    }

    public static void arrowRight(WidgetHolder widgets, RebornRecipe recipe, int x, int y) {
        widgets.addTexture(TRTextures.ARROW_RIGHT_EMPTY, x, y);
        widgets.addAnimatedTexture(TRTextures.ARROW_RIGHT_FULL, x, y, recipe.time() * 50, true, false, false);
    }

    public static void arrowLeft(WidgetHolder widgets, RebornRecipe recipe, int x, int y) {
        widgets.addTexture(TRTextures.ARROW_LEFT_EMPTY, x, y);
        widgets.addAnimatedTexture(TRTextures.ARROW_LEFT_FULL, x, y, recipe.time() * 50, true, true, false);
    }

    public static void arrowUp(WidgetHolder widgets, RebornRecipe recipe, int x, int y) {
        widgets.addTexture(TRTextures.ARROW_UP_EMPTY, x, y);
        widgets.addAnimatedTexture(TRTextures.ARROW_UP_FULL, x, y, recipe.time() * 50, false, true, false);
    }
}
