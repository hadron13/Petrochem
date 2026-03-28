package io.github.hadron13.petrochem.blocks.small_engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import io.github.hadron13.petrochem.register.PetrochemPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SmallEngineRenderer extends KineticBlockEntityRenderer<SmallEngineBlockEntity> {
    public SmallEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }



    @Override
    protected void renderSafe(SmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);

        if (VisualizationManager.supportsVisualization(be.getLevel())) return;

        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        Direction facing = be.getBlockState().getValue(HORIZONTAL_FACING);
        SuperByteBuffer piston = CachedBuffers.partialFacing(PetrochemPartialModels.SMALL_PISTON, be.getBlockState(), facing);

        Vec3 front = new Vec3(facing.step());
        Vec3 up = new Vec3(Direction.UP.step());
        Vec3 right = front.cross(up);

        float t = AnimationTickHolder.getRenderTime()/20f * be.getSpeed()/60f * Mth.TWO_PI;

        piston.translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .light(light)
                .renderInto(ms, solid);

        t += Mth.HALF_PI;

        piston.translate(front.scale(7/16f))
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .light(light)
                .renderInto(ms, solid);

        t += Mth.HALF_PI;

        piston.rotateCentered(Mth.PI, Direction.Axis.Y)
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .light(light)
                .renderInto(ms, solid);

        t += Mth.HALF_PI;

        piston.translate(front.scale(-7/16f))
                .rotateCentered(Mth.PI, Direction.Axis.Y)
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .light(light)
                .renderInto(ms, solid);

    }

    @Override
    protected BlockState getRenderedBlockState(SmallEngineBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
