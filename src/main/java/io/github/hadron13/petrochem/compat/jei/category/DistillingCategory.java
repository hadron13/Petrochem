package io.github.hadron13.petrochem.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillingRecipe;
import io.github.hadron13.petrochem.compat.jei.ModGuiTextures;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.fluids.FluidStack;

import static io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity.DistilMode.DISTIL_FLASH;

public class DistillingCategory extends CreateRecipeCategory<DistillingRecipe> {
    public DistillingCategory(Info<DistillingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DistillingRecipe recipe, IFocusGroup focuses) {

        if(recipe.mode == DISTIL_FLASH){
            addFluidSlot(builder, (177/2) - 54, 130+4 - 19, new FluidStack(PetrochemFluids.STEAM.getSource(), 500));
        }
        addFluidSlot(builder, (177/2) - 54, 130+4, recipe.getFluidIngredients().get(0));

        int i = 0;
        for (FluidStack fluidResult : recipe.getFluidResults()) {
            int xPosition = (177/2) + 38;
            int yPosition = 130 + 4 - (i * 19);
            addFluidSlot(builder, xPosition, yPosition, fluidResult);
            i++;
        }


    }

    public void draw(DistillingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {

        ModGuiTextures.JEI_SHORT_ARROW.render(graphics, (177/2) - 35, 130 + 8);

        if(recipe.mode == DISTIL_FLASH){
            AllGuiTextures.JEI_SHADOW.render(graphics, (177/2) - (52/2), 150 );
        }else{
            AllGuiTextures.JEI_LIGHT.render(graphics, (177/2) - (52/2), 155 );
            ModGuiTextures.JEI_DISTILLING_FIRE.render(graphics, (177/2) - 16, 145);
        }

        for(int i = 0; i < recipe.getFluidResults().size(); i++) {
            ModGuiTextures.JEI_SHORT_ARROW.render(graphics, (177/2) + 15, 130 + 8 - (19*i));

            ModGuiTextures column = i == 0? ModGuiTextures.JEI_DISTILLING_COLUMN_BOTTOM: ModGuiTextures.JEI_DISTILLING_COLUMN;
            column.render(graphics, (177/2) - (32/2), 130 - (19*i));
        }
        AllGuiTextures.JEI_SLOT.render(graphics, (177/2)-73 - 1, 130+4-1);
        recipe.mode.getIcon().render(graphics, (177/2)-73, 130+4);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, DistillingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        if(mouseX > (177/2)-74 && mouseX < (177/2)-56 && mouseY >= 133 && mouseY <= 151){
            tooltip.add(PetrochemLang.translate(recipe.mode.getRawTranslationKey()).component() );
        }

        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
    }
}
