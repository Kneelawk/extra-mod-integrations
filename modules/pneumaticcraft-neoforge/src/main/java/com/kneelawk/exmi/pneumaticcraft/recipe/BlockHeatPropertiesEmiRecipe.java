package com.kneelawk.exmi.pneumaticcraft.recipe;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import net.neoforged.neoforge.fluids.FluidType;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import me.desht.pneumaticcraft.api.crafting.recipe.HeatPropertiesRecipe;
import me.desht.pneumaticcraft.client.util.ClientUtils;
import me.desht.pneumaticcraft.client.util.GuiUtils;
import me.desht.pneumaticcraft.lib.Textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.exmi.pneumaticcraft.PCategories;

public class BlockHeatPropertiesEmiRecipe extends AbstractPNCEmiRecipe {
    
    private static final EmiTexture BACKGROUND_TEXTURE = new EmiTexture(Textures.GUI_JEI_HEAT_PROPERTIES, 0, 0, 146, 73);
    private static final EmiTexture HOT_AREA_TEXTURE = new EmiTexture(Textures.GUI_JEI_HEAT_PROPERTIES, 150, 0, 31, 18);
    private static final EmiTexture COLD_AREA_TEXTURE = new EmiTexture(Textures.GUI_JEI_HEAT_PROPERTIES, 150, 18, 31, 18);
    private static final EmiTexture AIR_TEXTURE = new EmiTexture(Textures.GUI_JEI_HEAT_PROPERTIES, 150, 36, 16, 16);

    private static final Bounds INPUT_AREA = new Bounds(65, 44, 18, 18);
    private static final Bounds COLD_AREA = new Bounds(5, 44, 18, 18);
    private static final Bounds HOT_AREA = new Bounds(125, 44, 18, 18);
    private static final Bounds[] OUTPUT_AREAS = new Bounds[] { COLD_AREA, HOT_AREA };

    private final HeatPropertiesRecipe recipe;

    public BlockHeatPropertiesEmiRecipe(RecipeHolder<HeatPropertiesRecipe> holder) {
        super(holder.id());

        this.recipe = holder.value();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PCategories.HEAT_PROPERTIES;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of();
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    protected EmiTexture getBackground() {
        return BACKGROUND_TEXTURE;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);
        
        draw(widgets);
        
        setInputIngredient(widgets);
    }
    
    private void setInputIngredient(WidgetHolder widgets) {
        int x = INPUT_AREA.x() + 1;
        int y = INPUT_AREA.y() - 2;
        if (recipe.getBlock() instanceof LiquidBlock l) {
            widgets.addSlot(EmiStack.of(l.fluid, FluidType.BUCKET_VOLUME), x, y)
                .drawBack(false)
                .recipeContext(this);
        } else {
            // items are rendered as blocks by renderBlock()
            widgets.add(new SlotWidget(EmiStack.of(recipe.getBlock()), x, y) {
                @Override
                public void drawStack(GuiGraphics draw, int mouseX, int mouseY, float delta) {
                    // no-op
                }
            }).drawBack(false).recipeContext(this);
        }
    }
    
    private void draw(WidgetHolder widgets) {
        Font fontRenderer = Minecraft.getInstance().font;
        int h = fontRenderer.lineHeight;

        Component desc = recipe.getDescriptionKey().isEmpty() ?
            Component.empty() :
            Component.literal(" (" + I18n.get(recipe.getDescriptionKey()) + ")");
        widgets.addText(recipe.getInputDisplayName().copy().append(desc), 0, 0, 0x4040a0, false);

        Component temp = Component.translatable("pneumaticcraft.waila.temperature").append(Component.literal((recipe.getTemperature() - 273) + "°C"));
        widgets.addText(temp, 0, h * 2, 0x404040, false);

        recipe.getThermalResistance().ifPresent(resistance -> {
            String res = NumberFormat.getNumberInstance(Locale.getDefault()).format(resistance);
            widgets.addText(Component.translatable("pneumaticcraft.gui.jei.thermalResistance").append(res), 0, h * 3, 0x404040, false);
        });

        boolean showCapacity = false;
        if (recipe.getTransformCold().isPresent()) {
            widgets.addTexture(COLD_AREA_TEXTURE, INPUT_AREA.x() - COLD_AREA_TEXTURE.width - 5, 42);
            showCapacity = true;
        }
        if (recipe.getTransformHot().isPresent()) {
            widgets.addTexture(HOT_AREA_TEXTURE, HOT_AREA.x() - HOT_AREA_TEXTURE.width - 5, 42);
            showCapacity = true;
        }

        renderBlock(recipe.getBlockState(), widgets, INPUT_AREA.x() + 9, INPUT_AREA.y() + 1);
        recipe.getTransformCold().ifPresent(state -> renderBlock(state, widgets, COLD_AREA.x() + 9, COLD_AREA.y() + 1));
        recipe.getTransformHot().ifPresent(state -> renderBlock(state, widgets, HOT_AREA.x() + 9, HOT_AREA.y() + 1));


        if (showCapacity) {
            recipe.getHeatCapacity().ifPresent(heatCapacity -> {
                widgets.addText(Component.translatable("pneumaticcraft.gui.jei.heatCapacity",
                        NumberFormat.getNumberInstance(Locale.getDefault()).format(heatCapacity)),
                    0, BACKGROUND_TEXTURE.height - h, 0x404040, false
                );
            });
        }
    }
    
    private static void renderBlock(@Nullable BlockState state, WidgetHolder widgets, int x, int y) {
        if (state != null) {
            if (state.getBlock() == Blocks.AIR) {
                widgets.addTexture(AIR_TEXTURE, x - 8, y - 2);
            } else {
                widgets.addDrawable(0, 0, 0, 0, (graphics, mouseX, mouseY, delta) -> {
                    float rot = ClientUtils.getClientLevel().getGameTime() % 360;
                    GuiUtils.renderBlockInGui(graphics, state, x, y, 100, rot, 15f);
                });
            }
        }
    }
}
