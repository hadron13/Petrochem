package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import io.github.hadron13.gearbox.register.PetrochemPartialModels;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class DistillationControllerRenderer extends SafeBlockEntityRenderer<DistillationControllerBlockEntity> {


    public DistillationControllerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(DistillationControllerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState state = be.getBlockState();

        SuperByteBuffer headBuffer = CachedBuffers.partial(PetrochemPartialModels.DISTILLATION_SELECTOR, state);

        for (Direction facing : Iterate.directions) {
            if (!DistillationControllerBlock.shouldRenderHeadOnFace(be.getLevel(), be.getBlockPos(), state,
                    facing))
                continue;
            VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());
            rotateBufferTowards(headBuffer, facing).light(light)
                    .renderInto(ms, vb);
        }
    }


    protected SuperByteBuffer rotateBufferTowards(SuperByteBuffer buffer, Direction target) {
        return buffer.rotateCentered((float) ((-target.toYRot() - 90) / 180 * Math.PI), Direction.UP);
    }
}
