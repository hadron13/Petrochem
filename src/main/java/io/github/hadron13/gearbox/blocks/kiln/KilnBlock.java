package io.github.hadron13.gearbox.blocks.kiln;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.createmod.catnip.data.Iterate;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class KilnBlock extends HorizontalKineticBlock implements IBE<KilnBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public KilnBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.TANK;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredHorizontalFacing(context);
        if (preferred != null)
            return defaultBlockState().setValue(HORIZONTAL_FACING, preferred.getOpposite()).setValue(POWERED, false);;
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, false);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (!player.getItemInHand(handIn)
                .isEmpty())
            return InteractionResult.PASS;
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        withBlockEntityDo(worldIn, pos, kiln -> {
            boolean emptyOutput = true;
            IItemHandlerModifiable inv = kiln.outputInv;
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (!stackInSlot.isEmpty())
                    emptyOutput = false;
                player.getInventory()
                        .placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
            }

            if (emptyOutput) {
                inv = kiln.inputInv;
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    player.getInventory()
                            .placeItemBackInInventory(inv.getStackInSlot(slot));
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                    worldIn.setBlock(pos, state.setValue(KilnBlock.POWERED, false), 3);
                }
            }

            kiln.setChanged();
            kiln.sendData();
        });

        return InteractionResult.SUCCESS;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);

        if (entityIn.level().isClientSide)
            return;
        if (!(entityIn instanceof ItemEntity))
            return;
        if (!entityIn.isAlive())
            return;

        KilnBlockEntity kiln = null;
        for (BlockPos pos : Iterate.hereAndBelow(entityIn.blockPosition()))
            if (kiln == null)
                kiln = getBlockEntity(worldIn, pos);

        if (kiln == null)
            return;

        ItemEntity itemEntity = (ItemEntity) entityIn;
        LazyOptional<IItemHandler> capability = kiln.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!capability.isPresent())
            return;

        ItemStack remainder = capability.orElse(new ItemStackHandler())
                .insertItem(0, itemEntity.getItem(), false);
        if (remainder.isEmpty())
            itemEntity.discard();
        if (remainder.getCount() < itemEntity.getItem()
                .getCount())
            itemEntity.setItem(remainder);
    }


    @Override
    public Class<KilnBlockEntity> getBlockEntityClass() {
        return KilnBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends KilnBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.KILN.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}