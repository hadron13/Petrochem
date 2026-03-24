package io.github.hadron13.petrochem.blocks.electrolyzer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import io.github.hadron13.petrochem.register.PetrochemPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrolyzerRenderer extends SafeBlockEntityRenderer<ElectrolyzerBlockEntity> {
    public ElectrolyzerRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    protected void renderSafe(ElectrolyzerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if(VisualizationManager.supportsVisualization(be.getLevel()))
            return;


        BlockState blockState = be.getBlockState();

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());


        float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
        float speed = be.getRenderedHeadRotationSpeed(partialTicks);
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI * 0.5f;

        SuperByteBuffer poleRender = CachedBuffers.partial(AllPartialModels.MECHANICAL_MIXER_POLE, blockState);
        poleRender.translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        VertexConsumer vbCutout = bufferSource.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer headRender = CachedBuffers.partial(PetrochemPartialModels.ELECTROLYZER_HEAD, blockState);
        headRender.rotateCentered(angle, Direction.UP)
                .translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vbCutout);
    }
}
