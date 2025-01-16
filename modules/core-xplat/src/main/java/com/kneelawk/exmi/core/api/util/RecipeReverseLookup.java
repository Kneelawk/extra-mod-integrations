package com.kneelawk.exmi.core.api.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import com.kneelawk.exmi.core.api.ExMILog;

/**
 * Provides a mechanism for getting recipe holders and ids from plain recipe objects.
 */
public class RecipeReverseLookup {
    private static final Map<Recipe<?>, RecipeHolder<?>> reverseLookup = new IdentityHashMap<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Collects the recipes from the recipe manager and builds a reverse-lookup table for them.
     *
     * @param manager the recipe manager to get recipes from.
     */
    public static void register(RecipeManager manager) {
        lock.writeLock().lock();
        try {
            ExMILog.LOG.info("[Extra Mod Integrations] Loading recipe reverse lookup...");
            reverseLookup.clear();
            for (RecipeHolder<?> holder : manager.getRecipes()) {
                reverseLookup.put(holder.value(), holder);
            }
            ExMILog.LOG.info("[Extra Mod Integrations] Recipe reverse lookup loaded.");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets the recipe holder for the given recipe, if one was ever registered.
     *
     * @param recipe the recipe to look up the recipe holder for.
     * @param <T>    the type of recipe to look up.
     * @return the recipe holder for the given recipe if one was ever registered.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> @Nullable RecipeHolder<T> getHolder(T recipe) {
        lock.readLock().lock();
        try {
            return (RecipeHolder<T>) reverseLookup.get(recipe);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the id for the given recipe, if it was ever registered.
     *
     * @param recipe the recipe to look up the id of.
     * @return the id of the given recipe if it was ever registered.
     */
    public static @Nullable ResourceLocation getId(Recipe<?> recipe) {
        RecipeHolder<? extends Recipe<?>> holder = getHolder(recipe);
        if (holder != null) return holder.id();
        return null;
    }
}
