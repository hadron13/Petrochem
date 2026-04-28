package io.github.hadron13.petrochem.data.recipe.base;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PumpjackRecipeGen extends ProcessingRecipeGen {


    public class PumpjackRecipeBuilder extends ProcessingRecipeBuilder<PumpjackRecipe>{
        public String biome_location;
        public PumpjackRecipeBuilder(ProcessingRecipeFactory<PumpjackRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public PumpjackRecipeBuilder inBiome(String biome_location){
            this.biome_location = biome_location;
            return this;
        }

        @Override
        public PumpjackRecipe build() {
            PumpjackRecipe recipe = super.build();
            recipe.setBiome(biome_location);
            return recipe;
        }
    }

    protected GeneratedRecipe createPumpjack(String name, UnaryOperator<PumpjackRecipeBuilder> transform) {
        return createPumpjackDeferredId(() -> asResource(name), transform);
    }

    protected GeneratedRecipe createPumpjackDeferredId(Supplier<ResourceLocation> name, UnaryOperator<PumpjackRecipeBuilder> transform) {

        ProcessingRecipeSerializer<PumpjackRecipe> serializer = getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new PumpjackRecipeBuilder((ProcessingRecipeBuilder.ProcessingRecipeFactory<PumpjackRecipe>) serializer.getFactory(), name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }


    public PumpjackRecipeGen(PackOutput generator, String defaultNamespace) {
        super(generator, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return PetrochemRecipeTypes.PUMPJACK;
    }
}
