package io.github.hadron13.petrochem.data;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import io.github.hadron13.petrochem.data.recipe.PetrochemGasolineEngineRecipeGen;
import io.github.hadron13.petrochem.data.recipe.PetrochemMixingRecipeGen;
import io.github.hadron13.petrochem.data.recipe.PetrochemPumpjackRecipeGen;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PetrochemRecipeProvider extends RecipeProvider {

    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();
    static final int BUCKET = FluidType.BUCKET_VOLUME;

    public PetrochemRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {

    }


    public static void registerAllProcessing(DataGenerator gen, PackOutput output) {

        GENERATORS.add(new PetrochemMixingRecipeGen(output));
        GENERATORS.add(new PetrochemGasolineEngineRecipeGen(output));
        GENERATORS.add(new PetrochemPumpjackRecipeGen(output));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "Petrochem's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }

}
