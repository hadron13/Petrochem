package io.github.hadron13.gearbox.blocks.flarestack;

import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlarestackBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tank;

    public FlarestackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if(tank.isEmpty())
            return;

        int amount = tank.getPrimaryHandler().getFluidAmount();

        if(level.isClientSide){
            Vec3 offset= new Vec3(0, 0.024f, 0);
            Vec3 particlePos = Vec3.atCenterOf(worldPosition.above());
            particlePos.add(level.random.nextFloat()/10, 0, level.random.nextFloat()/10);

            Vec3 velocity = VecHelper.offsetRandomly(offset, level.random, 0.03f).multiply(1, 5f, 1);

            for(int i = 0; i < (amount/20) + 1; i++)
                level.addParticle(ParticleTypes.FLAME,
                    particlePos.x + offset.x, particlePos.y + offset.y, particlePos.z + offset.z,
                    velocity.x, velocity.y, velocity.z);

            if(level.random.nextInt(4) == 1)
                level.addParticle(ParticleTypes.LARGE_SMOKE,
                    particlePos.x + offset.x, particlePos.y + offset.y, particlePos.z + offset.z,
                    velocity.x/5, velocity.y, velocity.z/5);

            if(level.random.nextInt(5) == 1){
                level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                        SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(level.isClientSide)
            return;
        int amount = tank.getPrimaryHandler().getFluidAmount();
        tank.getPrimaryHandler().drain(amount/2, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, this, 1, 4000, true);
        behaviours.add(tank);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side == Direction.DOWN))
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }
}
