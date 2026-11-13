package io.github.hadron13.petrochem.register;


import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.*;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveVisual;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.petrochem.Petrochem;

import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerRenderer;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputRenderer;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerVisual;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerRenderer;

import io.github.hadron13.petrochem.blocks.flarestack.FlarestackBlockEntity;
import io.github.hadron13.petrochem.blocks.medium_engine.MediumEngineBlockEntity;
import io.github.hadron13.petrochem.blocks.medium_engine.MediumEngineRenderer;
import io.github.hadron13.petrochem.blocks.medium_engine.MediumEngineVisual;
import io.github.hadron13.petrochem.blocks.pumpjack.*;

import io.github.hadron13.petrochem.blocks.small_engine.SmallEngineBlockEntity;
import io.github.hadron13.petrochem.blocks.small_engine.SmallEngineRenderer;
import io.github.hadron13.petrochem.blocks.small_engine.SmallEngineVisual;
import io.github.hadron13.petrochem.blocks.steam_output.SteamOutputBlockEntity;
import io.github.hadron13.petrochem.blocks.steel_pump.SteelPumpRenderer;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelFluidTankRenderer;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelTankBlockEntity;
import io.github.hadron13.petrochem.blocks.turbine.TurbineBlockEntity;
import io.github.hadron13.petrochem.blocks.turbine.TurbineRenderer;
import io.github.hadron13.petrochem.blocks.turbine.TurbineVisual;


public class PetrochemBlockEntities {

    private static final CreateRegistrate REGISTRATE = Petrochem.registrate();


    public static final BlockEntityEntry<ElectrolyzerBlockEntity> ELECTROLYZER = REGISTRATE
            .blockEntity("electrolyzer", ElectrolyzerBlockEntity::new)
            .visual(() -> ElectrolyzerVisual::new)
            .validBlocks(PetrochemBlocks.ELECTROLYZER)
            .renderer(() -> ElectrolyzerRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpjackArmBlockEntity> PUMPJACK_ARM = REGISTRATE
            .blockEntity("pumpjack_arm", PumpjackArmBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(PetrochemBlocks.PUMPJACK_ARM)
            .renderer(() -> PumpjackArmRenderer::new)
            // TODO: instance
            .register();
    public static final BlockEntityEntry<PumpjackCrankBlockEntity> PUMPJACK_CRANK = REGISTRATE
            .blockEntity("pumpjack_crank", PumpjackCrankBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(PetrochemBlocks.PUMPJACK_CRANK)
            .renderer(() -> PumpjackCrankRenderer::new)
            // TODO: instance
            .register();

    public static final BlockEntityEntry<PumpjackWellBlockEntity> PUMPJACK_WELL = REGISTRATE
            .blockEntity("pumpjack_well", PumpjackWellBlockEntity::new)
            .validBlocks(PetrochemBlocks.PUMPJACK_WELL)
            .register();

    public static final BlockEntityEntry<SteelTankBlockEntity> STEEL_FLUID_TANK = REGISTRATE
            .blockEntity("steel_fluid_tank", SteelTankBlockEntity::new)
            .validBlocks(PetrochemBlocks.STEEL_FLUID_TANK)
            .renderer(() -> SteelFluidTankRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> STEEL_FLUID_PIPE = REGISTRATE
            .blockEntity("steel_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(PetrochemBlocks.STEEL_FLUID_PIPE)
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> STEEL_GLASS_FLUID_PIPE = REGISTRATE
            .blockEntity("steel_glass_fluid_pipe", StraightPipeBlockEntity::new)
            .visual(() -> GlassPipeVisual::new, false)
            .validBlocks(PetrochemBlocks.STEEL_GLASS_FLUID_PIPE)
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> STRAIGHT_STEEL_FLUID_PIPE = REGISTRATE
            .blockEntity("straight_steel_fluid_pipe", StraightPipeBlockEntity::new)
            .validBlocks(PetrochemBlocks.STRAIGHT_STEEL_FLUID_PIPE)
            .register();

    public static final BlockEntityEntry<PumpBlockEntity> STEEL_FLUID_PUMP = REGISTRATE
            .blockEntity("steel_fluid_pump", PumpBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.ofZ(PetrochemPartialModels.STEEL_PUMP_COG))
            .validBlocks(PetrochemBlocks.STEEL_PUMP)
            .renderer(() -> SteelPumpRenderer::new)
            .register();

    public static final BlockEntityEntry<DistillationControllerBlockEntity> DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distillation_controller", DistillationControllerBlockEntity::new)
            .validBlocks(PetrochemBlocks.DISTILLATION_CONTROLLER)
            .renderer(() -> DistillationControllerRenderer::new)
            .register();

    public static final BlockEntityEntry<DistillationOutputBlockEntity> DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distillation_output", DistillationOutputBlockEntity::new)
            .validBlocks(PetrochemBlocks.DISTILLATION_OUTPUT)
            .renderer(() -> DistillationOutputRenderer::new)
            .register();

    public static final BlockEntityEntry<FlarestackBlockEntity> FLARESTACK = REGISTRATE
            .blockEntity("flarestack", FlarestackBlockEntity::new)
            .validBlocks(PetrochemBlocks.FLARESTACK)
            .register();

    public static final BlockEntityEntry<SmallEngineBlockEntity> SMALL_ENGINE = REGISTRATE
            .blockEntity("small_engine", SmallEngineBlockEntity::new)
            .visual(() -> SmallEngineVisual::new)
            .validBlocks(PetrochemBlocks.SMALL_ENGINE)
            .renderer(() -> SmallEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<MediumEngineBlockEntity> MEDIUM_ENGINE = REGISTRATE
            .blockEntity("medium_engine", MediumEngineBlockEntity::new)
            .visual(() -> MediumEngineVisual::new)
            .validBlocks(PetrochemBlocks.MEDIUM_ENGINE)
            .renderer(() -> MediumEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidValveBlockEntity> STEEL_FLUID_VALVE = REGISTRATE
            .blockEntity("steel_fluid_valve", FluidValveBlockEntity::new)
            .visual(() -> FluidValveVisual::new)
            .validBlocks(PetrochemBlocks.STEEL_FLUID_VALVE)
            .renderer(() -> FluidValveRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartFluidPipeBlockEntity> STEEL_SMART_FLUID_PIPE = REGISTRATE
            .blockEntity("steel_smart_fluid_pipe", SmartFluidPipeBlockEntity::new)
            .validBlocks(PetrochemBlocks.STEEL_SMART_FLUID_PIPE)
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<TurbineBlockEntity> TURBINE = REGISTRATE
            .blockEntity("turbine", TurbineBlockEntity::new)
            .visual(() -> TurbineVisual::new)
            .validBlocks(PetrochemBlocks.TURBINE)
            .renderer(() -> TurbineRenderer::new)
            .register();


    public static final BlockEntityEntry<SteamOutputBlockEntity> STEAM_OUTPUT = REGISTRATE
            .blockEntity("steam_output", SteamOutputBlockEntity::new)
            .validBlocks(PetrochemBlocks.STEAM_OUTPUT)
            .register();


    public static void register() {}
}