package io.github.hadron13.gearbox.blocks.steel_pipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import io.github.hadron13.gearbox.register.PetrochemBlockEntities;
import io.github.hadron13.gearbox.register.PetrochemBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class SteelGlassPipeBlock extends GlassFluidPipeBlock {
    public SteelGlassPipeBlock(Properties p_i48339_1_) {
        super(p_i48339_1_);
    }


    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (tryRemoveBracket(context))
            return InteractionResult.SUCCESS;
        BlockState newState;
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidTransportBehaviour.cacheFlows(world, pos);

        Direction side = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS));
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;

        newState = AllBlocks.FLUID_PIPE.get()
            .updateBlockState(PetrochemBlocks.STEEL_FLUID_PIPE.getDefaultState()
                .setValue(facingToPropertyMap.get(side), true)
                .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos)
                .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));

        world.setBlock(pos, newState, 3);
        FluidTransportBehaviour.loadFlows(world, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(PetrochemBlocks.STEEL_FLUID_PIPE.getDefaultState(), be);
    }
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return PetrochemBlocks.STEEL_FLUID_PIPE.asStack();
    }



    @Override
    public BlockEntityType<? extends StraightPipeBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.STEEL_GLASS_FLUID_PIPE.get();
    }
}
