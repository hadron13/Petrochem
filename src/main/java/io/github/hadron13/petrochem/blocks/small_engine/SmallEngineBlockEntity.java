package io.github.hadron13.petrochem.blocks.small_engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import io.github.hadron13.petrochem.register.PetrochemSoundEvents;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.scene.VectorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerBlock.HORIZONTAL_FACING;
import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.getBlockPos;
import static net.minecraft.core.Direction.Axis.X;

public class SmallEngineBlockEntity extends GeneratingKineticBlockEntity {

    public SmartFluidTankBehaviour tank;
    public EngineFuelRecipe currentFuel = null;
    public ScrollValueBehaviour targetSpeed;
    public float consumptionCounter = 0;
    public SmallEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, this, 1, 4000, true);
        tank.whenFluidUpdates(this::fluidUpdate);
        behaviours.add(tank);

        targetSpeed = new KineticScrollValueBehaviour(CreateLang.translateDirect("kinetics.speed_controller.rotation_speed"),
                this, new SpeedValueBoxTransform());
        targetSpeed.between(-256, 256);
        targetSpeed.value = 64;
        targetSpeed.withCallback(i -> this.updateGeneratedRotation());

        behaviours.add(targetSpeed);
    }


    private class SpeedValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
            TransformStack.of(ms)
                    .rotateZDegrees(-AngleHelper.horizontalAngle(state.getValue(HORIZONTAL_FACING)) );

        }

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction == Direction.UP;
        }
    }

    public void fluidUpdate(){

        FluidStack fluid = tank.getPrimaryHandler().getFluidInTank(0);
        if(fluid.isEmpty()){
            if(currentFuel != null){
                updateGeneratedRotation();
            }
            currentFuel = null;
        }else{
            if(currentFuel == null){
                List<EngineFuelRecipe> allFuels = level.getRecipeManager().getAllRecipesFor(PetrochemRecipeTypes.GASOLINE_ENGINE_FUEL.getType());

                Optional<EngineFuelRecipe> matchingFuel =
                        allFuels.stream().filter(recipe -> recipe.match(fluid) ).findAny();
                if(matchingFuel.isEmpty())
                    return;
                currentFuel = matchingFuel.get();
                updateGeneratedRotation();
            }
        }
    }


    @Override
    public void tick() {
        super.tick();

        if(currentFuel != null){
            consumptionCounter += currentFuel.getConsumptionRate() * (Mth.abs(getGeneratedSpeed())/64f);
            if(consumptionCounter > 1f){
                tank.getPrimaryHandler().drain(Mth.floor(consumptionCounter), IFluidHandler.FluidAction.EXECUTE);
                consumptionCounter = Mth.frac(consumptionCounter);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        if(getSpeed() == 0)
            return;

//        PetrochemSoundEvents.SMALL_ENGINE_HUMMING.playAt(level, getBlockPos(), 0.3f, 0.5f, false);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }

    @Override
    public float calculateAddedStressCapacity() {
//        return super.calculateAddedStressCapacity();
        float speed = getGeneratedSpeed();
        if(speed == 0.0)
            return 0;
        return super.calculateAddedStressCapacity() * 256f/Mth.abs(getGeneratedSpeed());
    }

    @Override
    public float getGeneratedSpeed() {
        if(level.isClientSide)
            return getSpeed();
        if(currentFuel == null)
            return 0;
        return  convertToDirection(targetSpeed.getValue(), getBlockState().getValue(HORIZONTAL_FACING));
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
        return true;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side == Direction.DOWN))
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }
}
