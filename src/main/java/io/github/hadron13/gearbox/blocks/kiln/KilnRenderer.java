package io.github.hadron13.gearbox.blocks.kiln;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class KilnRenderer extends SafeBlockEntityRenderer<KilnBlockEntity> {

    public KilnRenderer(BlockEntityRendererProvider.Context context) {}


    @Override
    protected void renderSafe(KilnBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        renderShaft(be, ms, bufferSource, light, overlay);
    }

    protected void renderShaft(KilnBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        KineticBlockEntityRenderer.renderRotatingBuffer(be, getRotatedModel(be), ms,
                buffer.getBuffer(RenderType.solid()), light);
    }

    protected SuperByteBuffer getRotatedModel(KineticBlockEntity be) {
        BlockState state = be.getBlockState();
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state.rotate(be.getLevel(), be.getBlockPos(), Rotation.NONE), state.getValue(HORIZONTAL_FACING).getOpposite());
    }


}