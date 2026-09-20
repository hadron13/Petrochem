package io.github.hadron13.petrochem.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.hadron13.petrochem.blocks.basin_shroud.BasinShroudBlock;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BasinRenderer.class)
public class BasinRendererMixin {

    private static boolean isSealed(BasinBlockEntity be){
        Level level = be.getLevel();
        if(level == null)
            return false;

        BlockPos pos = be.getBlockPos();
        return level.getBlockState(pos.above()).getBlock() instanceof BasinShroudBlock &&
                level.getBlockEntity(pos.above(2)) instanceof BasinOperatingBlockEntity;
    }

    @Inject(method = "renderFluids", at = @At("HEAD"), remap = false, cancellable = true)
    protected void renderFluids(BasinBlockEntity basin, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfoReturnable<Float> cir) {

        if(!isSealed(basin))
            return;

        SmartFluidTankBehaviour inputFluids = basin.getBehaviour(SmartFluidTankBehaviour.INPUT);
        SmartFluidTankBehaviour outputFluids = basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT);
        SmartFluidTankBehaviour[] tanks = { inputFluids, outputFluids };


        float totalLiquidUnits = 0;
        float totalGasUnits = 0;

        for (SmartFluidTankBehaviour behaviour : basin.getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.getRenderedFluid()
                        .isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                boolean isGas = tankSegment.getRenderedFluid().getFluidType().isLighterThanAir();

                if(isGas) totalGasUnits += units;
                else totalLiquidUnits += units;
            }
        }


        if (totalLiquidUnits < 1 && totalGasUnits < 1){
            cir.setReturnValue(0.0f);
            cir.cancel();
            return;
        }

        float fluidLevel = Mth.clamp(totalLiquidUnits / 2000, 0, 1);
        float gasLevel = Mth.clamp(totalGasUnits/ 2000, 0, 1);

        fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));
        gasLevel = 1 - ((1 - gasLevel) * (1 - gasLevel));

        float xMin = 2 / 16f;
        float xMax = 2 / 16f;
        final float yMin = 2 / 16f;
        final float yMax = yMin + 12 / 16f * fluidLevel;
        final float gasYMin = Mth.lerp(gasLevel, 34/16f, yMax);
        final float gasYMax = 34/16f;
        final float zMin = 2 / 16f;
        final float zMax = 14 / 16f;

        //render normal liquids

        for (SmartFluidTankBehaviour behaviour : tanks) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                FluidStack renderedFluid = tankSegment.getRenderedFluid();
                if (renderedFluid.isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                boolean isGas = renderedFluid.getFluidType().isLighterThanAir();
                if(isGas)
                    continue;

                float partial = Mth.clamp(units / totalLiquidUnits, 0, 1);
                xMax += partial * 12 / 16f;

                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax,
                        buffer, ms, light, false, false);

                xMin = xMax;
            }
        }

        xMin = 2 / 16f;
        xMax = 2 / 16f;

        //render gases on top

        for (SmartFluidTankBehaviour behaviour : tanks) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                FluidStack renderedFluid = tankSegment.getRenderedFluid();
                if (renderedFluid.isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                boolean isGas = renderedFluid.getFluidType().isLighterThanAir();
                if(!isGas)
                    continue;

                float partial = Mth.clamp(units / totalGasUnits, 0, 1);
                xMax += partial * 12 / 16f;

                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(renderedFluid, xMin, gasYMin, zMin, xMax, gasYMax, zMax,
                        buffer, ms, light, false, true);

                xMin = xMax;
            }
        }


        cir.setReturnValue(yMax);
        cir.cancel();
    }

}
