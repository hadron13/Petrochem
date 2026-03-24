package io.github.hadron13.petrochem.blocks.flarestack;

import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.petrochem.register.PetrochemBlockEntities;
import io.github.hadron13.petrochem.register.PetrochemShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlarestackBlock extends Block implements IBE<FlarestackBlockEntity> {
    public FlarestackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return PetrochemShapes.FLARESTACK.get(Direction.UP);
    }

    @Override
    public Class<FlarestackBlockEntity> getBlockEntityClass() {
        return FlarestackBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FlarestackBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.FLARESTACK.get();
    }
}
