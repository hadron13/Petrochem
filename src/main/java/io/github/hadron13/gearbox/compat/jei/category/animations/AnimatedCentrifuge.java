package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.PetrochemBlocks;
import io.github.hadron13.gearbox.register.PetrochemPartialModels;
import net.minecraft.client.gui.GuiGraphics;

public class AnimatedCentrifuge extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        int scale = 23;
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));

        blockElement(PetrochemBlocks.CENTRIFUGE.getDefaultState())
                .scale(scale)
                .render(graphics);

        blockElement(PetrochemPartialModels.CENTRIFUGE_COG)
                .rotateBlock(0, getCurrentAngle() * 2, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();

    }
}
