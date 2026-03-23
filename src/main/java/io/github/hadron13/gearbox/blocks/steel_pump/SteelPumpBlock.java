package io.github.hadron13.gearbox.blocks.steel_pump;

import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteelPumpBlock extends PumpBlock {
    public SteelPumpBlock(Properties p_i48415_1_) {
        super(p_i48415_1_);
    }

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.STEEL_FLUID_PUMP.get();
    }
}
