package io.github.hadron13.petrochem.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.petrochem.compat.jei.ModGuiTextures;
import io.github.hadron13.petrochem.compat.jei.category.animations.AnimatedCentrifuge;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.fluids.FluidStack;

public class CentrifugingCategory extends CreateRecipeCategory<CentrifugingRecipe> {

    AnimatedCentrifuge centrifuge = new AnimatedCentrifuge();

    public CentrifugingCategory(Info<CentrifugingRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, CentrifugingRecipe recipe, IFocusGroup focuses) {

        FluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);
        addFluidSlot(builder, 52, 10, fluidIngredient);

        int i = 0;

        int size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
        for (FluidStack fluidResult : recipe.getFluidResults()) {
            int xPosition = 110 + i%3 * 19 + (size > 3? 10:0);
            int yPosition = 68 - ((i>=3)?19:0);
            addFluidSlot(builder, xPosition, yPosition, fluidResult);
            i++;
        }
    }
    public void draw(CentrifugingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 76, 15);
        ModGuiTextures.JEI_BACK_ARROW.render(graphics, 88, 62);
        centrifuge.draw(graphics, 75, 55);
    }




}
