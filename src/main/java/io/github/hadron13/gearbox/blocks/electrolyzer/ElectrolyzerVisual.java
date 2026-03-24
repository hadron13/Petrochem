package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.PetrochemPartialModels;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class ElectrolyzerVisual extends KineticBlockEntityVisual<ElectrolyzerBlockEntity> implements SimpleDynamicVisual {

    public final OrientedInstance pole;
    public final RotatingInstance head;
    public final ElectrolyzerBlockEntity electrolyzer;

    public ElectrolyzerVisual(VisualizationContext context, ElectrolyzerBlockEntity blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks);
        this.electrolyzer = blockEntity;

        head = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(PetrochemPartialModels.ELECTROLYZER_HEAD))
                .createInstance();

        head.setRotationAxis(Direction.Axis.Y);

        pole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(AllPartialModels.MECHANICAL_MIXER_POLE))
                .createInstance();

        animate(partialTicks);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float pt) {
        float renderedHeadOffset = electrolyzer.getRenderedHeadOffset(pt);

        transformPole(renderedHeadOffset);
        transformHead(renderedHeadOffset, pt);
    }

    private void transformHead(float renderedHeadOffset, float pt) {
        float speed = electrolyzer.getRenderedHeadRotationSpeed(pt);

        head.setPosition(getVisualPosition())
                .nudge(0, -renderedHeadOffset, 0)
                .setRotationalSpeed(speed * RotatingInstance.SPEED_MULTIPLIER)
                .setChanged();
    }

    private void transformPole(float renderedHeadOffset) {
        pole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(pos.below(), head);
        relight(pole);
    }

    @Override
    protected void _delete() {
        head.delete();
        pole.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(head);
        consumer.accept(pole);
    }
}
