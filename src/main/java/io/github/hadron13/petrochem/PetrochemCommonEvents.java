package io.github.hadron13.petrochem;

import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputBlockEntity;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.petrochem.blocks.flarestack.FlarestackBlockEntity;
import io.github.hadron13.petrochem.blocks.medium_engine.MediumEngineBlockEntity;
import io.github.hadron13.petrochem.blocks.pumpjack.PumpjackWellBlockEntity;
import io.github.hadron13.petrochem.blocks.small_engine.SmallEngineBlockEntity;
import io.github.hadron13.petrochem.blocks.steam_output.SteamOutputBlockEntity;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelTankBlockEntity;
import io.github.hadron13.petrochem.blocks.turbine.TurbineBlockEntity;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber
public class PetrochemCommonEvents {
    @net.neoforged.bus.api.SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        DistillationControllerBlockEntity.registerCapabilities(event);
        DistillationOutputBlockEntity.registerCapabilities(event);
        ElectrolyzerBlockEntity.registerCapabilities(event);
        PumpjackWellBlockEntity.registerCapabilities(event);
        SteelTankBlockEntity.registerCapabilities(event);
        SmallEngineBlockEntity.registerCapabilities(event);
        FlarestackBlockEntity.registerCapabilities(event);
        MediumEngineBlockEntity.registerCapabilities(event);
        TurbineBlockEntity.registerCapabilities(event);
        SteamOutputBlockEntity.registerCapabilities(event);
    }
}
