package com.kneelawk.extramodintegrations;

import com.kneelawk.extramodintegrations.util.NinePatchTexture;
import dev.emi.emi.api.render.EmiTexture;
import net.minecraft.resources.ResourceLocation;

import static com.kneelawk.extramodintegrations.ExMIMod.id;

public class ExMITextures {
    public static final ResourceLocation SIMPLIFIED_ICONS = id("textures/gui/simplified_icons.png");
    public static final ResourceLocation WIDGETS = id("textures/gui/widgets.png");

    public static final EmiTexture GRINDING = new EmiTexture(SIMPLIFIED_ICONS, 0, 0, 16, 16);
    public static final EmiTexture ALLOY_SMELTING = new EmiTexture(SIMPLIFIED_ICONS, 16, 0, 16, 16);
    public static final EmiTexture ASSEMBLING = new EmiTexture(SIMPLIFIED_ICONS, 32, 0, 16, 16);
    public static final EmiTexture BLAST_FURNACE = new EmiTexture(SIMPLIFIED_ICONS, 48, 0, 16, 16);
    public static final EmiTexture CENTRIFUGE = new EmiTexture(SIMPLIFIED_ICONS, 64, 0, 16, 16);
    public static final EmiTexture CHEMICAL_REACTING = new EmiTexture(SIMPLIFIED_ICONS, 80, 0, 16, 16);
    public static final EmiTexture COMPRESSING = new EmiTexture(SIMPLIFIED_ICONS, 96, 0, 16, 16);
    public static final EmiTexture DISTILLATION_TOWER = new EmiTexture(SIMPLIFIED_ICONS, 112, 0, 16, 16);
    public static final EmiTexture EXTRACTING = new EmiTexture(SIMPLIFIED_ICONS, 128, 0, 16, 16);
    public static final EmiTexture IMPLOSION_COMPRESSING = new EmiTexture(SIMPLIFIED_ICONS, 144, 0, 16, 16);
    public static final EmiTexture ELECTROLYZING = new EmiTexture(SIMPLIFIED_ICONS, 160, 0, 16, 16);
    public static final EmiTexture INDUSTRIAL_GRINDING = new EmiTexture(SIMPLIFIED_ICONS, 176, 0, 16, 16);
    public static final EmiTexture SAWMILLING = new EmiTexture(SIMPLIFIED_ICONS, 192, 0, 16, 16);
    public static final EmiTexture SCRAPBOX = new EmiTexture(SIMPLIFIED_ICONS, 208, 0, 16, 16);
    public static final EmiTexture VACUUM_FREEZING = new EmiTexture(SIMPLIFIED_ICONS, 224, 0, 16, 16);
    public static final EmiTexture FLUID_REPLICATING = new EmiTexture(SIMPLIFIED_ICONS, 240, 0, 16, 16);
    public static final EmiTexture FUSION_REACTOR = new EmiTexture(SIMPLIFIED_ICONS, 0, 16, 16, 16);
    public static final EmiTexture ROLLING_MACHINE = new EmiTexture(SIMPLIFIED_ICONS, 16, 16, 16, 16);
    public static final EmiTexture WIRE_MILLING = new EmiTexture(SIMPLIFIED_ICONS, 32, 16, 16, 16);
    public static final EmiTexture CANNING = new EmiTexture(SIMPLIFIED_ICONS, 48, 16, 16, 16);
    public static final EmiTexture FLUID_FROM_CAN = new EmiTexture(SIMPLIFIED_ICONS, 64, 16, 16, 16);
    public static final EmiTexture FLUID_INTO_CAN = new EmiTexture(SIMPLIFIED_ICONS, 80, 16, 16, 16);

    public static final NinePatchTexture SLOT_BG = new NinePatchTexture(WIDGETS, 0, 0, 3, 3, 1, 1, 1, 1, false);
    public static final EmiTexture PLUS_LARGE_SYMBOL = new EmiTexture(WIDGETS, 240, 240, 16, 16);
    public static final EmiTexture RIGHT_ARROW = new EmiTexture(WIDGETS, 224, 240, 16, 16);
}
