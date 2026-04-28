package io.github.hadron13.petrochem.data.recipe;

import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.data.recipe.base.PumpjackRecipeGen;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import net.minecraft.data.PackOutput;

public class PetrochemPumpjackRecipeGen extends PumpjackRecipeGen {

    GeneratedRecipe DESERT = createPumpjack("desert", b -> (PumpjackRecipeBuilder) b
            .inBiome("minecraft:desert")
            .output(PetrochemFluids.PETROLEUM.get(), 50)
    );

    GeneratedRecipe DEEP_OCEAN = createPumpjack("deep_ocean", b -> (PumpjackRecipeBuilder) b
            .inBiome("minecraft:deep_ocean")
            .output(PetrochemFluids.PETROLEUM.get(), 20)
    );

    GeneratedRecipe COLD_DEEP_OCEAN = createPumpjack("cold_deep_ocean", b -> (PumpjackRecipeBuilder) b
            .inBiome("minecraft:deep_cold_ocean")
            .output(PetrochemFluids.PETROLEUM.get(), 50)
    );

    GeneratedRecipe JUNGLE = createPumpjack("jungle", b -> (PumpjackRecipeBuilder) b
            .inBiome("minecraft:jungle")
            .output(PetrochemFluids.DESALTED_OIL.get(), 50)
    );

    GeneratedRecipe SWAMP = createPumpjack("swamp", b -> (PumpjackRecipeBuilder) b
            .inBiome("minecraft:swamp")
            .output(PetrochemFluids.DESALTED_OIL.get(), 60)
    );




    public PetrochemPumpjackRecipeGen(PackOutput generator) {
        super(generator, Petrochem.MODID);
    }
}
