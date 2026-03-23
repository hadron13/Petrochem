package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.GearboxPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.Direction;

public class ReactorVisual extends SingleAxisRotatingVisual<ReactorBlockEntity> implements SimpleDynamicVisual {

    public final OrientedInstance pole;
    public final RotatingInstance head;

    public ReactorVisual(VisualizationContext context, ReactorBlockEntity blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks, Models.partial(AllPartialModels.SHAFT));

        head = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.MECHANICAL_MIXER_HEAD)).createInstance();

        head.setRotationAxis(Direction.Axis.Y);

        pole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(GearboxPartialModels.DIPPER_POLE)).createInstance();


        transformInstances(partialTicks);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        transformInstances(ctx.partialTick());
    }

    public void transformInstances(float pt){
        float renderedHeadOffset = 1.1f;

        pole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0).setChanged();

        float speed = blockEntity.getRenderedHeadRotationSpeed(AnimationTickHolder.getPartialTicks());

        head.setPosition(getVisualPosition())
                .nudge(0, -renderedHeadOffset, 0)
                .setRotationalSpeed(speed * 2).setChanged();


    }

    @Override
    public void updateLight(float partialTicks){
        super.updateLight(partialTicks);
        relight(pos, pole);
        relight(pos.below(), head);
    }

    @Override
    public void _delete() {
        super._delete();
        pole.delete();
        head.delete();
    }
}
