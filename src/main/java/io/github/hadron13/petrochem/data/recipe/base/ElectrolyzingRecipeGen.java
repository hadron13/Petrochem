package io.github.hadron13.petrochem.data.recipe.base;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ElectrolyzingRecipeGen extends ProcessingRecipeGen {


    public class ElectrolyzingRecipeBuilder extends ProcessingRecipeBuilder<ElectrolyzingRecipe> {
        public int energy;
        public ElectrolyzingRecipeBuilder(ProcessingRecipeFactory<ElectrolyzingRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public ElectrolyzingRecipeBuilder energy(int energy){
            this.energy = energy;
            return this;
        }

        @Override
        public ElectrolyzingRecipe build() {
            return super.build().setEnergy(energy);
        }
    }

    protected GeneratedRecipe createElectrolyzing(String name, UnaryOperator<ElectrolyzingRecipeBuilder> transform) {
        return createElectrolyzingDeferredId(() -> asResource(name), transform);
    }

    protected GeneratedRecipe createElectrolyzingDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ElectrolyzingRecipeBuilder> transform) {

        ProcessingRecipeSerializer<ElectrolyzingRecipe> serializer = getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ElectrolyzingRecipeBuilder(serializer.getFactory(), name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }



    public ElectrolyzingRecipeGen(PackOutput generator, String defaultNamespace) {
        super(generator, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return PetrochemRecipeTypes.ELECTROLYZING;
    }
}
