package io.github.hadron13.petrochem.blocks.pumpjack;


import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.petrochem.register.PetrochemBlockEntities;
import io.github.hadron13.petrochem.register.PetrochemShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class PumpjackCrankBlock extends HorizontalKineticBlock implements IBE<PumpjackCrankBlockEntity> {




    public PumpjackCrankBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public Class<PumpjackCrankBlockEntity> getBlockEntityClass() {
        return PumpjackCrankBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends PumpjackCrankBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.PUMPJACK_CRANK.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING);
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn,
                               BlockPos pos, CollisionContext context) {
        return PetrochemShapes.PUMPJACK_CRANK.get(state.getValue(HORIZONTAL_FACING));
    }
}
