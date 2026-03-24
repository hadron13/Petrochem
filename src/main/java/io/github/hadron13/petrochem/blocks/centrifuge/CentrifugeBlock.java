package io.github.hadron13.petrochem.blocks.centrifuge;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.petrochem.register.PetrochemBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CentrifugeBlock extends RotatedPillarKineticBlock implements ICogWheel, IBE<CentrifugeBlockEntity> {
    public CentrifugeBlock(Properties properties) {
        super(properties);
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public boolean isLargeCog() {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return CogWheelBlock.isValidCogwheelPosition(ICogWheel.isLargeCog(state), worldIn, pos, state.getValue(AXIS));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.LARGE_GEAR.get(state.getValue(AXIS));
    }

    public Direction.Axis getAxisForPlacement(BlockPlaceContext context) {
        return Direction.Axis.Y;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(AXIS, getAxisForPlacement(context));
    }

    @Override
    public float getParticleTargetRadius() {
        return 1.125f;
    }

    @Override
    public float getParticleInitialRadius() {
        return 1f;
    }

    @Override
    public Class<CentrifugeBlockEntity> getBlockEntityClass() {
        return CentrifugeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CentrifugeBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.CENTRIFUGE.get();
    }
}
