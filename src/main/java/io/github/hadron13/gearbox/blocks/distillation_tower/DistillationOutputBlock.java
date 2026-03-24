package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlock;
import io.github.hadron13.gearbox.register.PetrochemBlockEntities;
import io.github.hadron13.gearbox.register.PetrochemBlocks;
import io.github.hadron13.gearbox.register.PetrochemShapes;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class DistillationOutputBlock extends Block implements IBE<DistillationOutputBlockEntity>, IWrenchable {


    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final Property<Direction> TANK_FACE = DirectionProperty.create("tank_face");
    public static final Property<Boolean> POWERED = BlockStateProperties.POWERED;

    public DistillationOutputBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TANK_FACE, POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Direction tank_direction = Direction.DOWN;
        Direction facing = context.getNearestLookingDirection().getOpposite();

        Level l = context.getLevel();

        for(Direction d : Iterate.directions){
            BlockState neighbour = l.getBlockState(context.getClickedPos().relative(d));
            if(neighbour.getBlock() instanceof SteelTankBlock){
                tank_direction = d;
                break;
            }
        }

        for(Direction d : Iterate.directions){
            BlockEntity neighbour = l.getBlockEntity(context.getClickedPos().relative(d));
            if(neighbour instanceof SmartBlockEntity sneighbour && sneighbour.getBehaviour(FluidTransportBehaviour.TYPE) != null){
                BlockState neighbourState = l.getBlockState(context.getClickedPos().relative(d));
                if(sneighbour instanceof StraightPipeBlockEntity && neighbourState.getValue(AxisPipeBlock.AXIS) != d.getAxis()){
                    break;
                }
                facing = d;
                break;
            }
        }
        if(facing == tank_direction) facing = tank_direction.getOpposite();

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(TANK_FACE, tank_direction)
                .setValue(POWERED, l.hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide)
            return;
        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != worldIn.hasNeighborSignal(pos))
            worldIn.setBlock(pos, state.cycle(POWERED), 2);
    }



    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(TANK_FACE))).is(PetrochemBlocks.STEEL_FLUID_TANK.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return PetrochemShapes.DISTILLATION_OUTPUT.get(state.getValue(FACING));
    }

    @Override
    public Class<DistillationOutputBlockEntity> getBlockEntityClass() {
        return DistillationOutputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistillationOutputBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.DISTILLATION_OUTPUT.get();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
}
