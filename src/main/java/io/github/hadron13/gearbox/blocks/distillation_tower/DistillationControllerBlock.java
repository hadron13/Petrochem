package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlock;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.levelWrappers.WrappedLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.simibubi.create.content.kinetics.gauge.GaugeBlock.GAUGE;

public class DistillationControllerBlock extends Block implements IBE<DistillationControllerBlockEntity> {

    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty AXIS_ALONG_FIRST_COORDINATE = BooleanProperty.create("axis_along_first");

    public DistillationControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(GearboxBlocks.STEEL_FLUID_TANK.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_ALONG_FIRST_COORDINATE);
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        boolean alongFirst = false;
        Direction.Axis faceAxis = facing.getAxis();

        if (faceAxis.isVertical()) {
            alongFirst = context.getHorizontalDirection().getAxis() == Direction.Axis.Z;
        }
        if(faceAxis == Direction.Axis.Z){
            alongFirst = true;
        }
        for (Direction d : Iterate.directions){
            BlockState neighbour = context.getLevel().getBlockState(context.getClickedPos().relative(d));
            if(neighbour.getBlock() instanceof SteelTankBlock){
                facing = d.getOpposite();
                break;
            }
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(AXIS_ALONG_FIRST_COORDINATE, alongFirst);
    }

    public static boolean shouldRenderHeadOnFace(Level world, BlockPos pos, BlockState state, Direction face) {
        if (face.getAxis()
                .isVertical())
            return false;
        if (face == state.getValue(FACING)
                .getOpposite())
            return false;
        if (face.getAxis() == getAxis(state))
            return false;
        if (getAxis(state) == Direction.Axis.Y && face != state.getValue(FACING))
            return false;
        if (!Block.shouldRenderFace(state, world, pos, face, pos.relative(face)) && !(world instanceof WrappedLevel))
            return false;
        return true;
    }

    public static boolean shouldRenderHeadOnFaceStatic(BlockState state, Direction face) {
        if (face.getAxis()
                .isVertical())
            return false;
        if (face == state.getValue(FACING)
                .getOpposite())
            return false;
        if (face.getAxis() == getAxis(state))
            return false;
        if (getAxis(state) == Direction.Axis.Y && face != state.getValue(FACING))
            return false;
        return true;
    }

    public static Direction.Axis getAxis(BlockState state) {
        Direction.Axis pistonAxis = state.getValue(FACING)
                .getAxis();
        boolean alongFirst = state.getValue(AXIS_ALONG_FIRST_COORDINATE);

        if (pistonAxis == Direction.Axis.X)
            return alongFirst ? Direction.Axis.Y : Direction.Axis.Z;
        if (pistonAxis == Direction.Axis.Y)
            return alongFirst ? Direction.Axis.X : Direction.Axis.Z;
        if (pistonAxis == Direction.Axis.Z)
            return alongFirst ? Direction.Axis.X : Direction.Axis.Y;

        throw new IllegalStateException("Unknown axis??");
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return GAUGE.get(state.getValue(FACING), state.getValue(AXIS_ALONG_FIRST_COORDINATE));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public Class<DistillationControllerBlockEntity> getBlockEntityClass() {
        return DistillationControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistillationControllerBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.DISTILLATION_CONTROLLER.get();
    }
}
