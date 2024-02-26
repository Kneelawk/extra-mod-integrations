package com.kneelawk.extramodintegrations.tconstruct.recipe.melting;

import com.kneelawk.extramodintegrations.tconstruct.TiCCategories;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;

public class EntityMeltingEmiRecipe extends BasicEmiRecipe {
    public EntityMeltingEmiRecipe(EntityMeltingRecipe recipe) {
        super(TiCCategories.ENTITY_MELTING, recipe.getId(), 150, 62);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
