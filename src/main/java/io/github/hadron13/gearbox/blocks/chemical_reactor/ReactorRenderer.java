package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import io.github.hadron13.gearbox.register.GearboxPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.platform.ForgeCatnipServices;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;


public class ReactorRenderer extends KineticBlockEntityRenderer<ReactorBlockEntity> {
    public ReactorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void renderSafe(ReactorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);

        boolean renderFluid = !be.atmosphere_tank.getPrimaryHandler().getFluidInTank(0).isEmpty();

        FluidStack atmosphere = FluidStack.EMPTY;
        float atm_level = 0;

        if (renderFluid) {
            atmosphere = be.atmosphere_tank.getPrimaryHandler().getFluidInTank(0);
            atm_level = be.atmosphere_tank.getPrimaryTank().getFluidLevel().getValue(partialTicks);
        }




        if(VisualizationManager.supportsVisualization(be.getLevel()) && renderFluid) {
            ForgeCatnipServices.FLUID_RENDERER.renderFluidBox(atmosphere, 0.0f, -atm_level, 0f, 1.0f, 0f, 1.0f,
                                    bufferSource, ms, light, true, true);
            return;
        }


        BlockState blockState = be.getBlockState();

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());



        float renderedHeadOffset = 1.1f;
        float speed = be.getRenderedHeadRotationSpeed(partialTicks);
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;

        SuperByteBuffer poleRender = CachedBuffers.partial(GearboxPartialModels.DIPPER_POLE, blockState);
        poleRender.translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        VertexConsumer vbCutout = bufferSource.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer headRender = CachedBuffers.partial(AllPartialModels.MECHANICAL_MIXER_HEAD, blockState);
        headRender.rotateCentered(angle, Direction.UP)
                .translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vbCutout);

        if (renderFluid)
            ForgeCatnipServices.FLUID_RENDERER.renderFluidBox(atmosphere, 0.0f, -atm_level, 0f, 1.0f, 0f, 1.0f,
                    bufferSource, ms, light, true, true);
    }
    @Override
    protected SuperByteBuffer getRotatedModel(ReactorBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.UP);
    }

}
