package io.github.hadron13.gearbox.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedPumpjackWell;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraftforge.fluids.FluidStack;

public class PumpjackCategory extends CreateRecipeCategory<PumpjackRecipe>{
    public final AnimatedPumpjackWell well = new AnimatedPumpjackWell();

    public static final int xcenter = 177/2;
    public static final int ycenter = 65/2;

    public PumpjackCategory(CreateRecipeCategory.Info<PumpjackRecipe> info){super(info);}

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PumpjackRecipe recipe, IFocusGroup focuses) {


        FluidStack fluidResult = recipe.getFluidResults().get(0);
        builder.addSlot(RecipeIngredientRole.OUTPUT, xcenter + 40, ycenter+5)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, fluidResult)
                .setFluidRenderer(fluidResult.getAmount(), false, 16, 16);
    }

    @Override
    public void draw(PumpjackRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {

        Component biome_name = Component.translatable("biome." + recipe.biome.location().toLanguageKey());

        Font font = Minecraft.getInstance().font;
        int width = font.width(biome_name);

        graphics.drawString(Minecraft.getInstance().font, biome_name, xcenter - width/2, ycenter-10, 0xFFFFFF);

        AllGuiTextures.JEI_ARROW.render(graphics, xcenter-20, ycenter+10);
        well.draw(graphics, xcenter - 60, ycenter+22);

    }

}
