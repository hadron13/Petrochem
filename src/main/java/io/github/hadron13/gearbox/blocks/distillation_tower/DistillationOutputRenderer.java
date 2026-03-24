package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.hadron13.gearbox.register.PetrochemPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputBlock.POWERED;
import static io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputBlock.TANK_FACE;

public class DistillationOutputRenderer extends SafeBlockEntityRenderer<DistillationOutputBlockEntity> {
    public DistillationOutputRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(DistillationOutputBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState state = be.getBlockState();
        PartialModel model = state.getValue(POWERED)?   PetrochemPartialModels.DISTILLATION_OUTPUT_BASE_POWERED:
                                                        PetrochemPartialModels.DISTILLATION_OUTPUT_BASE_UNPOWERED;
        SuperByteBuffer baseBuffer = CachedBuffers.partialFacing(model, state, state.getValue(TANK_FACE));

        baseBuffer.light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
    }
}
