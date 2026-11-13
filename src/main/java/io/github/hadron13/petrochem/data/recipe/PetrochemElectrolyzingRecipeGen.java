package io.github.hadron13.petrochem.data.recipe;

import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.data.recipe.base.ElectrolyzingRecipeGen;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import io.github.hadron13.petrochem.register.PetrochemItems;
import io.github.hadron13.petrochem.register.PetrochemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class PetrochemElectrolyzingRecipeGen extends ElectrolyzingRecipeGen {

    GeneratedRecipe BASIC_DESALTED_OIL = create("basic_desalting", b -> b
            .energy(100)
            .require(PetrochemTags.FluidTags.CRUDE_OIL.tag, 500)
            .output(PetrochemFluids.DESALTED_OIL.get(), 500)
            .output(0.5f, PetrochemItems.SALT_DUST)
            .whenModMissing(Petrochem.REALISTIC_MODID)
    ),
    WATER_ELECTROLYSIS = create("water_electrolysis", b -> b
            .energy(150)
            .require(Fluids.WATER, 300)
            .output(PetrochemFluids.HYDROGEN.get(), 200)
            .output(PetrochemFluids.OXYGEN.get(), 100)
    ),
    CHLOR_ALKALI = create("chlor_alkali", b -> b
            .energy(150)
            .require(Fluids.WATER, 600)
            .require(PetrochemItems.SALT_DUST.get())
            .output(PetrochemFluids.CHLORINE.get(), 250)
            .output(PetrochemFluids.HYDROGEN.get(), 400)
            .output(PetrochemItems.CAUSTIC_SODA, 2)
    )
;


    public PetrochemElectrolyzingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, Petrochem.MODID);
    }
}
