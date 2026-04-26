package io.github.hadron13.petrochem.register;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.petrochem.Petrochem;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PetrochemFluids {
    private static final CreateRegistrate REGISTRATE = Petrochem.registrate().setCreativeTab(PetrochemCreativeTabs.INGREDIENTS);

    public static final FluidEntry<ForgeFlowingFluid.Flowing> PETROLEUM =
            REGISTRATE.standardFluid("petroleum",
                            SolidRenderedPlaceableFluidType.create(0x352228,
                                    () -> 1f / 32f ))
                    .lang("Petroleum")
                    .properties(b -> b.viscosity(20000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(ForgeFlowingFluid.Source::new)
                    .block()
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW))
                    .build()
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/petroleum")) //TODO: remove this
                    .build()
                    .register();


    public static final FluidEntry<ForgeFlowingFluid.Flowing> SULFURIC_ACID =
            REGISTRATE.standardFluid("sulfuric_acid",
                            SolidRenderedPlaceableFluidType.create(0xd66d842,
                                    () -> 1f / 32f ))
                    .lang("Sulfuric Acid")
                    .properties(b -> b.viscosity(500)
                            .density(1000)
                            .temperature(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(25)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))
                    .tag(FluidTags.LAVA)
                    .source(ForgeFlowingFluid.Source::new)
                    .block()
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW))
                    .build()
                    .bucket()
                    .build()
                    .register();


    public static final FluidEntry<VirtualFluid> AIR = REGISTRATE
            .virtualFluid("air")
            .properties(p -> p.viscosity(0).density(-100))
            .lang("Air")
            .bucket()
            .build()
            .register();


    public static final FluidEntry<ForgeFlowingFluid.Flowing> NITROGEN = gas("Nitrogen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> OXYGEN = gas("Oxygen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROGEN = gas("Hydrogen");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> STEAM = gas("Steam");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> CHLORINE = gas("Chlorine");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROGEN_SULFIDE = gas("hydrogen_sulfide");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> VOLATILE_GAS = gas("volatile_gas");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> BUTANE = gas("Butane");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> PROPANE = gas("Propane");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> LPG = gas("Lpg");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> ETHYLENE = gas("Ethylene");
    //    public static final FluidEntry<ForgeFlowingFluid.Flowing> ARGON = gas("argon");
//    public static final FluidEntry<ForgeFlowingFluid.Flowing> AMMONIA = gas("ammonia");
//    public static final FluidEntry<ForgeFlowingFluid.Flowing> DINITROGEN_TETROXIDE = gas("dinitrogen_tetroxide");


    public static final FluidEntry<ForgeFlowingFluid.Flowing>
        OIL_BRINE    = oillike("oil_brine", "Petroleum Brine", 0x373e42),
        DESALTED_OIL = oillike("desalted_oil", "Desalted Petroleum", 0x482e37),
        OIL          = oillike("oil", "Oil", 0x11141d),
        LIGHT_NAPHTA = oillike("light_naphta", "Light Naphta", 0xd9d8a3),
        HEAVY_NAPHTA = oillike("heavy_naphta", "Heavy Naphta", 0xc4c26e),
        DESULFURIZED_HEAVY_NAPHTA = oillike("desulfurized_heavy_naphta", "Desulfurized Heavy Naphta", 0xcfc254),
        PLASTIC = oillike("plastic", "Liquid Polyethylene", 0xd8d8d5),
        HYDROCRACKED_GASOLINE = oillike("hydrocracked_gasoline", "Raw Gasoline", 0xa68d3f),
        UNTREATED_GASOLINE = oillike("untreated_gasoline", "Untreated Gasoline", 0xc49b21),
        GASOLINE = oillike("gasoline", "Refined Gasoline", 0xcfc254),
        KEROSENE = oillike("kerosene", "Kerosene", 0x26a69a),
        DESULFURIZED_KEROSENE = oillike("desulfurized_kerosene", "Desulfurized Kerosene", 0x26a69a),
        LIGHT_DIESEL = oillike("light_diesel", "Light Diesel", 0xb58c4f),
        HEAVY_DIESEL = oillike("heavy_diesel", "Heavy Diesel", 0x856638),
        REFINED_DIESEL = oillike("diesel", "Refined Diesel", 0xe57373),
        LIGHT_GAS_OIL = oillike("light_gas_oil", "Light Gas Oil", 0x5e7a88),
        HEAVY_GAS_OIL = oillike("heavy_gas_oil", "Heavy Gas Oil", 0x2c393f),
        HYDROTREATED_GAS_OIL = oillike("hydrotreated_gas_oil", "Hydrotreated Gas Oil", 0x3c394f),
        DESULFURIZED_HEAVY_DIESEL = oillike("desulfurized_heavy_diesel", "Desulfurized Heavy Diesel", 0xb54f4f),
        OIL_RESIDUE = oillike("oil_residue", "Oil Residue", 0x311111),
        HEAVY_OIL_RESIDUE = oillike("heavy_oil_residue", "Heavy Oil Residue", 0x111111),
        FUEL_OIL =  oillike("fuel_oil", "Fuel Oil", 0x525252),
        ALKYLATE =  oillike("fuel_oil", "Fuel Oil", 0xb1a7c3),
        LUBRICANT = oillike("lubricant", "Lubricant", 0xffc107)
    ;

    public static FluidEntry<ForgeFlowingFluid.Flowing> gas(String name){
        return REGISTRATE
            .fluid(name.toLowerCase(), Petrochem.asResource("fluid/" + name.toLowerCase() + "_still"), Petrochem.asResource("fluid/" + name.toLowerCase() + "_flow"), TransparentFluidType::new)
            .properties(p -> p.viscosity(0).density(-100))
            .fluidProperties(p -> p.levelDecreasePerBlock(7)
                    .tickRate(1)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .build()
            .register();
    }


    public static FluidEntry<ForgeFlowingFluid.Flowing> oillike(String name, String lang, int fogColor){
        return REGISTRATE.standardFluid(name,
                            SolidRenderedPlaceableFluidType.create(0x352228,
                                    () -> 1f / 32f ))
                    .lang(lang)
                    .properties(b -> b.viscosity(2000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(3)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(ForgeFlowingFluid.Source::new)
                    .block()
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW))
                    .build()
                    .bucket()
//                    .onRegister(AllFluids::registerFluidDispenseBehavior)
                    .build()
                    .register();
    }
//
//    public static final ResourceLocation GAS_STILL = new ResourceLocation("minecraft", "block/water_still");
//    public static final ResourceLocation GAS_FLOW = new ResourceLocation("minecraft", "block/water_flow");
//    private static final ResourceLocation GAS_OVERLAY = new ResourceLocation("minecraft", "block/water_overlay");



    public static class TransparentFluidType extends FluidType {
        protected ResourceLocation stillTexture;
        protected ResourceLocation flowingTexture;

        protected TransparentFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties);
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                public ResourceLocation getStillTexture() {
                    return TransparentFluidType.this.stillTexture;
                }

                public ResourceLocation getFlowingTexture() {
                    return TransparentFluidType.this.flowingTexture;
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return 0x00ffffff;
                }
            });
        }
    }



    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }

    public static void register() {}
}
