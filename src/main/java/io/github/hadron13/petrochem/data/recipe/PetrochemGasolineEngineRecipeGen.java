package io.github.hadron13.petrochem.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.data.PackOutput;

public class PetrochemGasolineEngineRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe STANDARD_GASOLINE = create("gasoline", b -> b
            .require(PetrochemFluids.GASOLINE.get(), 1)
            .duration(35)
    );

    GeneratedRecipe STANDARD_KEROSENE = create("kerosene", b -> b
            .require(PetrochemFluids.KEROSENE.get(), 1)
            .duration(25)
    );



    public PetrochemGasolineEngineRecipeGen(PackOutput generator) {
        super(generator, Petrochem.MODID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return PetrochemRecipeTypes.GASOLINE_ENGINE_FUEL;
    }
}
