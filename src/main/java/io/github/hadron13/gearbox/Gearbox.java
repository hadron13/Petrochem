package io.github.hadron13.gearbox;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.hadron13.gearbox.config.GearboxConfig;
import io.github.hadron13.gearbox.data.GearboxDatagen;
import io.github.hadron13.gearbox.ponder.GearboxPonderPlugin;
import io.github.hadron13.gearbox.register.*;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gearbox.MODID)
public class Gearbox {

    public static final String MODID = "gearbox";
    public static boolean oculusLoaded = false;
    public static boolean adlodsLoaded = false;

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus modEventBus;
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory((item) -> (new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public Gearbox() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);

        GearboxCreativeTabs.register(modEventBus);
        GearboxBlocks.register();
        GearboxItems.register();
        GearboxBlockEntities.register();
        GearboxEntities.register();
        GearboxFluids.register();
        GearboxPartialModels.init();
        GearboxRecipeTypes.register(modEventBus);


        GearboxConfig.register(modLoadingContext);

        modEventBus.addListener(EventPriority.LOWEST, GearboxDatagen::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(Gearbox::clientInit) );

        MinecraftForge.EVENT_BUS.register(this);

        oculusLoaded = ModList.get().isLoaded("oculus");
        adlodsLoaded = ModList.get().isLoaded("adlods");
    }

    public static void clientInit(final FMLClientSetupEvent event){

        PonderIndex.addPlugin(new GearboxPonderPlugin());

    }

    public static CreateRegistrate registrate(){
        return REGISTRATE;
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
