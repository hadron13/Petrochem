package io.github.hadron13.gearbox.blocks.centrifuge;

import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.createmod.catnip.data.Iterate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CentrifugingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public CentrifugingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(GearboxRecipeTypes.CENTRIFUGING, params);
    }

    public static boolean match(CentrifugeBlockEntity centrifuge, CentrifugingRecipe recipe) {
        return apply(centrifuge, recipe, true);
    }

    public static boolean apply(CentrifugeBlockEntity centrifuge, CentrifugingRecipe recipe, boolean test){

        IFluidHandler availableFluids = centrifuge.inputTank.getCapability().orElse(null);
        IItemHandler availableItems = centrifuge.inputInv;

        if (availableItems == null || availableFluids == null)
            return false;

        List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
        List<FluidIngredient> fluidIngredients = recipe.getFluidIngredients();

        for (boolean simulate : Iterate.trueAndFalse) {

            if (!simulate && test)
                return true;

            FluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);
            FluidStack availableFluid = availableFluids.getFluidInTank(0);

            if( !fluidIngredient.test(availableFluid) || availableFluid.getAmount() < fluidIngredient.getRequiredAmount())
                return false;

            Ingredient itemIngredient = recipe.getIngredients().get(0);
            ItemStack availableItem = availableItems.getStackInSlot(0);

            if( !itemIngredient.test(availableItem))
                return false;




//            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];
//            int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
//
//            Ingredients:
//            for (Ingredient ingredient : ingredients) {
//                for (int slot = 0; slot < availableItems.getSlots(); slot++) {
//                    if (simulate && availableItems.getStackInSlot(slot)
//                            .getCount() <= extractedItemsFromSlot[slot])
//                        continue;
//                    ItemStack extracted = availableItems.extractItem(slot, 1, true);
//                    if (!ingredient.test(extracted))
//                        continue;
//                    if (!simulate)
//                        availableItems.extractItem(slot, 1, false);
//                    extractedItemsFromSlot[slot]++;
//                    continue Ingredients;
//                }
//
//                // something wasn't found
//                return false;
//            }
//
//            boolean fluidsAffected = false;
//            FluidIngredients:
//            for (int i = 0; i < fluidIngredients.size(); i++) {
//                FluidIngredient fluidIngredient = fluidIngredients.get(i);
//                int amountRequired = fluidIngredient.getRequiredAmount();
//
//                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
//                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
//                    if (fluidStack.getAmount() <= extractedFluidsFromTank[tank])
//                        continue;
//                    if (!fluidIngredient.test(fluidStack))
//                        continue;
//                    int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
//                    if (!simulate) {
//                        fluidStack.shrink(drainedAmount);
//                        fluidsAffected = true;
//                    }
//                    amountRequired -= drainedAmount;
//                    if (amountRequired != 0)
//                        continue;
//                    extractedFluidsFromTank[tank] += drainedAmount;
//                    continue FluidIngredients;
//                }
//                // something wasn't found
//                return false;
//            }

//
//            if (fluidsAffected) {
//                centrifuge.getBehaviour(SmartFluidTankBehaviour.INPUT)
//                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
//                centrifuge.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
//                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
//            }


            IFluidHandler targetTank = centrifuge.outputTank.getCapability()
                    .orElse(null);

            for (FluidStack fluidStack : recipe.getFluidResults()) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                        ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                        : targetTank.fill(fluidStack.copy(), action);
                if (fill != fluidStack.getAmount())
                    return false;
            }

            List<ItemStack> outputs = recipe.rollResults();

            for(ItemStack output : outputs){
                ItemStack remaining = ItemHandlerHelper.insertItemStacked(centrifuge.outputInv, output, simulate);
                if(simulate && !remaining.isEmpty())
                    return false;
            }
        }
        return true;

    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 6;
    }


    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }
}
