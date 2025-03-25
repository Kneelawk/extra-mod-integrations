package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.AssemblyRecipe;
import me.desht.pneumaticcraft.common.item.AssemblyProgramItem;
import me.desht.pneumaticcraft.common.recipes.assembly.AssemblyProgram;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.kneelawk.exmi.pneumaticcraft.Categories;

public class AssemblyEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND = new EmiTexture(Textures.GUI_JEI_ASSEMBLY_CONTROLLER, 5, 11, 158, 98);
    private static final EmiTexture PROGRESS_BAR = new EmiTexture(Textures.GUI_JEI_ASSEMBLY_CONTROLLER, 173, 0, 24, 17);

    private final List<EmiIngredient> machineBlocks;
    private final EmiIngredient input;
    private final EmiStack output;
    private final EmiIngredient program;

    public AssemblyEmiRecipe(RecipeHolder<AssemblyRecipe> holder) {
        super(holder.id());

        AssemblyRecipe recipe = holder.value();
        this.input = NeoForgeEmiIngredient.of(recipe.getInput());
        EmiStack program = EmiStack.of(AssemblyProgramItem.fromProgramType(recipe.getProgramType()));
        program.setRemainder(program);
        this.program = program;
        this.output = EmiStack.of(recipe.getOutput());
        this.machineBlocks = Arrays.stream(AssemblyProgram.fromRecipe(recipe).getRequiredMachines())
            .map(AssemblyProgram.EnumMachine::getMachineBlock)
            .map(EmiStack::of)
            .collect(Collectors.toList());
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Categories.ASSEMBLY;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input, program);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return machineBlocks;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        widgets.addAnimatedTexture(PROGRESS_BAR, 68, 65, 60 * 50, true, false, false);

        widgets.addText(Component.literal("Required Machines"), 5, 15, 0xFF404040, false);
        widgets.addText(Component.literal("Prog."), 129, 9, 0xFF404040, false);

        widgets.addSlot(input, 28, 55)
            .drawBack(false);
        widgets.addSlot(program, 132, 21)
            .drawBack(false);
        widgets.addSlot(output, 95, 55)
            .drawBack(false)
            .recipeContext(this);
        
        int xPos = 4;
        for (EmiIngredient machineBlock : machineBlocks) {
            widgets.addSlot(machineBlock, xPos, 24);
            xPos += 18;
        }
    }
}
