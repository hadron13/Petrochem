package io.github.hadron13.petrochem.register;

import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.tank.*;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.hadron13.petrochem.Petrochem;
import io.github.hadron13.petrochem.blocks.centrifuge.CentrifugeBlock;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerBlock;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationControllerGenerator;
import io.github.hadron13.petrochem.blocks.distillation_tower.DistillationOutputBlock;
import io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerBlock;
import io.github.hadron13.petrochem.blocks.flarestack.FlarestackBlock;
import io.github.hadron13.petrochem.blocks.kiln.KilnBlock;
import io.github.hadron13.petrochem.blocks.steel_pipe.SteelGlassPipeBlock;
import io.github.hadron13.petrochem.blocks.steel_pipe.SteelPipeAttachmentModel;
import io.github.hadron13.petrochem.blocks.steel_pipe.SteelPipeBlock;
import io.github.hadron13.petrochem.blocks.steel_pipe.StraightSteelPipeBlock;
import io.github.hadron13.petrochem.blocks.steel_pump.SteelPumpBlock;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelFluidTankModel;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelTankBlock;
import io.github.hadron13.petrochem.blocks.steel_tank.SteelTankItem;
import io.github.hadron13.petrochem.data.client.blockstates.*;
import io.github.hadron13.petrochem.blocks.pumpjack.*;
import io.github.hadron13.petrochem.config.PetrochemStress;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class PetrochemBlocks {


    private static final CreateRegistrate REGISTRATE = Petrochem.registrate().setCreativeTab(PetrochemCreativeTabs.MAIN_TAB);

    public static void register() {}

//    public static final BlockEntry<ExchangerBlock> EXCHANGER = REGISTRATE.block("exchanger", ExchangerBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .properties(p -> p.mapColor(MapColor.METAL))
//            .transform(pickaxeOnly())
//            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
//            .transform(BlockStressDefaults.setImpact(2.0))
//            .item()
//            .transform(customItemModel())
//            .register();


    public static final BlockEntry<KilnBlock> KILN = REGISTRATE.block("kiln", KilnBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p  .mapColor(MapColor.METAL)
                                .lightLevel(s -> s.getValue(KilnBlock.POWERED) ? 15 : 0))
            .transform(pickaxeOnly())
            .blockstate(new KilnGenerator()::generate)
            .transform(PetrochemStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();


//    public static final BlockEntry<BlackHoleBlock> BLACK_HOLE  = REGISTRATE.block("black_hole", BlackHoleBlock::new)
//            .initialProperties(SharedProperties::netheriteMetal)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).noCollission())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
//            .item()
//            .transform(customItemModel())
//            .register();

    public static final BlockEntry<ElectrolyzerBlock> ELECTROLYZER = REGISTRATE.block("electrolyzer", ElectrolyzerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();


    public static final BlockEntry<CentrifugeBlock> CENTRIFUGE = REGISTRATE.block("centrifuge", CentrifugeBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(new PartialAxisBlockStateGen()::generate)
            .transform(PetrochemStress.setImpact(8.0))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<PumpjackArmBlock> PUMPJACK_ARM = REGISTRATE.block("pumpjack_arm", PumpjackArmBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL).noOcclusion())
            .blockstate(PumpjackGenerator.arm()::generate)
            .item(PumpjackArmBlockItem::new)
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), Petrochem.asResource("block/pumpjack/arm_item")))
            .build()
            .register();
    public static final BlockEntry<PumpjackCrankBlock> PUMPJACK_CRANK = REGISTRATE.block("pumpjack_crank", PumpjackCrankBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL).noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(PumpjackGenerator.crank()::generate)
            .transform(PetrochemStress.setImpact(32.0))
            .item()
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), Petrochem.asResource("block/pumpjack/crank_item")))
            .build()
            .register();


    public static final BlockEntry<PumpjackWellBlock> PUMPJACK_WELL = REGISTRATE.block("pumpjack_well", PumpjackWellBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(PumpjackGenerator.well()::generate)
            .item()
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), Petrochem.asResource("block/pumpjack/well")))
            .build()
            .register();


//    public static final BlockEntry<DipperBlock> DIPPER = REGISTRATE.block("dipper", DipperBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
//            //.addLayer(() -> RenderType::cutoutMipped)
//            .item(AssemblyOperatorBlockItem::new)
//            .transform(customItemModel())
//            .register();
//
//
//    public static final BlockEntry<ReactorBlock> REACTOR = REGISTRATE.block("chemical_reactor", ReactorBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
//            //.addLayer(() -> RenderType::cutoutMipped)
//            .transform(GearboxStress.setImpact(4.0))
//            .item(AssemblyOperatorBlockItem::new)
//            .transform(customItemModel())
//            .register();


//    public static final BlockEntry<DistillationTankBlock> DISTILLATION_TANK = REGISTRATE.block("distillation_tank", DistillationTankBlock::new)
//            .initialProperties(SharedProperties::copperMetal)
//            .properties(p -> p.noOcclusion()
//                    .isRedstoneConductor((p1, p2, p3) -> true))
//            .transform(pickaxeOnly())
//            .blockstate(new FluidTankGenerator()::generate)
//            .onRegister(CreateRegistrate.blockModel(() -> FluidTankModel::standard))
//            .item(FluidTankItem::new)
//            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
//            .build()
//            .register();

    public static final BlockEntry<SteelTankBlock> STEEL_FLUID_TANK = REGISTRATE.block("steel_fluid_tank", SteelTankBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion().sound(SoundType.METAL))
                    .transform(pickaxeOnly())
                    .blockstate(new FluidTankGenerator()::generate)
//                    .transform(mountedFluidStorage(CreateMountedStorageTypes.FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::new))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(SteelTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();


    public static final BlockEntry<SteelPipeBlock> STEEL_FLUID_PIPE = REGISTRATE.block("steel_fluid_pipe", SteelPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.forceSolidOff().sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<SteelGlassPipeBlock> STEEL_GLASS_FLUID_PIPE =
            REGISTRATE.block("glass_fluid_pipe", SteelGlassPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.forceSolidOff().sound(SoundType.METAL))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/steel_fluid_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::withAO))
                    .loot((p, b) -> p.dropOther(b, STEEL_FLUID_PIPE.get()))
                    .register();


    public static final BlockEntry<StraightSteelPipeBlock> STRAIGHT_STEEL_FLUID_PIPE = REGISTRATE.block("straight_steel_fluid_pipe", StraightSteelPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.forceSolidOff().sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/steel_fluid_pipe/straight")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, STEEL_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<DistillationControllerBlock> DISTILLATION_CONTROLLER = REGISTRATE.block("distillation_controller", DistillationControllerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(axeOrPickaxe())
            .properties(p -> p.mapColor(MapColor.METAL))
            .blockstate(new DistillationControllerGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteelPumpBlock> STEEL_PUMP = REGISTRATE.block("steel_pump", SteelPumpBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PetrochemStress.setImpact(3.0))
            .item()
            .transform(customItemModel())
            .register();



    public static final BlockEntry<DistillationOutputBlock> DISTILLATION_OUTPUT = REGISTRATE.block("distillation_output", DistillationOutputBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<Block> ASPHALT_BLOCK =  REGISTRATE.block("asphalt", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).speedFactor(1.5f))
                    .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
                    .transform(pickaxeOnly())
                    .lang("Asphalt Block")
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<FlarestackBlock> FLARESTACK = REGISTRATE.block("flarestack", FlarestackBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL))
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .lang("Flarestack")
            .item()
            .build()
            .register();


}