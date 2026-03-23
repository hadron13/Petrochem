package io.github.hadron13.gearbox.blocks.kiln;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.drill.DrillActorVisual;
import com.simibubi.create.content.kinetics.drill.DrillRenderer;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.InstancerProvider;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Consumer;

public class KilnVisual extends KineticBlockEntityVisual<KilnBlockEntity> {

    protected final RotatingInstance rotatingModel;

    public KilnVisual(VisualizationContext context, KilnBlockEntity blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks);
        rotatingModel = shaft(instancerProvider(), blockState)
                .setup(blockEntity)
                .setPosition(getVisualPosition());
        rotatingModel.setChanged();
    }

    public static RotatingInstance shaft(InstancerProvider instancerProvider, BlockState state) {
        var facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Direction align = facing.getOpposite();
        return instancerProvider.instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance()
                .rotateTo(0, 0, 1, align.getStepX(), align.getStepY(), align.getStepZ());
    }

    @Override
    public void update(float pt) {
        rotatingModel.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(rotatingModel);
    }

    @Override
    protected void _delete() {
        rotatingModel.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(rotatingModel);
    }
}