package io.github.hadron13.petrochem.mixin;


import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.blocks.basin_shroud.BasinShroudBlock;
import io.github.hadron13.petrochem.config.PetrochemConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BasinBlockEntity.class)
public class BasinBlockEntityMixin {


    @Shadow protected IFluidHandler fluidCapability;

    @Shadow
    private boolean contentsChanged;


    private static boolean isSealed(BasinBlockEntity be){
        Level level = be.getLevel();
        if(level == null)
            return false;

        BlockPos pos = be.getBlockPos();
        return level.getBlockState(pos.above()).getBlock() instanceof BasinShroudBlock &&
                level.getBlockEntity(pos.above(2)) instanceof BasinOperatingBlockEntity;
    }

    @Inject(method = "lazyTick", at = @At("HEAD"), remap = false)
    public void petrochem$lazyTick(CallbackInfo ci){

        if(!PetrochemConfig.common().basinsLeakGas.get())
            return;
        BasinBlockEntity be = (BasinBlockEntity)(Object)this;

        if(isSealed(be))
            return;

        IFluidHandler fluids = this.fluidCapability;
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (!fluidStack.getFluidType().isLighterThanAir())
                continue;

            FluidStack to_drain = fluidStack.copyWithAmount(Mth.ceil(fluidStack.getAmount() * 0.03));

            fluids.drain(to_drain, IFluidHandler.FluidAction.EXECUTE);
            contentsChanged = true;
        }
    }

    @Inject(method = "addToGoggleTooltip", at=@At("HEAD"), remap = false)
    public void addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir){

        if(!PetrochemConfig.common().basinsLeakGas.get())
            return;
        boolean hasGas = false;

        IFluidHandler fluids = this.fluidCapability;
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if(fluidStack.isEmpty())
                continue;
            if (fluidStack.getFluidType().isLighterThanAir()) {
                hasGas = true;
                break;
            }
        }
        if(!hasGas)
            return;

        BasinBlockEntity be = (BasinBlockEntity)(Object)this;
        if(!isSealed(be))
            PetrochemLang.addHint(tooltip, "hint.basin_unsealed");

    }
}
