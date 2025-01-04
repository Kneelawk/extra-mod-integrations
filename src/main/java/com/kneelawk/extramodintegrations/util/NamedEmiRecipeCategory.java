package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NamedEmiRecipeCategory extends EmiRecipeCategory {
    private final Component name;

    public NamedEmiRecipeCategory(ResourceLocation id, EmiRenderable icon, Component name) {
        super(id, icon);
        this.name = name;
    }

    public NamedEmiRecipeCategory(ResourceLocation id, EmiRenderable icon, EmiRenderable simplified, Component name) {
        super(id, icon, simplified);
        this.name = name;
    }

    @Override
    public Component getName() {
        return name;
    }
}
