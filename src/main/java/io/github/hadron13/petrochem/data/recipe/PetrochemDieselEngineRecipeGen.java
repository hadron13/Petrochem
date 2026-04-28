package io.github.hadron13.petrochem.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.data.PackOutput;

public class PetrochemDieselEngineRecipeGen extends ProcessingRecipeGen {


    GeneratedRecipe standard_diesel = create("diesel", b -> b
            .require(PetrochemFluids.REFINED_DIESEL.get(), 1)
            .duration(50)
    );

    GeneratedRecipe fuel_oil = create("fuel_oil", b -> b
            .require(PetrochemFluids.FUEL_OIL.get(), 1)
            .duration(30)
    );

    GeneratedRecipe petrol = create("raw_petroleum", b -> b
            .require(PetrochemFluids.PETROLEUM.get(), 1)
            .duration(10)
    );


    public PetrochemDieselEngineRecipeGen(PackOutput generator) {
        super(generator, Petrochem.MODID);
    }
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return PetrochemRecipeTypes.DIESEL_ENGINE_FUEL;
    }
}
