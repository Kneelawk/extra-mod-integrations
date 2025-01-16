package com.kneelawk.exmi.core.neoforge.impl;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;

import com.kneelawk.exmi.core.api.ExMIConstants;
import com.kneelawk.exmi.core.api.util.RecipeReverseLookup;

@EventBusSubscriber(value = Dist.CLIENT, modid = ExMIConstants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ExMIClientEvents {
    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        RecipeReverseLookup.register(event.getRecipeManager());
    }
}
