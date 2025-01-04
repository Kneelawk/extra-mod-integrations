package com.kneelawk.extramodintegrations.tconstruct.recipe;

import com.google.common.collect.Lists;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

public class TiCTankWidget extends TankWidget {

    protected Supplier<List<Component>> tiCTooltipSupplier;

    public TiCTankWidget(EmiIngredient stack, int x, int y, int width, int height, long capacity) {
        super(stack, x, y, width, height, capacity);
    }

    public TiCTankWidget setTiCTooltipSupplier(Supplier<List<Component>> tiCTooltipSupplier) {
        this.tiCTooltipSupplier = tiCTooltipSupplier;
        return this;
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        List<ClientTooltipComponent> list = Lists.newArrayList();
        if (getStack().isEmpty()) {
            return list;
        }

        list.addAll(tiCTooltipSupplier == null ? getStack().getTooltip() : tiCTooltipSupplier.get().stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList());

        addSlotTooltip(list);
        return list;
    }
}
