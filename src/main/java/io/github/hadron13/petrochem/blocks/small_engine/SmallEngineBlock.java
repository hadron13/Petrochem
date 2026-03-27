package io.github.hadron13.petrochem.blocks.small_engine;

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

public class SmallEngineBlock extends HorizontalKineticBlock implements IBE<SmallEngineBlockEntity>{

    public SmallEngineBlock(Properties properties) {
        super(properties);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn,
                               BlockPos pos, CollisionContext context) {
        return PetrochemShapes.SMALL_ENGINE.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(HORIZONTAL_FACING).getAxis() == face.getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }


    @Override
    public Class<SmallEngineBlockEntity> getBlockEntityClass() {
        return SmallEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SmallEngineBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.SMALL_ENGINE.get();
    }
}
