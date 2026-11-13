package io.github.hadron13.petrochem.blocks.steam_output;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.register.PetrochemBlockEntities;
import io.github.hadron13.petrochem.register.PetrochemFluids;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.lang.ref.WeakReference;
import java.util.List;

import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class SteamOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public WeakReference<FluidTankBlockEntity> source;
    public SmartFluidTankBehaviour outputTank;
    public float efficiency;

    public SteamOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        source = new WeakReference<>(null);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        outputTank = SmartFluidTankBehaviour.single(this, 4000);
        outputTank.forbidInsertion();
        behaviours.add(outputTank);
    }



    @Override
    public void tick() {
        super.tick();


        FluidTankBlockEntity tank = getTank();
        if(tank == null)
            return;

        efficiency = Mth.clamp(tank.boiler.getEngineEfficiency(tank.getTotalTankSize()), 0, 1);


        if(isTankFull())
            return;
        outputTank.getPrimaryHandler().fill(new FluidStack(PetrochemFluids.STEAM.get().getSource(), (int)(50 * efficiency)), EXECUTE);

    }


    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = SteamOutputBlock.getFacing(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));
            if (be instanceof FluidTankBlockEntity tankBe)
                source = new WeakReference<>(tank = tankBe);
        }
        if (tank == null)
            return null;
        return tank.getControllerBE();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean hasTooltip = false;

        if(containedFluidTooltip(tooltip, isPlayerSneaking, outputTank.getCapability()))
            hasTooltip = true;


        if(efficiency != 0){
            ;
            PetrochemLang.translate("gui.steam_output.output")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
            ;

            PetrochemLang.text((int)(efficiency * 50f) + "mb/t")
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip, 1);


            hasTooltip = true;
        }

        return hasTooltip;
    }

    public boolean isTankFull(){
        return outputTank.getPrimaryHandler().getFluidAmount() >= 4000;
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

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                PetrochemBlockEntities.STEAM_OUTPUT.get(),
                (be, context) -> {
                    if (context == null || SteamOutputBlock.getFacing(be.getBlockState()) == context){
                        return be.outputTank.getCapability();
                    }
                    return null;
                }
        );
    }
}
