package io.github.hadron13.gearbox.blocks.pumpjack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import io.github.hadron13.gearbox.register.GearboxPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import static io.github.hadron13.gearbox.blocks.pumpjack.PumpjackArmBlock.HORIZONTAL_FACING;

public class PumpjackCrankRenderer  extends KineticBlockEntityRenderer<PumpjackCrankBlockEntity> {
    public PumpjackCrankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }



    @Override
    protected void renderSafe(PumpjackCrankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());


        BlockState blockstate = be.getBlockState();
        Direction facing = blockstate.getValue(HORIZONTAL_FACING);
        SuperByteBuffer crank = CachedBuffers.partialFacing(GearboxPartialModels.PUMPJACK_CRANK, blockstate, facing);
        SuperByteBuffer half_shaft = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, blockstate, facing);

        standardKineticRotationTransform(half_shaft, be, light);

        half_shaft.renderInto(ms, solid);


        float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = be.angle + speed * partialTicks;

        crank
                .light(light)
                .translate(0, 3/16f, 0)
                .rotateCentered(angle * Mth.DEG_TO_RAD, facing.getClockWise())
                .translate(0, -3/16f, 0)
                .renderInto(ms, solid);


    }
}
