package io.github.hadron13.petrochem.blocks.pumpjack;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static io.github.hadron13.petrochem.blocks.pumpjack.PumpjackArmBlock.HORIZONTAL_FACING;

public class PumpjackArmBlockEntity extends SmartBlockEntity  {

    public boolean pumped;
    public PumpjackCrankBlockEntity crank;
    public PumpjackWellBlockEntity well;


    public PumpjackArmBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(5);
    }


    public AABB renderBoundingBox = new AABB(worldPosition.offset(-2, -2, -2), worldPosition.offset(2, 1, 2));

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return renderBoundingBox;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(level == null)
            return;
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        BlockEntity crank_be= level.getBlockEntity(getBlockPos().below(2).relative(facing, 2));
        BlockEntity well_be = level.getBlockEntity(getBlockPos().below(2).relative(facing, -2));
        if(crank_be instanceof PumpjackCrankBlockEntity){
            crank = (PumpjackCrankBlockEntity) crank_be;
        }else{
            crank = null;
        }

        if(well_be instanceof PumpjackWellBlockEntity){
            well = (PumpjackWellBlockEntity) well_be;
        }else{
            well = null;
        }

        if(crank == null || well == null)
            return;




        if(Mth.abs(crank.getSpeed()) > 0f ){
            float crank_angle = Mth.abs(crank.angle);
            if(crank_angle > 100 && !pumped){
                pumped = true;

                if(level.isClientSide ){
                    float pitch = Mth.clamp((Math.abs(crank.visualSpeed.getValue()) / 256f) , 0, 0.75f);
                    BlockPos pos = getBlockPos();

                    level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                            AllSoundEvents.TRAIN.getMainEvent(), SoundSource.AMBIENT,0.015f, pitch, false);
                    return;
                }
                well.updateRecipe();
                well.pump();
            }else if(pumped && crank_angle < 100){


                pumped = false;

            }
        }
    }



}
