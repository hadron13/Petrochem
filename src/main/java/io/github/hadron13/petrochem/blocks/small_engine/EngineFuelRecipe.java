package io.github.hadron13.petrochem.blocks.small_engine;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class EngineFuelRecipe extends ProcessingRecipe<RecipeWrapper> {


    public EngineFuelRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(typeInfo, params);
    }

    public static EngineFuelRecipe gasoline(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        return new EngineFuelRecipe(PetrochemRecipeTypes.GASOLINE_ENGINE_FUEL, params);
    }

    public static EngineFuelRecipe diesel(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        return new EngineFuelRecipe(PetrochemRecipeTypes.DIESEL_ENGINE_FUEL, params);
    }

    public static EngineFuelRecipe ship(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        return new EngineFuelRecipe(PetrochemRecipeTypes.SHIP_ENGINE_FUEL, params);
    }

    public boolean match(FluidStack fuel){
        return getFluidIngredients().get(0).test(fuel);
    }
    public float getConsumptionRate(){
        return (float)getFluidIngredients().get(0).getRequiredAmount() / (float)getProcessingDuration();
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    public boolean matches(RecipeWrapper container, Level level) {
        return false;
    }
}
