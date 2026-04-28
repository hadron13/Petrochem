package io.github.hadron13.petrochem.data.recipe;

import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.data.recipe.base.ElectrolyzingRecipeGen;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import net.minecraft.data.PackOutput;

public class PetrochemElectrolyzingRecipeGen extends ElectrolyzingRecipeGen {

    GeneratedRecipe BASIC_DESALTED_OIL = createElectrolyzing("basic_desalting", b -> (ElectrolyzingRecipeBuilder) b
            .energy(100)
            .require(PetrochemFluids.PETROLEUM.get(), 500)
            .output(PetrochemFluids.DESALTED_OIL.get(), 500)
            .whenModMissing(Petrochem.REALISTIC_MODID)
    );


    public PetrochemElectrolyzingRecipeGen(PackOutput generator) {
        super(generator, Petrochem.MODID);
    }
}
