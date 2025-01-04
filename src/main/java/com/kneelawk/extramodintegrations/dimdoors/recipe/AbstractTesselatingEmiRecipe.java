package com.kneelawk.extramodintegrations.dimdoors.recipe;

import com.kneelawk.extramodintegrations.dimdoors.DimDoorsCategories;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractTesselatingEmiRecipe extends EmiCraftingRecipe {
    private final int weavingTime;

    public AbstractTesselatingEmiRecipe(List<EmiIngredient> inputs, EmiStack output, int weavingTime, ResourceLocation id, boolean shapeless) {
        super(inputs, output, id, shapeless);
        this.weavingTime = weavingTime;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return DimDoorsCategories.TESSELATING;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(60, 18, weavingTime * 50);
        if (shapeless) {
            widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);
        }
        int sOff = 0;
        if (!shapeless) {
            if (canFit(1, 3)) {
                sOff -= 1;
            }
            if (canFit(3, 1)) {
                sOff -= 3;
            }
        }
        for (int i = 0; i < 9; i++) {
            int s = i + sOff;
            if (s >= 0 && s < input.size()) {
                widgets.addSlot(input.get(s), i % 3 * 18, i / 3 * 18);
            } else {
                widgets.addSlot(EmiStack.of(ItemStack.EMPTY), i % 3 * 18, i / 3 * 18);
            }
        }
        widgets.addSlot(output, 92, 14).large(true).recipeContext(this);
    }
}
