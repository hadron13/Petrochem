package io.github.hadron13.petrochem.blocks.small_engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
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
import org.joml.Vector3f;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SmallEngineRenderer extends KineticBlockEntityRenderer<SmallEngineBlockEntity> {
    public SmallEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }



    @Override
    protected void renderSafe(SmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);
        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        Direction facing = be.getBlockState().getValue(HORIZONTAL_FACING);
        SuperByteBuffer piston = CachedBuffers.partialFacing(PetrochemPartialModels.SMALL_PISTON, be.getBlockState(), facing);

        Vec3 front = new Vec3(facing.step());
        Vec3 up = new Vec3(Direction.UP.step());
        Vec3 right = front.cross(up);
        Vec3 left = right.scale(-1);

        float t1 = AnimationTickHolder.getRenderTime();
        float t2 = t1 + Mth.HALF_PI;

        piston.translate(right.scale(Mth.sin(t1) * 1/16f + 1/16f))
                .translate(up.scale(Mth.sin(t1) * 1/16f + 1/16f))
                .light(light)
                .renderInto(ms, solid);

        piston.translate(front.scale(7/16f))
                .translate(right.scale(Mth.sin(t2) * 1/16f + 1/16f))
                .translate(up.scale(Mth.sin(t2) * 1/16f + 1/16f))
                .light(light)
                .renderInto(ms, solid);

        piston.rotateCentered(Mth.PI, Direction.Axis.Y)
                .translate(right.scale(Mth.sin(t1) * 1/16f + 1/16f))
                .translate(up.scale(Mth.sin(t1) * 1/16f + 1/16f))
                .light(light)
                .renderInto(ms, solid);

        piston.translate(front.scale(-7/16f))
                .rotateCentered(Mth.PI, Direction.Axis.Y)
                .translate(right.scale(Mth.sin(t2) * 1/16f + 1/16f))
                .translate(up.scale(Mth.sin(t2) * 1/16f + 1/16f))
                .light(light)
                .renderInto(ms, solid);

    }

    @Override
    protected BlockState getRenderedBlockState(SmallEngineBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
