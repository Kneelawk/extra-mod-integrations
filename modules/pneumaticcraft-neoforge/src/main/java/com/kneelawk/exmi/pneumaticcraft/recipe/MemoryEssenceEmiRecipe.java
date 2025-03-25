package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.common.XPFluidManager;
import me.desht.pneumaticcraft.common.registry.ModFluids;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.exmi.pneumaticcraft.Categories;

public class MemoryEssenceEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_MEMORY_ESSENCE, 0, 0, 146, 73);
    
    private final EmiIngredient input1;
    private final EmiIngredient input2;
    private final EmiStack output;

    public MemoryEssenceEmiRecipe(ResourceLocation id, EmiIngredient input1, EmiIngredient input2) {
        super(id);
        
        this.input1 = input1;
        this.input2 = input2;
        this.output = EmiStack.of(ModFluids.MEMORY_ESSENCE.get(), 1000);
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.MEMORY_ESSENCE;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of();
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        int ratio = XPFluidManager.getInstance().getXPRatio(ModFluids.MEMORY_ESSENCE.get());
        String s = "1 XP = " + ratio + " mB";
        widgets.addText(Component.literal(s), getBackground().width / 2, 0, 0x404040, false)
            .horizontalAlign(TextWidget.Alignment.CENTER);
        
        widgets.addSlot(input1, 53, 28)
            .drawBack(false);
        widgets.addSlot(input2, 75, 28)
            .drawBack(false);
        widgets.addSlot(output, 111, 28)
            .drawBack(false)
            .recipeContext(this);
    }
}
