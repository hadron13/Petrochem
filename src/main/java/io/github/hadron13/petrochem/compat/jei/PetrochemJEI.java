package io.github.hadron13.petrochem.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillingRecipe;
import io.github.hadron13.petrochem.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.petrochem.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.petrochem.compat.jei.category.*;
import io.github.hadron13.petrochem.register.PetrochemBlocks;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class PetrochemJEI implements IModPlugin {

    private static final ResourceLocation ID = Petrochem.asResource("jei_plugin");

    public IIngredientManager ingredientManager;
    private final List<CreateRecipeCategory<?>> modCategories = new ArrayList<>();


    private void loadCategories() {

        this.modCategories.clear();
        CreateRecipeCategory<?>
                pyroprocessing = builder(PyroprocessingRecipe.class)
                .addTypedRecipes(PetrochemRecipeTypes.PYROPROCESSING)
                .catalyst(PetrochemBlocks.KILN::get)
                .itemIcon(PetrochemBlocks.KILN.get())
                .emptyBackground(177, 75)
                .build("pyroprocessing", PyroprocessingCategory::new);




        CreateRecipeCategory<?>
                electrolyzing = builder(BasinRecipe.class)
                .addTypedRecipes(PetrochemRecipeTypes.ELECTROLYZING)
                .catalyst(PetrochemBlocks.ELECTROLYZER::get)
                .catalyst(AllBlocks.BASIN::get)
                .doubleItemIcon(PetrochemBlocks.ELECTROLYZER.get(), AllBlocks.BASIN.get())
                .emptyBackground(177, 103)
                .build("electrolyzing", ElectrolyzingCategory::new);


        CreateRecipeCategory<?>
                centrifuging = builder(CentrifugingRecipe.class)
                .addTypedRecipes(PetrochemRecipeTypes.CENTRIFUGING)
                .catalyst(PetrochemBlocks.CENTRIFUGE::get)
                .itemIcon(PetrochemBlocks.CENTRIFUGE.get())
                .emptyBackground(177, 93)
                .build("centrifuging", CentrifugingCategory::new);

        CreateRecipeCategory<?>
                pumpjack = builder(PumpjackRecipe.class)
                .addTypedRecipes(PetrochemRecipeTypes.PUMPJACK)
                .catalyst(PetrochemBlocks.PUMPJACK_WELL::get)
                .catalyst(PetrochemBlocks.PUMPJACK_ARM::get)
                .catalyst(PetrochemBlocks.PUMPJACK_CRANK::get)
                .itemIcon(PetrochemBlocks.PUMPJACK_WELL.get())
                .emptyBackground(177, 65)
                .build("pumpjack", PumpjackCategory::new);


        CreateRecipeCategory<?>
                distilling = builder(DistillingRecipe.class)
                .addTypedRecipes(PetrochemRecipeTypes.DISTILLING)
                .catalyst(PetrochemBlocks.DISTILLATION_CONTROLLER::get)
                .catalyst(PetrochemBlocks.DISTILLATION_OUTPUT::get)
                .catalyst(PetrochemBlocks.STEEL_FLUID_TANK::get)
                .itemIcon(PetrochemBlocks.DISTILLATION_CONTROLLER.get())
                .emptyBackground(177, 165)
                .build("distilling", DistillingCategory::new);

//        CreateRecipeCategory<?>
//                reacting= builder(BasinRecipe.class)
//                .addTypedRecipes(GearboxRecipeTypes.REACTING)
//                .catalyst(GearboxBlocks.REACTOR::get)
//                .catalyst(AllBlocks.BASIN::get)
//                .doubleItemIcon(GearboxBlocks.REACTOR.get(), AllBlocks.BASIN.get())
//                .emptyBackground(177, 103)
//                .build("reacting", ReactingCategory::new);
    }
    private <T extends Recipe<?>> CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new CategoryBuilder<>(recipeClass);
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(modCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();

        modCategories.forEach(c -> c.registerRecipes(registration));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        modCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
    }



    private class CategoryBuilder<T extends Recipe<?>> {
        private final Class<? extends T> recipeClass;
        private Predicate<CRecipes> predicate = cRecipes -> true;

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public CategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
            this.predicate = predicate;
            return this;
        }

        public CategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
            predicate = c -> configValue.apply(c).get();
            return this;
        }

        public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
            return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
        }

        public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add((T) recipe);
                }
            }));
        }

        public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(converter.apply(recipe));
                }
            }));
        }

        public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
        }

        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> recipes.add(converter.apply(recipe)), recipeType.get()));
        }

        public CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(recipe);
                }
            }, recipeType.get()));
        }

        public CategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType,
                                                           Supplier<RecipeType<? extends T>> excluded) {
            return addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes(excluded.get());
                CreateJEI.<T>consumeTypedRecipes(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                            return;
                        }
                    }
                    recipes.add(recipe);
                }, recipeType.get());
            });
        }

        public CategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes(recipeType.get());
                recipes.removeIf(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                            return true;
                        }
                    }
                    return false;
                });
            });
        }

        public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            catalysts.add(supplier);
            return this;
        }

        public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get()
                    .asItem()));
        }

        public CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public CategoryBuilder<T> itemIcon(ItemLike item) {
            icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
            return this;
        }

        public CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public CategoryBuilder<T> emptyBackground(int width, int height) {
            background(new EmptyBackground(width, height));
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (predicate.test(AllConfigs.server().recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList<>();
                    for (Consumer<List<T>> consumer : recipeListConsumers)
                        consumer.accept(recipes);
                    return recipes;
                };
            } else {
                recipesSupplier = () -> Collections.emptyList();
            }

            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
                    new mezz.jei.api.recipe.RecipeType<>(Petrochem.asResource(name), recipeClass),
                    PetrochemLang.translateDirect("recipe." + name), background, icon, recipesSupplier, catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            modCategories.add(category);
            return category;
        }
    }

    public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
        Minecraft.getInstance()
                .getConnection()
                .getRecipeManager()
                .getRecipes()
                .forEach(consumer);
    }






}
