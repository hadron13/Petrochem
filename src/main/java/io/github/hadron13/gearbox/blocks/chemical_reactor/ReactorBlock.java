package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import io.github.hadron13.gearbox.register.GearboxShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ReactorBlock extends HorizontalKineticBlock implements IBE<ReactorBlockEntity>{

    public ReactorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return GearboxShapes.DIPPER.get(Direction.DOWN);
    }

    @Override
    public Class<ReactorBlockEntity> getBlockEntityClass() {
        return ReactorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorBlockEntity> getBlockEntityType() {
//        return GearboxBlockEntities.REACTOR.get();
        return null;
    }


}
