package io.github.hadron13.gearbox.data.client.blockstates;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;

public abstract class HorizontalDirectionalBlockStateGen extends SpecialBlockStateGen {

    private int yFlip = 0;

    public HorizontalDirectionalBlockStateGen flipY(int degrees) {
        this.yFlip = degrees;
        return this;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        if (direction.getAxis()
                .isVertical())
            return 0;

        return (horizontalAngle(direction) + 180 + yFlip) % 360;
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    public abstract <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state);

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return prov.models().getExistingFile(prov.modLoc(getModelPrefix(ctx, prov, state)));
    }
}
