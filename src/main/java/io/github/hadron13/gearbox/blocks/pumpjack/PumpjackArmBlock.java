package io.github.hadron13.gearbox.blocks.pumpjack;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import io.github.hadron13.gearbox.register.GearboxShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PumpjackArmBlock extends Block implements IBE<PumpjackArmBlockEntity>, IWrenchable {

    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn,
                               BlockPos pos, CollisionContext context) {
        return GearboxShapes.PUMPJACK_PIVOT.get(state.getValue(HORIZONTAL_FACING));
    }

    public PumpjackArmBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection()
                        .getOpposite());
    }

    @Override
    public Class<PumpjackArmBlockEntity> getBlockEntityClass() {
        return PumpjackArmBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends PumpjackArmBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.PUMPJACK_ARM.get();
    }

}
