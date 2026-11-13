package io.github.hadron13.petrochem.register;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.petrochem.Petrochem;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class PetrochemFluids {
    private static final CreateRegistrate REGISTRATE = Petrochem.registrate().setCreativeTab(PetrochemCreativeModeTabs.INGREDIENTS);

    public static final FluidEntry<BaseFlowingFluid.Flowing> PETROLEUM =
            REGISTRATE.standardFluid("petroleum",
                            SolidRenderedPlaceableFluidType.create(0x352228,
                                    () -> 1f / 32f ))
                    .lang("Petroleum")
                    .tag(PetrochemTags.FluidTags.CRUDE_OIL.tag)
                    .properties(b -> b.viscosity(20000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(BaseFlowingFluid.Source::new)
                    .block()
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW))
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

    public static final FluidEntry<BaseFlowingFluid.Flowing> SULFURIC_ACID =
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
                    .source(BaseFlowingFluid.Source::new)
                    .block()
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW))
                    .build()
                    .bucket()
                    .build()
                    .register();

    static{
        PetrochemCreativeModeTabs.expert_fluid_ids.add("sulfuric_acid");
        PetrochemCreativeModeTabs.expert_item_ids.add("sulfuric_acid_bucket");
    }

    public static final FluidEntry<BaseFlowingFluid.Flowing> NITROGEN = gas("Nitrogen", true);
    public static final FluidEntry<BaseFlowingFluid.Flowing> OXYGEN = gas("Oxygen", false);
    public static final FluidEntry<BaseFlowingFluid.Flowing> HYDROGEN = gas("Hydrogen", false);

    public static final FluidEntry<BaseFlowingFluid.Flowing> STEAM = gas("Steam", false);
    public static final FluidEntry<BaseFlowingFluid.Flowing> CHLORINE = gas("Chlorine", false);
    public static final FluidEntry<BaseFlowingFluid.Flowing> HYDROGEN_SULFIDE = gas("Hydrogen Sulfide", "hydrogen_sulfide", true);
    public static final FluidEntry<BaseFlowingFluid.Flowing> VOLATILE_GAS = gas("Volatile Gas", "volatile_gas", true);
    public static final FluidEntry<BaseFlowingFluid.Flowing> BUTANE = gas("Butane", true);
    public static final FluidEntry<BaseFlowingFluid.Flowing> PROPANE = gas("Propane", true);
    public static final FluidEntry<BaseFlowingFluid.Flowing> LPG = gas("Lpg", "lpg", false, PetrochemTags.FluidTags.LPG.tag);
    public static final FluidEntry<BaseFlowingFluid.Flowing> ETHYLENE = gas("Ethylene", true);


    public static final FluidEntry<BaseFlowingFluid.Flowing>
            OIL_BRINE    = oillike("oil_brine", "Petroleum Brine", 0x373e42, true),
            DESALTED_OIL = oillike("desalted_oil", "Desalted Petroleum", 0x482e37, false),
            OIL          = oillike("oil", "Oil", 0x11141d, false),
            LIGHT_NAPHTA = oillike("light_naphta", "Light Naphta", 0xd9d8a3, false),
            HEAVY_NAPHTA = oillike("heavy_naphta", "Heavy Naphta", 0xc4c26e, false),
            DESULFURIZED_HEAVY_NAPHTA = oillike("desulfurized_heavy_naphta", "Desulfurized Heavy Naphta", 0xcfc254, true),
            PLASTIC = oillike("plastic", "Liquid Polyethylene", 0xd8d8d5, false),
            HYDROCRACKED_GASOLINE = oillike("hydrocracked_gasoline", "Raw Gasoline", 0xa68d3f, true),
            UNTREATED_GASOLINE = oillike("untreated_gasoline", "Untreated Gasoline", 0xc49b21, true),
            GASOLINE = oillike("gasoline", "Refined Gasoline", 0xcfc254, false, PetrochemTags.FluidTags.GASOLINE.tag),
            KEROSENE = oillike("kerosene", "Kerosene", 0x26a69a, false, PetrochemTags.FluidTags.KEROSENE.tag),
            DESULFURIZED_KEROSENE = oillike("desulfurized_kerosene", "Desulfurized Kerosene", 0x26a69a,true),
            LIGHT_DIESEL = oillike("light_diesel", "Light Diesel", 0xb58c4f, true),
            HEAVY_DIESEL = oillike("heavy_diesel", "Heavy Diesel", 0x856638, true),
            REFINED_DIESEL = oillike("diesel", "Refined Diesel", 0xe57373, false, PetrochemTags.FluidTags.DIESEL.tag),
            LIGHT_GAS_OIL = oillike("light_gas_oil", "Light Gas Oil", 0x5e7a88, true),
            HEAVY_GAS_OIL = oillike("heavy_gas_oil", "Heavy Gas Oil", 0x2c393f, false),
            HYDROTREATED_GAS_OIL = oillike("hydrotreated_gas_oil", "Hydrotreated Gas Oil", 0x3c394f, true),
            DESULFURIZED_HEAVY_DIESEL = oillike("desulfurized_heavy_diesel", "Desulfurized Heavy Diesel", 0xb54f4f, true),
            OIL_RESIDUE = oillike("oil_residue", "Oil Residue", 0x311111, false),
            HEAVY_OIL_RESIDUE = oillike("heavy_oil_residue", "Heavy Oil Residue", 0x111111, false),
            FUEL_OIL =  oillike("fuel_oil", "Fuel Oil", 0x525252, false),
            ALKYLATE =  oillike("fuel_oil", "Fuel Oil", 0xb1a7c3, true),
            LUBRICANT = oillike("lubricant", "Lubricant", 0xffc107, false)
    ;

    public static FluidEntry<BaseFlowingFluid.Flowing> gas(String name, boolean expert) {
        return gas(name, name.toLowerCase(), expert);
    }

    public static FluidEntry<BaseFlowingFluid.Flowing> gas(String name, String id, boolean expert) {
        return gas(name, id, expert, null);
    }
    public static FluidEntry<BaseFlowingFluid.Flowing> gas(String name, String id, boolean expert, TagKey<Fluid> tag){
        if(expert) {
            PetrochemCreativeModeTabs.expert_fluid_ids.add(id);
            PetrochemCreativeModeTabs.expert_item_ids.add(id + "_bucket");
        }
        var builder = REGISTRATE
            .fluid(id, Petrochem.asResource("fluid/" + id + "_still"), Petrochem.asResource("fluid/" + id + "_flow"), TransparentFluidType::new)
            .lang(name);;;;

        if(tag != null)
            builder.tag(tag);

        return    builder
            .properties(p -> p.viscosity(0).density(-100))
            .fluidProperties(p -> p.levelDecreasePerBlock(7)
                    .tickRate(1)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .bucket()
            .build()
            .register();
    }
    public static FluidEntry<BaseFlowingFluid.Flowing> oillike(String name, String lang, int fogColor, boolean expert) {
        return oillike(name, lang, fogColor, expert, null);
    }

    public static FluidEntry<BaseFlowingFluid.Flowing> oillike(String name, String lang, int fogColor, boolean expert, TagKey<Fluid> tag){

        if(expert) {
            PetrochemCreativeModeTabs.expert_fluid_ids.add(name.toLowerCase());
            PetrochemCreativeModeTabs.expert_item_ids.add(name.toLowerCase() + "_bucket");
        }
        var builder = REGISTRATE.standardFluid(name,
                        SolidRenderedPlaceableFluidType.create(fogColor,
                                () -> 1f / 32f ))
                .lang(lang);

        if(tag != null)
            builder.tag(tag);

        return builder
                .properties(b -> b.viscosity(2000)
                        .density(1000))
                .fluidProperties(p -> p.levelDecreasePerBlock(3)
                        .tickRate(25)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .source(BaseFlowingFluid.Source::new)
                .block()
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW))
                .build()
                .bucket()
//                    .onRegister(AllFluids::registerFluidDispenseBehavior)
                .build()
                .register();
    }

    public static class TransparentFluidType extends AllFluids.TintedFluidType{
        protected TransparentFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return NO_TINT;
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
