package io.github.hadron13.petrochem.blocks.distillation_tower;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelTankBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputBlock.*;

public class DistillationOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tankInventory;
    public boolean duplicate = false;

    public DistillationOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tankInventory = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 4000, true)
                .whenFluidUpdates(this::sendData)
                .forbidInsertion();
        behaviours.add(tankInventory);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if(level.isClientSide)
            return;


        if(getBlockState().getValue(POWERED)){
            tankInventory.getPrimaryHandler().drain(500, IFluidHandler.FluidAction.EXECUTE);
            sendData();
        }

        BlockEntity be = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(TANK_FACE)));
        if(be instanceof SteelTankBlockEntity tank){
            DistillationControllerBlockEntity controller = tank.getDistillationControllerBE();
            if(controller != null ){

                boolean wasDuplicate = duplicate;
                duplicate = controller.addOutput(getOutputNumber(), worldPosition);
                if(wasDuplicate != duplicate)
                    sendData();
            }
        }
    }

    @Override
    public void remove() {
        super.remove();

        if(level.isClientSide)
            return;
        BlockEntity be = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(TANK_FACE)));
        if(be instanceof SteelTankBlockEntity tank){
            DistillationControllerBlockEntity controller = tank.getDistillationControllerBE();
            if(controller != null){
                controller.removeOutput(getOutputNumber());
            }
        }
    }

    public int getOutputNumber(){
        int output = -1;
        BlockEntity be = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(TANK_FACE)));
        if(be instanceof SteelTankBlockEntity tank){
            output = tank.getOutputNumber();
        }
        return output;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        if(duplicate){
            PetrochemLang.translate("gui.distil_duplicate")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
        }

        int output = getOutputNumber();
        if(output != -1) {
            PetrochemLang.translate("gui.distil_layer")
                    .text("#" + output)
                    .forGoggles(tooltip);
            PetrochemLang.text("").forGoggles(tooltip);
        }


        containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));


        if(getBlockState().getValue(POWERED)) {
            PetrochemLang.text("").forGoggles(tooltip);
            PetrochemLang.addHint(tooltip, "hint.distil.discard");
        }
        return true;
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putBoolean("dup", duplicate);
        tankInventory.write(tag, clientPacket);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        duplicate = tag.getBoolean("dup");
        tankInventory.read(tag, clientPacket);
        super.read(tag, clientPacket);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side == getBlockState().getValue(FACING))){
            return tankInventory.getCapability().cast();
        }
        return super.getCapability(cap, side);
    }
}
