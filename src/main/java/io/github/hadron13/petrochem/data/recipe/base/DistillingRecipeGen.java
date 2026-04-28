package io.github.hadron13.petrochem.data.recipe.base;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillingRecipe;
import io.github.hadron13.petrochem.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DistillingRecipeGen extends ProcessingRecipeGen {

    public class DistillingRecipeBuilder extends ProcessingRecipeBuilder<DistillingRecipe> {
        public DistillationControllerBlockEntity.DistilMode mode;
        public DistillingRecipeBuilder(ProcessingRecipeFactory<DistillingRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public DistillingRecipeBuilder mode(DistillationControllerBlockEntity.DistilMode mode){
            this.mode = mode;
            return this;
        }

        @Override
        public DistillingRecipe build() {
            return super.build().setMode(mode);
        }
    }

    protected GeneratedRecipe createDistilling(String name, UnaryOperator<DistillingRecipeBuilder> transform) {
        return createDistillingDeferredId(() -> asResource(name), transform);
    }

    protected GeneratedRecipe createDistillingDeferredId(Supplier<ResourceLocation> name, UnaryOperator<DistillingRecipeBuilder> transform) {

        ProcessingRecipeSerializer<DistillingRecipe> serializer = getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new DistillingRecipeBuilder(serializer.getFactory(), name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    public DistillingRecipeGen(PackOutput generator, String defaultNamespace) {
        super(generator, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return PetrochemRecipeTypes.DISTILLING;
    }
}
