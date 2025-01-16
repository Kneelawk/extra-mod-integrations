package com.kneelawk.exmi.core.fabric.impl.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import com.kneelawk.exmi.core.api.util.RecipeReverseLookup;

@Mixin(RecipeManager.class)
public class Mixin_RecipeManager {
    @Inject(method = "replaceRecipes", at = @At("RETURN"))
    private void exmi$onReplaceRecipes(Iterable<RecipeHolder<?>> recipes, CallbackInfo ci) {
        RecipeReverseLookup.register((RecipeManager) (Object) this);
    }
}
