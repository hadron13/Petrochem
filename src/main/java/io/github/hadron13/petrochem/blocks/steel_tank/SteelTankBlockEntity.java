package io.github.hadron13.petrochem.blocks.steel_tank;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static java.lang.Math.abs;


public class SteelTankBlockEntity extends FluidTankBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer.Fluid {

    public boolean isDistillingColumn = false;
    public boolean[] occludedDirections = {true, true, true, true};
    public float heat;
    public BlockPos distillationController;

    public SteelTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    public void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        refreshCapability();
        ConnectivityHandler.formMulti(this);
    }

    public DistillationControllerBlockEntity getDistillationControllerBE(){
        SteelTankBlockEntity controller = getControllerBE();
        if(controller != null && controller.distillationController != null){
            return (DistillationControllerBlockEntity) level.getBlockEntity(controller.distillationController);
        }
        return null;
    }


    public void setDistillationMode(boolean active){
        isDistillingColumn = active;
        setWindows(!active);
    }

    @Override
    public void lazyTick() {
        if(!isController())
            return;

        if(!isDistillingColumn)
            return;
        for (Direction d : Iterate.horizontalDirections) {
            AABB aabb =
                    new AABB(getBlockPos()).move(width / 2f - .5f, 0, width / 2f - .5f)
                            .deflate(5f / 8);
            aabb = aabb.move(d.getStepX() * (width / 2f + 1 / 4f), 0,
                    d.getStepZ() * (width / 2f + 1 / 4f));
            aabb = aabb.inflate(Math.abs(d.getStepZ()) / 2f, 0.25f, Math.abs(d.getStepX()) / 2f);
            occludedDirections[d.get2DDataValue()] = !getLevel()
                    .noCollision(aabb);
        }

        heat = 0;
        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos pos = worldPosition.offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                heat += Math.max(BoilerHeater.findHeat(level, pos, blockState), 0);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
    }

     @SuppressWarnings("unchecked")
    @Override
    public SteelTankBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity tileEntity = level.getBlockEntity(controller);
        if (tileEntity instanceof SteelTankBlockEntity)
            return (SteelTankBlockEntity) tileEntity;
        return null;
    }

    public void removeController(boolean keepFluids) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyFluidTankSize(1);
        controller = null;
        width = 1;
        height = 1;

        onFluidStackChanged(tankInventory.getFluid());

        BlockState state = getBlockState();
        if (SteelTankBlock.isTank(state)) {
            state = state.setValue(SteelTankBlock.BOTTOM, true);
            state = state.setValue(SteelTankBlock.TOP, true);
            state = state.setValue(SteelTankBlock.SHAPE, window ? FluidTankBlock.Shape.WINDOW : FluidTankBlock.Shape.PLAIN);
            getLevel().setBlock(worldPosition, state, 22);
        }
        refreshCapability();
        setChanged();
        sendData();
    }

    public void toggleWindows() {
        SteelTankBlockEntity te = getControllerBE();
        if (te == null)
            return;
        if(te.isDistillingColumn)
            return;
        te.setWindows(!te.window);
    }


    public int getOutputNumber(){
        SteelTankBlockEntity te = getControllerBE();
        if (te == null)
            return -1;
        if(!te.isDistillingColumn)
            return -1;

        return (worldPosition.subtract(te.worldPosition).getY()+1)/2 + 1;
    }

    public boolean hasWindows(){
        return window;
    }
    public int getLuminosity(){
        return luminosity;
    }

    public void setWindows(boolean window) {
        this.window = window;
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!SteelTankBlock.isTank(blockState))
                        continue;
                    FluidTankBlock.Shape shape = FluidTankBlock.Shape.PLAIN;
                    if (window) {
                        // SIZE 1: Every tank has a window
                        if (width == 1)
                            shape = FluidTankBlock.Shape.WINDOW;
                        // SIZE 2: Every tank has a corner window
                        if (width == 2)
                            shape = xOffset == 0 ? zOffset == 0 ? FluidTankBlock.Shape.WINDOW_NW : FluidTankBlock.Shape.WINDOW_SW
                                    : zOffset == 0 ? FluidTankBlock.Shape.WINDOW_NE : FluidTankBlock.Shape.WINDOW_SE;
                        // SIZE 3: Tanks in the center have a window
                        if (width == 3 && abs(abs(xOffset) - abs(zOffset)) == 1)
                            shape = FluidTankBlock.Shape.WINDOW;
                    }

                    level.setBlock(pos, blockState.setValue(SteelTankBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
    }


    @Override
    public void updateBoilerState() {
        if (!isController())
            return;
    }


    @Override
    public void setController(BlockPos controller) {
        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }
    public void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = fluidCapability;
        fluidCapability = LazyOptional.of(() -> handlerForCapability());
        oldCap.invalidate();
    }

    private IFluidHandler handlerForCapability() {
        return isController() ?
                tankInventory
                : getControllerBE() != null ? getControllerBE().handlerForCapability() : tankInventory;
    }

    @Nullable
    public SteelTankBlockEntity getOtherFluidTankTileEntity(Direction direction) {
        BlockEntity otherTE = level.getBlockEntity(worldPosition.relative(direction));
        if (otherTE instanceof SteelTankBlockEntity)
            return (SteelTankBlockEntity) otherTE;
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        SteelTankBlockEntity controller = getControllerBE();
        if(controller == null || controller.isDistillingColumn)
            return LazyOptional.empty();
        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        SteelTankBlockEntity controllerTE = getControllerBE();

        if(controllerTE.isDistillingColumn){
            PetrochemLang.translate("gui.distil_layer")
                    .text("#" + (getOutputNumber()))
                    .forGoggles(tooltip);
            return true;
        }
        return containedFluidTooltip(tooltip, isPlayerSneaking,
                controllerTE.getCapability(ForgeCapabilities.FLUID_HANDLER));
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (SteelTankBlock.isTank(state)) { // safety
            state = state.setValue(SteelTankBlock.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(SteelTankBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);
        }
        if (isController())
            setWindows(window);
        onFluidStackChanged(tankInventory.getFluid());
        updateBoilerState();
        setChanged();
    }
}

