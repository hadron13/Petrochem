package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import net.minecraft.client.gui.GuiGraphics;

public class AnimatedPumpjackWell extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0);

        blockElement(GearboxBlocks.PUMPJACK_WELL.getDefaultState())
                .rotateBlock(22.5, 22.5+180, 0)
                .scale(23)
                .render(graphics);
        matrixStack.popPose();
    }
}
