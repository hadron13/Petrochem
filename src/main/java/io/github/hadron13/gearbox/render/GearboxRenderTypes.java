package io.github.hadron13.gearbox.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

public class GearboxRenderTypes {


    // Accessor functon, ensures that you don't use the raw methods below unintentionally.
    public static RenderType laserBeam() {
        return CustomRenderTypes.laserBeam();
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Gearbox.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModClientEvents {
        @SubscribeEvent
        public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
            // Adds a shader to the list, the callback runs when loading is complete.
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation("gearbox:rendertype_laser_beam"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
                CustomRenderTypes.laserBeamShader = shaderInstance;
            });
        }
    }

    // "Keep private because this stuff isn't meant to be public" ~someone
    // Laughs in I know what I'm doing
    public static class CustomRenderTypes extends RenderType {
        // Holds the object loaded via RegisterShadersEvent
        public static ShaderInstance laserBeamShader;

        // Shader state for use in the render type, the supplier ensures it updates automatically with resource reloads
        public static final ShaderStateShard RENDERTYPE_LASER_BEAM_SHADER = new ShaderStateShard(() -> laserBeamShader);

        // Dummy constructor needed to make java happy
        private CustomRenderTypes(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2) {
            super(s, v, m, i, b, b2, r, r2);
            throw new IllegalStateException("This class is not meant to be constructed!");
        }


        // Defines the RenderType. Make sure the name is unique by including your MODID in the name.
        private static RenderType laserBeam() {

            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LASER_BEAM_SHADER)
                    .setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(NO_OVERLAY)
                    .setOutputState(TRANSLUCENT_TARGET)
                    .createCompositeState(true);
            return create("gearbox_laser_beam", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2 << 16, true, true, rendertype$state);
        }
    }

}
