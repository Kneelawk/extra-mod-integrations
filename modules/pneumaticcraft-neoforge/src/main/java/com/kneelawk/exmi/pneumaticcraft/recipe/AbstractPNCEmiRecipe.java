package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.TemperatureRange;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature;
import me.desht.pneumaticcraft.client.render.pressure_gauge.PressureGaugeRenderer2D;
import me.desht.pneumaticcraft.common.heat.HeatUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractPNCEmiRecipe implements EmiRecipe {
    
    protected final ResourceLocation id;
    
    public AbstractPNCEmiRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }
    
    @Override
    public int getDisplayWidth() {
        return getBackground().width;
    }

    @Override
    public int getDisplayHeight() {
        return getBackground().height;
    }

    protected abstract EmiTexture getBackground();

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(getBackground(), 0, 0);
    }
    
    protected static void addPressureGauge(WidgetHolder widgets, int x, int y, float minPressure, float maxPressure, float dangerousPressure, float requiredPressure) {
        addPressureGauge(widgets, x, y, minPressure, maxPressure, dangerousPressure, requiredPressure, 1f);
    }

    protected static void addPressureGauge(WidgetHolder widgets, int x, int y, float minPressure, float maxPressure, float dangerousPressure, float requiredPressure, float airUsageMultiplier) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("pneumaticcraft.gui.tooltip.pressure", requiredPressure));
        if (airUsageMultiplier != 1f) {
            tooltip.add(Component.translatable("pneumaticcraft.gui.tab.info.pneumatic_armor.usage")
                .append(" x")
                .append(String.format("%.1f", airUsageMultiplier))
                .withStyle(ChatFormatting.GRAY));
        }
        widgets.addDrawable(x - 20, y - 20, 40, 40, (graphics, mouseX, mouseY, delta) -> {
            float pressure = requiredPressure * ((float) (System.currentTimeMillis() % (60 * 50)) / (60 * 50));
            PressureGaugeRenderer2D.drawPressureGauge(graphics, Minecraft.getInstance().font, minPressure, maxPressure, dangerousPressure, requiredPressure, pressure, 20, 20);
        }).tooltipText(tooltip);
    }
    
    protected static void addTemperatureGauge(WidgetHolder widgets, int x, int y, TemperatureRange operatingTemp) {
        WidgetTemperature w = WidgetTemperature.fromOperatingRange(0, 0, operatingTemp);
        widgets.addDrawable(x, y, w.getWidth(), w.getHeight(), (graphics, mouseX, mouseY, delta) -> {
            w.setTemperature(w.getTotalRange().getMin() + (w.getTotalRange().getMax() - w.getTotalRange().getMin()) * (int) (System.currentTimeMillis() % (60 * 50)) / (60 * 50));
            w.renderWidget(graphics, mouseX, mouseY, delta);
        }).tooltipText(List.of(
            HeatUtil.formatHeatString(operatingTemp.asString(TemperatureRange.TemperatureScale.CELSIUS))));

    }
}
