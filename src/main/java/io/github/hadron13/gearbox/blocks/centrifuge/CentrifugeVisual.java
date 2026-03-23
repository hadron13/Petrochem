package io.github.hadron13.gearbox.blocks.centrifuge;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import io.github.hadron13.gearbox.register.GearboxPartialModels;

public class CentrifugeVisual extends SingleAxisRotatingVisual<CentrifugeBlockEntity> {
    public CentrifugeVisual(VisualizationContext context, CentrifugeBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(GearboxPartialModels.CENTRIFUGE_COG));
    }
}
