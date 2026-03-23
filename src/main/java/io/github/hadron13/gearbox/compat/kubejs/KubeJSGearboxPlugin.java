package io.github.hadron13.gearbox.compat.kubejs;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import io.github.hadron13.gearbox.compat.kubejs.schemas.ProcessingRecipeSchema;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;

import java.util.HashMap;
import java.util.Map;

public class KubeJSGearboxPlugin extends KubeJSPlugin {

    private static final Map<GearboxRecipeTypes, RecipeSchema> recipeSchemas = new HashMap<>();

    static {
        recipeSchemas.put(GearboxRecipeTypes.PYROPROCESSING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(GearboxRecipeTypes.ELECTROLYZING, ProcessingRecipeSchema.PROCESSING_WITH_ENERGY);
        recipeSchemas.put(GearboxRecipeTypes.CENTRIFUGING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(GearboxRecipeTypes.PUMPJACK, ProcessingRecipeSchema.PUMPJACK_RECIPE);
        recipeSchemas.put(GearboxRecipeTypes.DISTILLING, ProcessingRecipeSchema.DISTILLING_RECIPE);
//        recipeSchemas.put(GearboxRecipeTypes.REACTING, ProcessingRecipeSchema.REACTING_RECIPE);
//        recipeSchemas.put(GearboxRecipeTypes.DIPPING, ProcessingRecipeSchema.DIPPING_RECIPE);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        for (var createRecipeType : GearboxRecipeTypes.values()) {
            if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer<?>) {
                var schema = recipeSchemas.getOrDefault(createRecipeType, ProcessingRecipeSchema.PROCESSING_DEFAULT);
                event.register(createRecipeType.getId(), schema);
            }
        }
    }
}