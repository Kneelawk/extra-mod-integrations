package com.kneelawk.exmi.pneumaticcraft.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.fluids.FluidStack;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.common.block.entity.processing.UVLightBoxBlockEntity;
import me.desht.pneumaticcraft.lib.Textures;

import java.util.List;
import java.util.Random;

public class EtchingTankEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_ETCHING_TANK, 0, 0, 83, 42);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_JEI_ETCHING_TANK, 83, 0, 42, 42);
    
    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack failed;
    private final EmiStack etchingFluid;
    private final int uniq = new Random().nextInt();

    public EtchingTankEmiRecipe(ResourceLocation id, ItemStack input, ItemStack output, ItemStack failed, FluidStack etchingFluid) {
        super(id);
        
        this.input = input;
        this.output = output;
        this.failed = failed;
        this.etchingFluid = NeoForgeEmiStack.of(etchingFluid);
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.ETCHING_TANK;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiStack.of(input));
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(etchingFluid);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(output), EmiStack.of(failed));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addAnimatedTexture(PROGRESS_BAR, 20, 0, 60 * 50, true, false, false);
        
        widgets.addGeneratedSlot(r -> getStack(r, 0), uniq, 0, 12)
            .drawBack(false);
        widgets.addSlot(etchingFluid, 25, 12)
            .drawBack(false);
        widgets.addGeneratedSlot(r -> getStack(r, 1), uniq, 65, 0)
            .recipeContext(this);
        widgets.addGeneratedSlot(r -> getStack(r, 2), uniq, 65, 24)
            .recipeContext(this);
    }
    
    private EmiIngredient getStack(Random random, int idx) {
        int p = random.nextInt(101);
        ItemStack pcbStack = input.copy();
        UVLightBoxBlockEntity.setExposureProgress(pcbStack, p);
        return new EmiIngredient[] {
            EmiStack.of(pcbStack),
            EmiStack.of(output).setChance(p / 100f),
            EmiStack.of(failed).setChance((100 - p) / 100f)
        }[idx];
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
