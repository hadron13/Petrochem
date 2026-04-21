package io.github.hadron13.petrochem.blocks.medium_engine;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import io.github.hadron13.petrochem.blocks.small_engine.EngineFuelRecipe;
import io.github.hadron13.petrochem.blocks.small_engine.EngineSoundInstance;
import io.github.hadron13.petrochem.register.PetrochemBlocks;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MediumEngineBlockEntity extends SteamEngineBlockEntity implements IHaveGoggleInformation {


    public boolean initialized = false;


    @OnlyIn(Dist.CLIENT)
    public EngineSoundInstance soundInstance;
    public SmartFluidTankBehaviour tank;
    public EngineFuelRecipe currentFuel = null;
//    public ScrollValueBehaviour targetSpeed;
    public float consumptionCounter = 0;
    public float load = 0;
    public float consumption = 0;

    public MediumEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, this, 1, 4000, true);
        tank.whenFluidUpdates(this::fluidUpdate);
        behaviours.add(tank);
    }

    public void fluidUpdate(){
        FluidStack fluid = tank.getPrimaryHandler().getFluidInTank(0);
        if(fluid.isEmpty()){
            if(currentFuel != null){
            }
            currentFuel = null;
        }else{
            if(currentFuel == null){
                List<EngineFuelRecipe> allFuels = level.getRecipeManager().getAllRecipesFor(PetrochemRecipeTypes.DIESEL_ENGINE_FUEL.getType());

                Optional<EngineFuelRecipe> matchingFuel =
                        allFuels.stream().filter(recipe -> recipe.match(fluid) ).findAny();
                if(matchingFuel.isEmpty())
                    return;
                currentFuel = matchingFuel.get();
                updateRotation();
            }
        }
    }

    public void updateRotation(){
        PoweredShaftBlockEntity shaft = getShaft();
        if (shaft == null)
            return;

        Direction facing = MediumEngineBlock.getFacing(getBlockState());

        BlockState shaftState = shaft.getBlockState();
        Direction.Axis targetAxis = Direction.Axis.X;
        if (shaftState.getBlock() instanceof IRotate ir)
            targetAxis = ir.getRotationAxis(shaftState);
        boolean verticalTarget = targetAxis == Direction.Axis.Y;

        BlockState blockState = getBlockState();
        if (!PetrochemBlocks.MEDIUM_ENGINE.has(blockState))
            return;

        if (facing.getAxis() == Direction.Axis.Y)
            facing = blockState.getValue(MediumEngineBlock.FACING);

        float efficiency = currentFuel != null? 1.0f : 0.0f;

        int conveyedSpeedLevel =
                efficiency == 0 ? 1 : verticalTarget ? 1 : (int) GeneratingKineticBlockEntity.convertToDirection(1, facing);
        if (targetAxis == Direction.Axis.Z)
            conveyedSpeedLevel *= -1;
//        if (movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE)
//            conveyedSpeedLevel *= -1;

        float shaftSpeed = shaft.getTheoreticalSpeed();
        if (shaft.hasSource() && shaftSpeed != 0 && conveyedSpeedLevel != 0
                && (shaftSpeed > 0) != (conveyedSpeedLevel > 0)) {
//            movementDirection.setValue(1 - movementDirection.get()
//                    .ordinal());
            conveyedSpeedLevel *= -1;
        }

        shaft.update(worldPosition, conveyedSpeedLevel, efficiency);
    }


    @Override
    public void tick() {

        //SmartBlockEntity tick, skip SteamEngineBlockEntity
        {
            if (!initialized && hasLevel()) {
                initialize();
                initialized = true;
            }

            if (lazyTickCounter-- <= 0) {
                lazyTickCounter = lazyTickRate;
                lazyTick();
            }

            forEachBehaviour(BlockEntityBehaviour::tick);
        }


        PoweredShaftBlockEntity shaft = getShaft();
        if (shaft == null)
            return;

        Direction facing = MediumEngineBlock.getFacing(getBlockState());


        if(currentFuel != null && shaft.getGeneratedSpeed() != 0){
            consumptionCounter += getConsumption();
            if(consumptionCounter > 1f){
                tank.getPrimaryHandler().drain(Mth.floor(consumptionCounter), IFluidHandler.FluidAction.EXECUTE);
                consumptionCounter = Mth.frac(consumptionCounter);
            }
            if(tank.isEmpty()){
                updateRotation();
            }
        }

        if(currentFuel == null || tank.isEmpty()){
            if (level.isClientSide())
                return;
            if (!shaft.getBlockPos()
                    .subtract(worldPosition)
                    .equals(shaft.enginePos))
                return;
            if (shaft.engineEfficiency == 0)
                return;

            if (level.isLoaded(worldPosition.relative(facing.getOpposite())))
                shaft.update(worldPosition, 0, 0);
        }


        if (!level.isClientSide)
            return;
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::spawnParticles);
    }

    public float getConsumption(){
        if(currentFuel == null)
            return 0;
        return currentFuel.getConsumptionRate() * (float)Math.max(load, 0.3) ;
    }


    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Float getTargetAngle() {
        float angle = 0;
        BlockState blockState = getBlockState();
        if (!PetrochemBlocks.MEDIUM_ENGINE.has(blockState))
            return null;

        Direction facing = MediumEngineBlock.getFacing(blockState);
        PoweredShaftBlockEntity shaft = getShaft();
        Direction.Axis facingAxis = facing.getAxis();
        Direction.Axis axis = Direction.Axis.Y;

        if (shaft == null)
            return null;

        axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
        angle = KineticBlockEntityRenderer.getAngleForBe(shaft, shaft.getBlockPos(), axis);

        if (axis == facingAxis)
            return null;
        if (axis.isHorizontal() && (facingAxis == Direction.Axis.X ^ facing.getAxisDirection() == Direction.AxisDirection.POSITIVE))
            angle *= -1;
        if (axis == Direction.Axis.X && facing == Direction.DOWN)
            angle *= -1;
        return angle;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER)
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        PoweredShaftBlockEntity shaft = getShaft();
        if (shaft != null)
            shaft.remove(worldPosition);
        super.remove();
    }


}
