package io.github.hadron13.petrochem.blocks.small_engine;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.instance.PosedInstance;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.engine_room.flywheel.lib.util.RecyclingPoseStack;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.petrochem.register.PetrochemPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SmallEngineVisual extends ShaftVisual<SmallEngineBlockEntity> implements SimpleDynamicVisual {

    public final RecyclingPoseStack poseStack = new RecyclingPoseStack();
    public TransformedInstance []pistons;

    public SmallEngineVisual(VisualizationContext context, SmallEngineBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        pistons = new TransformedInstance[4];
        for(int i = 0; i < 4; i++){
            pistons[i] = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(PetrochemPartialModels.SMALL_PISTON))
                            .createInstance();
        }


        TransformStack.of(poseStack)
            .translate(getVisualPosition())
//                .center()
        ;
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        for(TransformedInstance instance : pistons)
            consumer.accept(instance);
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        for(TransformedInstance instance : pistons)
            relight(instance);
    }

    @Override
    protected void _delete() {
        super._delete();
        for(TransformedInstance instance : pistons)
            instance.delete();
    }
    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING);

        Vec3 front = new Vec3(facing.step());
        Vec3 up = new Vec3(Direction.UP.step());
        Vec3 right = front.cross(up).scale(-1);

        float t = AnimationTickHolder.getRenderTime()/20f * blockEntity.getSpeed()/60f * Mth.TWO_PI;

        poseStack.pushPose();
//        TransformStack.of(poseStack).rotateYDegrees(facing.toYRot());
//        TransformStack.of(poseStack)


        poseStack.pushPose();
        TransformStack.of(poseStack)
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .center()
                .rotateToFace(facing)
                .uncenter();
        pistons[0].setTransform(poseStack).setChanged();
        poseStack.popPose();

        t += Mth.HALF_PI;

        poseStack.pushPose();
        TransformStack.of(poseStack)
                .translate(front.scale(-7/16f))
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .center()
                .rotateToFace(facing)
                .uncenter();
        pistons[1].setTransform(poseStack).setChanged();
        poseStack.popPose();

        t += Mth.HALF_PI;

        poseStack.pushPose();
        TransformStack.of(poseStack)
                .translate(front.scale(7/16f))
                .rotateCentered(Mth.PI, Direction.Axis.Y)
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .center()
                .rotateToFace(facing)
                .uncenter();
        pistons[2].setTransform(poseStack).setChanged();
        poseStack.popPose();

        t += Mth.HALF_PI;

        poseStack.pushPose();
        TransformStack.of(poseStack)
                .rotateCentered(Mth.PI, Direction.Axis.Y)
                .translate(right.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .translate(up.scale(Mth.sin(t) * 1/16f + 1/16f).scale(0.707))
                .center()
                .rotateToFace(facing)
                .uncenter();
        pistons[3].setTransform(poseStack).setChanged();
        poseStack.popPose();

        poseStack.popPose();
    }
}
