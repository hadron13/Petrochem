package io.github.hadron13.petrochem.register;


import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassPipeVisual;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugeVisual;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugeRenderer;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerRenderer;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputBlockEntity;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputRenderer;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerVisual;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerRenderer;
import io.github.hadron13.petrochem.blocks.flarestack.FlarestackBlockEntity;
import io.github.hadron13.petrochem.blocks.kiln.KilnBlockEntity;
import io.github.hadron13.petrochem.blocks.kiln.KilnRenderer;
import io.github.hadron13.petrochem.blocks.kiln.KilnVisual;
import io.github.hadron13.petrochem.blocks.pumpjack.*;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelFluidTankRenderer;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelTankBlockEntity;


public class PetrochemBlockEntities {

    private static final CreateRegistrate REGISTRATE = Petrochem.registrate();

    public static final BlockEntityEntry<KilnBlockEntity> KILN = REGISTRATE
            .blockEntity("kiln", KilnBlockEntity::new)
            .visual(() -> KilnVisual::new)
            .validBlocks(PetrochemBlocks.KILN)
            .renderer(() -> KilnRenderer::new)
            .register();

//    public static final BlockEntityEntry<BlackHoleBlockEntity> BLACK_HOLE = REGISTRATE
//            .blockEntity("black_hole", BlackHoleBlockEntity::new)
//            .validBlocks(ModBlocks.BLACK_HOLE)
//            .renderer(()-> BlackHoleRenderer::new)
//            .register();



    public static final BlockEntityEntry<ElectrolyzerBlockEntity> ELECTROLYZER = REGISTRATE
            .blockEntity("electrolyzer", ElectrolyzerBlockEntity::new)
            .visual(() -> ElectrolyzerVisual::new)
            .validBlocks(PetrochemBlocks.ELECTROLYZER)
            .renderer(() -> ElectrolyzerRenderer::new)
            .register();


    public static final BlockEntityEntry<CentrifugeBlockEntity> CENTRIFUGE = REGISTRATE
            .blockEntity("centrifuge", CentrifugeBlockEntity::new)
            .visual(() -> CentrifugeVisual::new)
            .validBlocks(PetrochemBlocks.CENTRIFUGE)
            .renderer(() -> CentrifugeRenderer::new)
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

//    public static final BlockEntityEntry<DipperBlockEntity> DIPPER = REGISTRATE
//            .blockEntity("dipper", DipperBlockEntity::new)
//            .validBlocks(GearboxBlocks.DIPPER)
//
//            .register();
//
//    public static final BlockEntityEntry<ReactorBlockEntity> REACTOR = REGISTRATE
//            .blockEntity("chemical_reactor", ReactorBlockEntity::new)
//            .visual(() -> ReactorVisual::new)
//            .validBlocks(GearboxBlocks.REACTOR)
//            .renderer(() -> ReactorRenderer::new)
//            .register();

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
            .renderer(() -> PumpRenderer::new)
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


    public static void register() {}
}