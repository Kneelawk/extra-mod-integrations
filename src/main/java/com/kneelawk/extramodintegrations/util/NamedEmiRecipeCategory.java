package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NamedEmiRecipeCategory extends EmiRecipeCategory {
    private final Text name;

    public NamedEmiRecipeCategory(Identifier id, EmiRenderable icon, Text name) {
        super(id, icon);
        this.name = name;
    }

    @Override
    public Text getName() {
        return name;
    }
}
