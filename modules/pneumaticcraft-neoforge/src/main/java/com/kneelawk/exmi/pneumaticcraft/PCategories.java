package com.kneelawk.exmi.pneumaticcraft;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.kneelawk.exmi.pneumaticcraft.recipe.ExplosionEmiRecipe;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import me.desht.pneumaticcraft.common.registry.ModBlocks;
import me.desht.pneumaticcraft.common.registry.ModFluids;
import me.desht.pneumaticcraft.common.registry.ModItems;
import me.desht.pneumaticcraft.lib.Textures;

public class PCategories {
    public static final EmiRecipeCategory AMADRON_TRADE = create(
        "amadron_trade",
        ModItems.AMADRON_TABLET.get()
    );
    public static final EmiRecipeCategory ASSEMBLY = create(
        "assembly",
        ModBlocks.ASSEMBLY_CONTROLLER.get()
    );
    public static final EmiRecipeCategory EXPLOSION_CRAFTING = create(
        "explosion_crafting",
        Component.translatable("pneumaticcraft.gui.nei.title.explosionCrafting"),
        ExplosionEmiRecipe.ICON
    );
    public static final EmiRecipeCategory FLUID_MIXER = create(
        "fluid_mixer",
        ModBlocks.FLUID_MIXER.get()
    );
    public static final EmiRecipeCategory HEAT_FRAME_COOLING = create(
        "heat_frame_cooling",
        Component.translatable("pneumaticcraft.gui.nei.title.heatFrameCooling"),
        EmiStack.of(ModItems.HEAT_FRAME.get())
    );
    public static final EmiRecipeCategory HEAT_PROPERTIES = create(
        "heat_properties", 
        Component.translatable("pneumaticcraft.gui.jei.title.heatProperties"),
        new EmiTexture(Textures.JEI_THERMOMETER, 0, 0, 16, 16, 16, 16, 16, 16)
    );
    public static final EmiRecipeCategory PRESSURE_CHAMBER = create(
        "pressure_chamber",
        Component.translatable("pneumaticcraft.gui.pressureChamber"),
        EmiStack.of(ModBlocks.PRESSURE_CHAMBER_WALL.get())
    );
    public static final EmiRecipeCategory REFINERY = create(
        "refinery",
        ModBlocks.REFINERY.get()
    );
    public static final EmiRecipeCategory THERMO_PLANT = create(
        "thermo_plant",
        ModBlocks.THERMOPNEUMATIC_PROCESSING_PLANT.get()
    );
    public static final EmiRecipeCategory ELECTRO_GRID = create(
        "electro_grid",
        Component.translatable("pneumaticcraft.gui.jei.title.electrostaticGrid"),
        EmiStack.of(ModBlocks.ELECTROSTATIC_COMPRESSOR.get())
    );
    public static final EmiRecipeCategory ETCHING_TANK = create(
        "etching_tank",
        ModBlocks.ETCHING_TANK.get()
    );
    public static final EmiRecipeCategory MEMORY_ESSENCE = create(
        "memory_essence",
        ModFluids.MEMORY_ESSENCE.get().getFluidType().getDescription(),
        EmiStack.of(ModItems.MEMORY_ESSENCE_BUCKET.get())
    );
    public static final EmiRecipeCategory PLASTIC_SOLIDIFYING = create(
        "plastic_solidifying",
        Component.translatable("pneumaticcraft.gui.jei.title.plasticSolidifying"),
        EmiStack.of(ModItems.PLASTIC.get())
    );
    public static final EmiRecipeCategory SPAWNER_EXTRACTION = create(
        "spawner_extraction",
        Component.translatable("pneumaticcraft.gui.jei.title.spawnerExtraction"),
        EmiStack.of(ModBlocks.SPAWNER_EXTRACTOR.get())
    );
    public static final EmiRecipeCategory UV_LIGHT_BOX = create(
        "uv_light_box",
        ModBlocks.UV_LIGHT_BOX.get()
    );
    public static final EmiRecipeCategory YEAST_CRAFTING = create(
        "yeast_crafting",
        Component.translatable("pneumaticcraft.gui.jei.title.yeastCrafting"),
        EmiStack.of(ModItems.YEAST_CULTURE_BUCKET.get())
    );

    private static EmiRecipeCategory create(String name, Block block) {
        return create(name, block.getName(), EmiStack.of(block));
    }

    private static EmiRecipeCategory create(String name, Item item) {
        return create(name, item.getDescription(), EmiStack.of(item));
    }

    private static EmiRecipeCategory create(String name, Component title, EmiRenderable icon) {
        return new EmiRecipeCategory(PIntegration.pncLoc(name), icon) {
            @Override
            public Component getName() {
                return title;
            }
        };
    }
}
