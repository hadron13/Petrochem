package io.github.hadron13.petrochem.blocks.pumpjack;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.fluids.FluidStack;

public class PumpjackRecipe extends ProcessingRecipe<RecipeInput, PumpjackRecipeParams> {

    public ResourceKey<Biome> biome;
    public TagKey<Biome> biome_tag;
    public float density;

    public PumpjackRecipe(PumpjackRecipeParams params) {
        super(PetrochemRecipeTypes.PUMPJACK, params);

        if(params.biome.contains("#")){
            biome_tag = TagKey.create(Registries.BIOME, ResourceLocation.parse(params.biome.substring(1)));
        }else{
            biome = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(params.biome));
        }
    }

    public static boolean match(PumpjackWellBlockEntity be, PumpjackRecipe recipe){
        if(recipe.biome_tag != null){
            return be.getLevel().getBiome(be.getBlockPos()).is(recipe.biome_tag);
        }
        return be.getLevel().getBiome(be.getBlockPos()).is(recipe.biome);
    }

    public FluidStack getFluidResult(){
        return getFluidResults().get(0);
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }


    @FunctionalInterface
    public interface Factory<R extends PumpjackRecipe> extends ProcessingRecipe.Factory<PumpjackRecipeParams, R> {
        R create(PumpjackRecipeParams params);
    }

    public static class Builder<R extends PumpjackRecipe> extends ProcessingRecipeBuilder<PumpjackRecipeParams, R, PumpjackRecipe.Builder<R>> {
        public Builder(PumpjackRecipe.Factory<R> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected PumpjackRecipeParams createParams() {
            return new PumpjackRecipeParams();
        }

        @Override
        public PumpjackRecipe.Builder<R> self() {
            return this;
        }

        public PumpjackRecipe.Builder<R> biome(String biome){
            params.biome = biome;
            return this;
        }
    }

    public static class Serializer<R extends PumpjackRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<PumpjackRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, PumpjackRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, PumpjackRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }
}
