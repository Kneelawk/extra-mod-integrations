package com.kneelawk.extramodintegrations.hephaestus.melting;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;

import java.awt.*;
import java.util.List;

public abstract class AbstractMeltingEmiRecipe implements EmiRecipe {
  protected static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/melting.png");
  protected static final EmiTexture plus = new EmiTexture(BACKGROUND_LOC, 132, 34, 6, 6);
  protected static final EmiTexture arrow = new EmiTexture(BACKGROUND_LOC, 150, 41, 24, 17);
  protected static final EmiTexture tankOverlay = new EmiTexture(BACKGROUND_LOC, 132, 0, 32, 32);

  protected final ResourceLocation id;
  protected final EmiIngredient input;
  protected final int time;
  protected final IMeltingContainer.OreRateType oreType;
  protected final int temperature;

  public AbstractMeltingEmiRecipe(MeltingRecipe recipe) {
    id = recipe.getId();
    input = EmiIngredient.of(recipe.getInput());
    time = recipe.getTime();
    oreType = recipe.getOreType();
    temperature = recipe.getTemperature();
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(input);
  }

  @Override
  public int getDisplayWidth() {
    return 132;
  }

  @Override
  public int getDisplayHeight() {
    return 40;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(BACKGROUND_LOC, 0, 0, 132, 40, 0, 0);

    // draw the arrow
    widgets.addAnimatedTexture(arrow, 56, 18, time * 250, true, false, false);
    if (oreType != null) {
      widgets.addTexture(plus, 87, 31)
        .tooltip((mouseX, mouseY) -> List.of(ClientTooltipComponent.create(new TranslatableComponent("jei.tconstruct.melting.ore").getVisualOrderText())));
    }

    Component temp = new TranslatableComponent("jei.tconstruct.temperature", temperature);
    widgets.addText(temp, 56, 3, Color.GRAY.getRGB(), false).horizontalAlign(TextWidget.Alignment.CENTER);

    // input
    widgets.addSlot(input, 23, 17).drawBack(false);
  }
}
