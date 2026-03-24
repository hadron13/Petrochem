package io.github.hadron13.petrochem.compat.kubejs;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import io.github.hadron13.petrochem.compat.kubejs.schemas.ProcessingRecipeSchema;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;

import java.util.HashMap;
import java.util.Map;

public class KubeJSPetrochemPlugin extends KubeJSPlugin {

    private static final Map<PetrochemRecipeTypes, RecipeSchema> recipeSchemas = new HashMap<>();

    static {
        recipeSchemas.put(PetrochemRecipeTypes.PYROPROCESSING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(PetrochemRecipeTypes.ELECTROLYZING, ProcessingRecipeSchema.PROCESSING_WITH_ENERGY);
        recipeSchemas.put(PetrochemRecipeTypes.CENTRIFUGING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(PetrochemRecipeTypes.PUMPJACK, ProcessingRecipeSchema.PUMPJACK_RECIPE);
        recipeSchemas.put(PetrochemRecipeTypes.DISTILLING, ProcessingRecipeSchema.DISTILLING_RECIPE);
//        recipeSchemas.put(GearboxRecipeTypes.REACTING, ProcessingRecipeSchema.REACTING_RECIPE);
//        recipeSchemas.put(GearboxRecipeTypes.DIPPING, ProcessingRecipeSchema.DIPPING_RECIPE);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        for (var createRecipeType : PetrochemRecipeTypes.values()) {
            if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer<?>) {
                var schema = recipeSchemas.getOrDefault(createRecipeType, ProcessingRecipeSchema.PROCESSING_DEFAULT);
                event.register(createRecipeType.getId(), schema);
            }
        }
    }
}