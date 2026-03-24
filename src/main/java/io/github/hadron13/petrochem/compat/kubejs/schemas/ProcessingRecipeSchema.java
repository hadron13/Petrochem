package io.github.hadron13.petrochem.compat.kubejs.schemas;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.BlockTagIngredient;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.item.ingredient.TagContext;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.MapJS;
import io.github.hadron13.petrochem.compat.kubejs.helpers.CreateInputFluid;
import io.github.hadron13.petrochem.compat.kubejs.helpers.FluidIngredientHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public interface ProcessingRecipeSchema {
    RecipeKey<Either<OutputFluid, OutputItem>[]> RESULTS = FluidComponents.OUTPUT_OR_ITEM_ARRAY.key("results");
    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS = FluidComponents.INPUT_OR_ITEM_ARRAY.key("ingredients");

    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS_UNWRAPPED = new RecipeComponentWithParent<Either<InputFluid, InputItem>[]>() {
        @Override
        public RecipeComponent<Either<InputFluid, InputItem>[]> parentComponent() {
            return INGREDIENTS.component;
        }

        @Override
        public JsonElement write(RecipeJS recipe, Either<InputFluid, InputItem>[] value) {
            // during writing, unwrap all stacked input items
            var json = new JsonArray();
            for (var either : value) {
                either.ifLeft(fluid -> json.add(FluidComponents.INPUT.write(recipe, fluid)))
                        .ifRight(item -> {
                            for (var unwrapped : item.unwrap()) {
                                json.add(ItemComponents.INPUT.write(recipe, unwrapped));
                            }
                        });
            }
            return json;
        }
    }.key("ingredients");

    RecipeKey<InputFluid> ATMOSPHERE = new RecipeComponent<InputFluid>() {
        @Override
        public Class<?> componentClass() {
            return InputFluid.class;
        }

        @Override
        public JsonElement write(RecipeJS recipeJS, InputFluid inputFluid) {
            var json = new JsonArray();
            json.add(FluidComponents.INPUT.write(recipeJS, inputFluid));
            return json;
        }

        @Override
        public InputFluid read(RecipeJS recipeJS, Object o) {
            return recipeJS.readInputFluid(o);
        }
    }.key("atmosphere").optional(CreateInputFluid.EMPTY);

    RecipeKey<Long> PROCESSING_TIME = TimeComponent.TICKS.key("processingTime").optional(100L);
    // specifically for crushing, cutting and milling
    RecipeKey<Long> PROCESSING_TIME_REQUIRED = TimeComponent.TICKS.key("processingTime").optional(100L).alwaysWrite();

    RecipeKey<String> BIOME = StringComponent.ID.key("biome");
    RecipeKey<String> MODE = StringComponent.ID.key("mode").optional("distil_atmospheric").alwaysWrite();
    //RecipeKey<ResourceKey<Biome>> BIOME = new RecipeComponent<ResourceKey<Biome>>() {
    //    @Override
    //    public Class<?> componentClass() {
    //        return ResourceKey.class;
    //    }
//
    //    @Override
    //    public JsonElement write(RecipeJS recipeJS, ResourceKey<Biome> biome) {
    //        JsonObject json = recipeJS.json;
    //        json.addProperty("biome", biome.registry().getPath());
    //        return json;
    //    }
//
    //    @Override
    //    public ResourceKey<Biome> read(RecipeJS recipeJS, Object o) {
    //        var json = recipeJS.json;
    //        String biome_key = GsonHelper.getAsString(json, "biome");
    //        return ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(biome_key));
    //    }
    //}.key("biome");

    RecipeKey<Integer> ENERGY_REQUIREMENT = NumberComponent.INT.key("energy").optional(1);

    RecipeKey<Integer> RPM_MAX = NumberComponent.INT.key("rpm_max").optional(1);
    RecipeKey<Integer> RPM_MIN = NumberComponent.INT.key("rpm_min").optional(1);

    RecipeKey<Integer> DIPS = NumberComponent.INT.key("dips").optional(1);

    RecipeKey<Integer> COLOR = NumberComponent.INT.key("color");

    RecipeKey<Float> POWER = NumberComponent.FLOAT.key("power").optional(1.0f);



    RecipeKey<String> HEAT_REQUIREMENT = new StringComponent("not a valid heat condition!", s -> {
        for (var h : HeatCondition.values()) {
            if (h.name().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }).key("heatRequirement").defaultOptional().allowEmpty();

    // specifically used in item application
    RecipeKey<Boolean> KEEP_HELD_ITEM = BooleanComponent.BOOLEAN.key("keepHeldItem").optional(false);

    class ProcessingRecipeJS extends RecipeJS {
        @Override
        public InputFluid readInputFluid(Object from) {
            if (from instanceof CreateInputFluid fluid) {
                return fluid;
            } else if (from instanceof FluidIngredient fluid) {
                return new CreateInputFluid(fluid);
            } else if (from instanceof FluidStackJS fluid) {
                return new CreateInputFluid(FluidIngredientHelper.toFluidIngredient(fluid));
            } else if (from instanceof FluidStack fluid) {
                return new CreateInputFluid(FluidIngredient.fromFluidStack(fluid));
            } else {
                var json = MapJS.json(from);
                if (json != null) {
                    return new CreateInputFluid(FluidIngredient.deserialize(json));
                }
                return CreateInputFluid.EMPTY;
            }
        }

        @Override
        public JsonElement writeInputFluid(InputFluid value) {
            if (value instanceof CreateInputFluid fluid) {
                return fluid.ingredient().serialize();
            } else if (value instanceof FluidIngredient fluid) {
                return fluid.serialize();
            } else if (value instanceof FluidStackJS fluid) {
                return FluidIngredientHelper.toFluidIngredient(fluid).serialize();
            } else {
                return FluidIngredient.EMPTY.serialize();
            }
        }

        @Override
        public boolean inputItemHasPriority(Object from) {
            if (from instanceof InputItem || from instanceof Ingredient || from instanceof ItemStack) {
                return true;
            }

            var input = readInputItem(from);
            if (input.ingredient instanceof BlockTagIngredient blockTag) {
                return !TagContext.INSTANCE.getValue().isEmpty(blockTag.getTag());
            }

            return !input.isEmpty();
        }

        @Override
        public boolean inputFluidHasPriority(Object from) {
            return from instanceof InputFluid || FluidIngredient.isFluidIngredient(MapJS.json(from));
        }

        @Override
        public OutputItem readOutputItem(Object from) {
            if (from instanceof ProcessingOutput output) {
                return OutputItem.of(output.getStack(), output.getChance());
            } else {
                var outputItem = super.readOutputItem(from);
                if (from instanceof JsonObject j && j.has("chance")) {
                    return outputItem.withChance(j.get("chance").getAsFloat());
                }
                return outputItem;
            }
        }

        public RecipeJS heated() {
            return setValue(HEAT_REQUIREMENT, HeatCondition.HEATED.serialize());
        }

        public RecipeJS superheated() {
            return setValue(HEAT_REQUIREMENT, HeatCondition.SUPERHEATED.serialize());
        }
    }

    class ItemApplicationRecipeJS extends ProcessingRecipeJS {
        public RecipeJS keepHeldItem() {
            return setValue(KEEP_HELD_ITEM, true);
        }
    }

    RecipeSchema PROCESSING_DEFAULT = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME, HEAT_REQUIREMENT);

    RecipeSchema PROCESSING_WITH_TIME = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME_REQUIRED, HEAT_REQUIREMENT);

    RecipeSchema PROCESSING_WITH_ENERGY = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, ENERGY_REQUIREMENT, PROCESSING_TIME, HEAT_REQUIREMENT);

    RecipeSchema PROCESSING_UNWRAPPED = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS_UNWRAPPED, PROCESSING_TIME, HEAT_REQUIREMENT);

    RecipeSchema LASER_RECIPE = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, COLOR, POWER, PROCESSING_TIME);

    RecipeSchema ITEM_APPLICATION = new RecipeSchema(ItemApplicationRecipeJS.class, ItemApplicationRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME, HEAT_REQUIREMENT, KEEP_HELD_ITEM);

    RecipeSchema REACTING_RECIPE = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME_REQUIRED, HEAT_REQUIREMENT, ATMOSPHERE, RPM_MAX, RPM_MIN);

    RecipeSchema PUMPJACK_RECIPE = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, BIOME, PROCESSING_TIME_REQUIRED);
    RecipeSchema DISTILLING_RECIPE = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME_REQUIRED, MODE);

    RecipeSchema DIPPING_RECIPE = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, DIPS, PROCESSING_TIME, HEAT_REQUIREMENT);
}
