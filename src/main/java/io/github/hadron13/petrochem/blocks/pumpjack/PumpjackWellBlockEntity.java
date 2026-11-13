package io.github.hadron13.petrochem.blocks.pumpjack;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.register.PetrochemBlockEntities;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.createmod.catnip.data.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.github.hadron13.petrochem.blocks.pumpjack.PumpjackWellBlock.HORIZONTAL_FACING;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class PumpjackWellBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tank;
    public AABB renderBoundingBox;
    public PumpjackRecipe currentRecipe;


    public static Set<Pair<ResourceKey<Level>,BlockPos>> loadedWells = new HashSet<>();
    public boolean isPipingValid = false;
    public float efficiency = 1f;

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition.offset(-2, -2, -2).getCenter(), worldPosition.offset(2, 2, 2).getCenter());
        }
        return renderBoundingBox;
    }

    public PumpjackWellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setLevel(Level pLevel) {
        if(!hasLevel() && !pLevel.isClientSide){
            loadedWells.add(Pair.of(level.dimension(), getBlockPos()));
        }
        super.setLevel(pLevel);
    }

    @Override
    public void remove() {
        super.remove();
        if(!level.isClientSide)
            loadedWells.remove(Pair.of(level.dimension(), getBlockPos()));
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(!level.isClientSide)
            loadedWells.remove(Pair.of(level.dimension(), getBlockPos()));
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
        if(level == null)
            return;
        BlockPos pos = getBlockPos();
        efficiency = 1f;
        for(Pair<ResourceKey<Level>, BlockPos> other : loadedWells){
            if(!level.dimension().equals(other.getFirst()))
                continue;
            if(pos.equals(other.getSecond()))
                continue;

            if(pos.closerThan(other.getSecond().atY(pos.getY()), 4)){
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
            tank.getPrimaryHandler().fill(new FluidStack(PetrochemFluids.PETROLEUM, 2000), EXECUTE);
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



    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                PetrochemBlockEntities.PUMPJACK_WELL.get(),
                (be, context) -> {
                    if (context == null || be.getBlockState().getValue(HORIZONTAL_FACING) == context){
                        return be.tank.getCapability();
                    }
                    return null;
                }
        );
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

        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability());
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putFloat("efficiency", efficiency);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        efficiency = tag.getFloat("efficiency");
    }
}
