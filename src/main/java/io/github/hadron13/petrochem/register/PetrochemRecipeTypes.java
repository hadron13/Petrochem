package io.github.hadron13.petrochem.register;

import com.google.common.collect.ImmutableSet;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.petrochem.blocks.chemical_reactor.ReactingRecipe;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillingRecipe;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.petrochem.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.petrochem.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.petrochem.blocks.pumpjack.PumpjackWellBlockEntity;
import io.github.hadron13.petrochem.blocks.small_engine.EngineFuelRecipe;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum PetrochemRecipeTypes implements IRecipeTypeInfo {
    PYROPROCESSING(PyroprocessingRecipe::new),
    ELECTROLYZING(ElectrolyzingRecipe::new),
    CENTRIFUGING(CentrifugingRecipe::new),
    PUMPJACK(PumpjackRecipe::new),
    REACTING(ReactingRecipe::new),
    DISTILLING(DistillingRecipe::new),
    GASOLINE_ENGINE_FUEL(EngineFuelRecipe::gasoline),
    DIESEL_ENGINE_FUEL(EngineFuelRecipe::diesel),
    SHIP_ENGINE_FUEL(EngineFuelRecipe::ship);

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    public static final Predicate<? super Recipe<?>> CAN_BE_AUTOMATED = r -> !r.getId()
            .getPath()
            .endsWith("_manual_only");


    PetrochemRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = Petrochem.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> simpleType(id));
        type = typeObject;
    }
    PetrochemRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }




    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }



    public Optional<CentrifugingRecipe> find(CentrifugeBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();
        List<CentrifugingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(PetrochemRecipeTypes.CENTRIFUGING.getType());

        Stream<CentrifugingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> CentrifugingRecipe.match(blockEntity, recipe) );

        return matchingRecipes.findAny();
    }
    public Optional<PumpjackRecipe> find(PumpjackWellBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();
        List<PumpjackRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(PetrochemRecipeTypes.PUMPJACK.getType());

        Stream<PumpjackRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> PumpjackRecipe.match(blockEntity, recipe) );

        return matchingRecipes.findAny();
    }

    public Optional<DistillingRecipe> find(DistillationControllerBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();
        List<DistillingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(PetrochemRecipeTypes.DISTILLING.getType());

        Stream<DistillingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> DistillingRecipe.match(blockEntity, recipe) );

        return matchingRecipes.findAny();
    }



    public static final Set<ResourceLocation> RECIPE_DENY_SET =
            ImmutableSet.of(new ResourceLocation("occultism", "spirit_trade"), new ResourceLocation("occultism", "ritual"));

    public static boolean shouldIgnoreInAutomation(Recipe<?> recipe) {
        RecipeSerializer<?> serializer = recipe.getSerializer();
        if (serializer != null && AllTags.AllRecipeSerializerTags.AUTOMATION_IGNORE.matches(serializer))
            return true;
        return !CAN_BE_AUTOMATED.test(recipe);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Petrochem.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Petrochem.MODID);
    }

}