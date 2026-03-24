package io.github.hadron13.gearbox.compat.jei;

import io.github.hadron13.gearbox.Petrochem;
import net.createmod.catnip.gui.TextureSheetSegment;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ModGuiTextures implements ScreenElement, TextureSheetSegment {
    JEI_HEAT_BAR_CENTERED("jei/widgets", 0, 16, 169, 19),
    JEI_SHORT_ARROW("jei/widgets", 0, 0, 20, 9),
    JEI_BACK_ARROW("jei/widgets", 0, 48, 14, 18),
    JEI_DISTILLING_COLUMN("jei/widgets", 0, 80, 32, 32),
    JEI_DISTILLING_COLUMN_BOTTOM("jei/widgets", 0, 112, 32, 32),
    JEI_DISTILLING_FIRE("jei/widgets", 0, 144, 32, 16);

    public static final int FONT_COLOR = 0x575F7A;
    public final ResourceLocation location;
    private final int width;
    private final int height;
    private final int startX;
    private final int startY;

    ModGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    ModGuiTextures(String location, int startX, int startY, int width, int height) {
        this(Petrochem.MODID, location, startX, startY, width, height);
    }

    ModGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}