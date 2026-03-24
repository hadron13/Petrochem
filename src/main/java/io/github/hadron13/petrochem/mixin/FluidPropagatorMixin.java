package io.github.hadron13.petrochem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidPropagator.class)
public class FluidPropagatorMixin {

    @WrapOperation(method = "propagateChangedPipe",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private static boolean pumpWrapper(BlockEntry instance, BlockState state, Operation<Boolean> original){
        return original.call(instance, state) || state.getBlock() instanceof PumpBlock;
    }


    @WrapOperation(method = "validateNeighbourChange",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private static boolean encasedPipeWrapper(BlockEntry instance, BlockState state, Operation<Boolean> original){
        return original.call(instance, state) || state.getBlock() instanceof EncasedPipeBlock;
    }
}
