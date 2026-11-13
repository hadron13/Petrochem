package io.github.hadron13.petrochem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.hadron13.petrochem.blocks.steam_output.SteamOutputBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoilerData.class)
public class BoilerDataMixin {
    @WrapOperation(method = "evaluate",
        remap = false,
        at = @At(
            value = "INVOKE",
            ordinal = 0,
            target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private static boolean steamOutputWrapper(BlockEntry instance, BlockState state, Operation<Boolean> original){
        return original.call(instance, state) || state.getBlock() instanceof SteamOutputBlock;
    }
}
