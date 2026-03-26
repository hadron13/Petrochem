package io.github.hadron13.petrochem.blocks.small_engine;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SmallEngineVisual extends KineticBlockEntityVisual<SmallEngineBlockEntity> {
    public SmallEngineVisual(VisualizationContext context, SmallEngineBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }

    @Override
    public void updateLight(float partialTick) {

    }

    @Override
    protected void _delete() {

    }
}
