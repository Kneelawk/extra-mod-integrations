package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.common.registry.ModBlocks;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.List;

public class ElectroStaticGridEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_ELECTROGRID, 0, 0, 36, 36, 36, 36, 36, 36);
    
    private final EmiIngredient input;
    private final EmiIngredient catalyst;
    
    public ElectroStaticGridEmiRecipe(ResourceLocation id, Block block) {
        super(id);
        
        this.input = EmiStack.of(block);
        this.catalyst = EmiStack.of(ModBlocks.ELECTROSTATIC_COMPRESSOR.get());
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.ELECTRO_GRID;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(catalyst);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addSlot(catalyst, 0, 0)
            .drawBack(false);
        widgets.addSlot(input, 0, 18)
            .drawBack(false);

        widgets.addTooltipText(List.of(Component.translatable("pneumaticcraft.gui.nei.recipe.electrostaticGrid")), 18, 18, 16, 16);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
