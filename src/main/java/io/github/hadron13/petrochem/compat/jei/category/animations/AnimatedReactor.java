package io.github.hadron13.petrochem.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.petrochem.register.PetrochemPartialModels;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public class AnimatedReactor extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, 200);
        poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(shaft(Direction.Axis.Y))
                .rotateBlock(0, getCurrentAngle(), 0)
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(graphics);
//
//        blockElement(GearboxBlocks.REACTOR.getDefaultState())
//                .rotateBlock(0, 90f, 0)
//                .atLocal(0, 0, 0)
//                .scale(scale)
//                .render(graphics);

        blockElement(PetrochemPartialModels.DIPPER_POLE)
                .atLocal(0, 17/16f, 0)
                .scale(scale)
                .render(graphics);

        blockElement(AllPartialModels.MECHANICAL_MIXER_HEAD)
                .rotateBlock(0, getCurrentAngle() / 4, 0)
                .atLocal(0, 17/16f, 0)
                .scale(scale)
                .render(graphics);

        blockElement(AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(graphics);

        poseStack.popPose();

    }
}
