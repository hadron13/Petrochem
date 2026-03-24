package io.github.hadron13.petrochem.blocks.centrifuge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import io.github.hadron13.petrochem.register.PetrochemPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class CentrifugeRenderer extends KineticBlockEntityRenderer<CentrifugeBlockEntity> {
    public CentrifugeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public void renderSafe(CentrifugeBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay){
        if(VisualizationManager.supportsVisualization(be.getLevel()))
            return;


        Direction.Axis axis = getRotationAxisOf(be);
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        renderRotatingBuffer(be,
                CachedBuffers.partialFacingVertical(PetrochemPartialModels.CENTRIFUGE_COG, be.getBlockState(), facing),
                ms, buffer.getBuffer(RenderType.solid()), light);
    }
}
