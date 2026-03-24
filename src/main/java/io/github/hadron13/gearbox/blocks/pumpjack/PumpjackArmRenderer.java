package io.github.hadron13.gearbox.blocks.pumpjack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.register.PetrochemPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;

import static io.github.hadron13.gearbox.blocks.pumpjack.PumpjackArmBlock.HORIZONTAL_FACING;

public class PumpjackArmRenderer extends SafeBlockEntityRenderer<PumpjackArmBlockEntity> {
    public PumpjackArmRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public int getViewDistance(){
        return 128;
    }



    @Override
    protected void renderSafe(PumpjackArmBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());



        BlockState blockstate = be.getBlockState();
        Direction facing = blockstate.getValue(HORIZONTAL_FACING);

        SuperByteBuffer head = CachedBuffers.partialFacing(PetrochemPartialModels.PUMPJACK_HEAD, blockstate, facing);
        SuperByteBuffer body = CachedBuffers.partialFacing(PetrochemPartialModels.PUMPJACK_ARM, blockstate, facing);
        SuperByteBuffer tail = CachedBuffers.partialFacing(PetrochemPartialModels.PUMPJACK_CONNECTOR, blockstate, facing);
        SuperByteBuffer pitman = CachedBuffers.partialFacing(PetrochemPartialModels.PUMPJACK_PITMAN, blockstate, facing);
        SuperByteBuffer smooth_rod = CachedBuffers.partialFacing(PetrochemPartialModels.PUMPJACK_SMOOTHROD, blockstate, facing);



        ms.pushPose();
        //TransformStack.cast(ms).rotateCentered(facing.getClockWise(), (float) Math.sin(AnimationTickHolder.getRenderTime()/10f)/4f);

        float crank_angle = 0;
        if(be.crank != null){
            float speed = be.crank.visualSpeed.getValue(partialTicks) * 6 / 20f;
            float angle = be.crank.angle + speed * partialTicks;
            crank_angle = angle * Mth.DEG_TO_RAD;
        }


        TransformStack.of(ms).translate(Vec3i.ZERO.relative(facing, 2).below(2));


        Vec2 pitman_pivot = new Vec2(Mth.sin(-crank_angle) * 8f/16f, Mth.cos(-crank_angle) * 8/16f + 11/16f);
        Vec2 beam_pivot = new Vec2(-32/16f, 40/16f);
        float beam_radius = 32/16f;
        float pitman_radius = 28/16f;

        float[] intersections = new float[4];
        findCircleIntersection(pitman_pivot, pitman_radius, beam_pivot, beam_radius, intersections);

        float pitman_angle = (float) Mth.atan2(pitman_pivot.y - intersections[1], pitman_pivot.x - intersections[0]) + Mth.HALF_PI;
        float beam_angle   = (float) Mth.atan2(intersections[1] - beam_pivot.y, intersections[0] - beam_pivot.x);

        float x_coef = 0f, z_coef = 0f;
        switch (facing){
            case SOUTH -> {x_coef = 0; z_coef = 1f;}
            case NORTH -> {x_coef = 0; z_coef = -1f;}
            case EAST  -> {x_coef = 1f; z_coef = 0f;}
            case WEST  -> {x_coef = -1f; z_coef = 0f;}
        }

        pitman
                .light(light)
                .translate(pitman_pivot.x * x_coef, pitman_pivot.y, pitman_pivot.x * z_coef)
                .translate(0, -8/16f, 0)
                .rotateCentered(pitman_angle, facing.getClockWise())
                .translate(0, 8/16f, 0)
                .renderInto(ms,solid);


        TransformStack.of(ms)
                .translate(intersections[0] * x_coef, intersections[1], intersections[0] * z_coef)
                .translate(0, -8/16f, 0)
                .rotateCentered(beam_angle, facing.getClockWise())
        ;

        tail.light(light).renderInto(ms, solid);
        body.light(light).translate(Vec3i.ZERO.relative(facing, -2)).renderInto(ms,solid);
        head.light(light).translate(Vec3i.ZERO.relative(facing, -4)).renderInto(ms,solid);

        ms.popPose();

        if(be.well == null)
            return;

        float head_height = (float) Math.sin(-beam_angle) * 2f;

        TransformStack.of(ms).translate(Vec3i.ZERO.relative(facing, -2).below());
        smooth_rod.light(light).translate(0, head_height - 1, 0).scale(1f, 2f, 1f).renderInto(ms, solid);

    }
    public static boolean findCircleIntersection(Vec2 c1, float r1,
                                                 Vec2 c2, float r2,
                                                 float[] result) {
        // Calculate the distance between the centers
        float dx = c2.x - c1.x;
        float dy = c2.y - c1.y;
        float d = (float) Math.sqrt(dx * dx + dy * dy);

        // No intersection if the circles are too far apart or one is completely inside the other
        if (d > r1 + r2 || d < Math.abs(r1 - r2)) {
            return false; // No intersection
        }

        // Check if the circles are tangent
        if (d == 0 && r1 == r2) {
            return false; // Infinite intersections (coincident circles)
        }

        // Calculate the distance from the center of circle 1 to the line joining the intersection points
        float a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
        float h = (float) Math.sqrt(r1 * r1 - a * a);

        // Midpoint between the intersection points
        float xm = c1.x + a * dx / d;
        float ym = c1.y + a * dy / d;

        // Calculate the intersection points
        result[0] = xm + h * dy / d; // x1
        result[1] = ym - h * dx / d; // y1
        result[2] = xm - h * dy / d; // x2
        result[3] = ym + h * dx / d; // y2

        return true; // Intersection exists
    }
}
