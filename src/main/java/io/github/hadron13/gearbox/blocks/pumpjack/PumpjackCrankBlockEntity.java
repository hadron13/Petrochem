package io.github.hadron13.gearbox.blocks.pumpjack;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PumpjackCrankBlockEntity extends KineticBlockEntity {

    float angle = 0; //degrees
    LerpedFloat visualSpeed = LerpedFloat.linear();

    public PumpjackCrankBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(5);
        visualSpeed.chase(0f, 1 / 128f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();

//        if(level != null && level.isClientSide)
//            return;



        float targetSpeed = Mth.log2((int)Mth.abs(getSpeed())) * 3.3f;
        if(Mth.abs(getSpeed()) < 32){
            targetSpeed = 0;
        }

        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        angle += visualSpeed.getValue() * 6/20f;
        angle %= 360;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        sendData();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("angle", angle);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        angle = compound.getFloat("angle");
//        if (clientPacket)
//            visualSpeed.chase(getSpeed()/8f, 1 / 128f, LerpedFloat.Chaser.EXP);
    }
}
