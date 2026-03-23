package io.github.hadron13.gearbox.data.client.blockstates;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PumpjackGenerator extends HorizontalDirectionalBlockStateGen {

    private final String part;

    public PumpjackGenerator(String part) {
        this.part = part;
    }

    public static PumpjackGenerator arm() {
        return new PumpjackGenerator("arm_holder");
    }

    public static PumpjackGenerator crank() {
        return new PumpjackGenerator("crank_holder");
    }

    public static PumpjackGenerator well() {
        return new PumpjackGenerator("well");
    }

    @Override
    public <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return "block/pumpjack/" + part;
    }
}
