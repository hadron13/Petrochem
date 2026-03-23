package io.github.hadron13.gearbox.data.client.blockstates;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;

public class PartialAxisBlockStateGen extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
        return axis == Direction.Axis.Y ? 0 : 90;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
        return axis == Direction.Axis.X ? 90 : axis == Direction.Axis.Z ? 180 : 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return prov.models().getExistingFile(prov.modLoc("block/" + ctx.getName() + "/block"));
    }
}
