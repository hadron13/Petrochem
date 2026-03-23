package io.github.hadron13.gearbox.data.client.blockstates;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class KilnGenerator extends HorizontalDirectionalBlockStateGen {

    @Override
    public <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        boolean powered = state.getValue(BlockStateProperties.POWERED);
        String suffix = powered ? "/powered" : "/unpowered";
        return "block/" + ctx.getName() + suffix;
    }
}
