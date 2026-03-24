package io.github.hadron13.gearbox.blocks.pumpjack;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.hadron13.gearbox.PetrochemLang;
import io.github.hadron13.gearbox.register.PetrochemFluids;
import io.github.hadron13.gearbox.register.PetrochemRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.hadron13.gearbox.blocks.pumpjack.PumpjackWellBlock.HORIZONTAL_FACING;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class PumpjackWellBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tank;
    public AABB renderBoundingBox;
    public PumpjackRecipe currentRecipe;


    public static ArrayList<BlockPos> loadedWells = new ArrayList<>();
    public boolean isPipingValid = false;
    public float efficiency = 1f;

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition.offset(-2, -2, -2), worldPosition.offset(2, 2, 2));
        }
        return renderBoundingBox;
    }

    public PumpjackWellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setLevel(Level pLevel) {
        if(!hasLevel() && !pLevel.isClientSide){
            loadedWells.add(getBlockPos());
        }
        super.setLevel(pLevel);
    }

    @Override
    public void remove() {
        super.remove();
        if(!level.isClientSide)
            loadedWells.remove(getBlockPos());
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(!level.isClientSide)
            loadedWells.remove(getBlockPos());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 2000);
        tank.forbidInsertion();
        behaviours.add(tank);

    }


    public void updateRecipe(){
        if(currentRecipe == null || !PumpjackRecipe.match(this, currentRecipe)){
            Optional<PumpjackRecipe> match = PetrochemRecipeTypes.PUMPJACK.find(this, getLevel());
            if(match.isEmpty())
                return;
            currentRecipe = match.get();
        }
    }

    public void updateEfficiency(){
        BlockPos pos = getBlockPos();
        efficiency = 1f;
        for(BlockPos other : loadedWells){
            if(pos.equals(other))
                continue;

            if(pos.closerThan(other.atY(pos.getY()), 8)){
                efficiency *= 0.75f;
            }
        }
        sendData();
    }

    public boolean validatePiping(){
        if(level == null)
            return true;
        BlockPos position = getBlockPos().below();
        BlockState block = level.getBlockState(position);
        while(block.getBlock() != Blocks.BEDROCK){
            Block[] validBlocks = {
                    AllBlocks.FLUID_PIPE.get(),
                    AllBlocks.GLASS_FLUID_PIPE.get(),
                    AllBlocks.ENCASED_FLUID_PIPE.get()
            };
            if(!(block.getBlock() instanceof FluidPipeBlock) && !(block.getBlock() instanceof AxisPipeBlock))
                return false;

            position = position.below();
            block = level.getBlockState(position);
        }
        return true;

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        isPipingValid = validatePiping();
        if(isVirtual()){
            tank.allowInsertion();
            tank.getPrimaryHandler().fill(new FluidStack(PetrochemFluids.PETROLEUM.get(), 2000), EXECUTE);
        }
    }

    public void pump(){
        if(currentRecipe == null)
            return;
        if(!isPipingValid)
            return;

        updateEfficiency();

        if(isTankFull())
            return;

        FluidStack result = currentRecipe.getFluidResult().copy();
        result.setAmount((int) ((float)result.getAmount() * efficiency));
        tank.allowInsertion();
        tank.getPrimaryHandler().fill(result, EXECUTE);
        tank.forbidInsertion();

    }



    public boolean isTankFull(){
        return tank.getPrimaryHandler().getFluidAmount() == 2000;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isFluidHandlerCap(cap)
                && (side == null || getBlockState().getValue(HORIZONTAL_FACING) == side))
            return tank.getCapability().cast();

        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(efficiency < 1f){
            int percentage = (int)(efficiency * 100f);

            PetrochemLang.translate("gui.pumpjack_well.efficiency")
                    .add(PetrochemLang.text(" " + percentage + "%"))
                    .forGoggles(tooltip);
            PetrochemLang.translate("gui.pumpjack_well.other_wells")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
        }
        if(!isPipingValid){
            PetrochemLang.addHint(tooltip, "hint.pumpjack_well.pipes");
            PetrochemLang.text("").forGoggles(tooltip);
        }

        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putFloat("efficiency", efficiency);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        efficiency = tag.getFloat("efficiency");
    }
}
