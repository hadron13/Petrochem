package io.github.hadron13.gearbox.compat.jei.category;

//import io.github.hadron13.gearbox.content.contraptions.components.sifter.PyroprocessingRecipe;
//import io.github.hadron13.gearbox.foundation.gui.ModGUITextures;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedKiln;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class PyroprocessingCategory extends CreateRecipeCategory<PyroprocessingRecipe> {
    private AnimatedKiln kiln = new AnimatedKiln();
    public PyroprocessingCategory(CreateRecipeCategory.Info<PyroprocessingRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, PyroprocessingRecipe recipe, IFocusGroup focuses) {
        builder
                .addSlot(RecipeIngredientRole.INPUT, 15, 9)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int i = 0;
        for (ProcessingOutput output : results) {
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = (i / 2) * -19;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset, 27 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack()).addRichTooltipCallback(addStochasticTooltip(output));

            i++;
        }

    }
    public void draw(PyroprocessingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 85, 32);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 43, 4);
        kiln.draw(graphics, 48, 27);
    }
}
