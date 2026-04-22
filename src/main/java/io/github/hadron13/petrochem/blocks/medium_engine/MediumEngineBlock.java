package io.github.hadron13.petrochem.blocks.medium_engine;

import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import io.github.hadron13.petrochem.register.PetrochemBlockEntities;
import io.github.hadron13.petrochem.register.PetrochemShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MediumEngineBlock extends SteamEngineBlock{

    public MediumEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SteamEngineBlockEntity> getBlockEntityType() {
        return PetrochemBlockEntities.MEDIUM_ENGINE.get();
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        BlockPos shaftPos = getShaftPos(pState, pPos);
        BlockState shaftState = pLevel.getBlockState(shaftPos);
        if (isShaftValid(pState, shaftState))
            pLevel.setBlock(shaftPos, PoweredShaftBlock.getEquivalent(shaftState), 3);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        AttachFace face = pState.getValue(FACE);
        Direction direction = pState.getValue(FACING);
        return face == AttachFace.CEILING ? PetrochemShapes.MEDIUM_ENGINE_CEILING.get(direction.getAxis())
                : face == AttachFace.FLOOR ? PetrochemShapes.MEDIUM_ENGINE.get(direction.getAxis())
                : PetrochemShapes.MEDIUM_ENGINE_WALL.get(direction);
    }

}
