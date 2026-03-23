package io.github.hadron13.gearbox.data.client.blockstates;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PartialHorizontalBlockStateGen extends HorizontalDirectionalBlockStateGen {
    @Override
    public <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return "block/" + ctx.getName() + "/block";
    }
}
