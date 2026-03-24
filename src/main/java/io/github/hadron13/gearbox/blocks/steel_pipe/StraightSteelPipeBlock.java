package io.github.hadron13.gearbox.blocks.steel_pipe;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import io.github.hadron13.gearbox.register.PetrochemBlockEntities;
import io.github.hadron13.gearbox.register.PetrochemBlocks;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.HitResult;


public class StraightSteelPipeBlock extends GlassFluidPipeBlock {
    public StraightSteelPipeBlock(Properties p_i48339_1_) {
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

        world.setBlockAndUpdate(pos, PetrochemBlocks.STEEL_GLASS_FLUID_PIPE.getDefaultState()
                .setValue(GlassFluidPipeBlock.AXIS, state.getValue(AXIS))
                .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));

        FluidTransportBehaviour.loadFlows(world, pos);
        return InteractionResult.SUCCESS;
    }


    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(PetrochemBlocks.STEEL_FLUID_PIPE.getDefaultState(), be);
    }
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
                                       Player player) {
        return PetrochemBlocks.STEEL_FLUID_PIPE.asStack();
    }


    @Override
    public BlockEntityType<? extends StraightPipeBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.STRAIGHT_STEEL_FLUID_PIPE.get();
    }
}
