package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.gearbox.Petrochem;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
    private static final CreateRegistrate REGISTRATE = Petrochem.registrate().setCreativeTab(PetrochemCreativeTabs.MAIN_TAB);

//    public static final FluidEntry<ForgeFlowingFluid.Flowing> PETROLEUM = REGISTRATE
//            .fluid("petroleum",
//                    Gearbox.asResource("fluid/petroleum_still"),
//                    Gearbox.asResource("fluid/petroleum_flow"))
//            .lang("Petroleum")
//            .properties(p -> p.viscosity(2000).density(1500))
//            .fluidProperties(p -> p.levelDecreasePerBlock(3)
//                            .tickRate(25)
//                            .slopeFindDistance(3)
//                            .explosionResistance(100f))
//            .source(ForgeFlowingFluid.Source::new)
//            .bucket()
//            .tag(AllTags.forgeItemTag("buckets/petroleum"))
//            .build().register();

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
//                    .onRegister(AllFluids::registerFluidDispenseBehavior)
                    .tag(AllTags.forgeItemTag("buckets/petroleum")) //TODO: remove this
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> RESIN = REGISTRATE
            .fluid("resin",
                    Petrochem.asResource("fluid/resin_still"),
                    Petrochem.asResource("fluid/resin_flow"))
            .lang("Resin")
            .properties(p -> p.density(1500).viscosity(2000))
            .fluidProperties(p -> p.levelDecreasePerBlock(3)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(AllTags.forgeItemTag("buckets/resin"))
            .build().register();


    public static final FluidEntry<VirtualFluid> AIR = REGISTRATE
            .virtualFluid("air")
            .properties(p -> p.viscosity(0).density(-100))
            .lang("Air")
            .bucket()
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> NITROGEN = gas("nitrogen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> OXYGEN = gas("oxygen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROGEN = gas("hydrogen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> ARGON = gas("argon");

    public static final FluidEntry<ForgeFlowingFluid.Flowing> STEAM = gas("steam");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> AMMONIA = gas("ammonia");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> CHLORINE = gas("chlorine");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> DINITROGEN_TETROXIDE = gas("dinitrogen_tetroxide");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROGEN_SULFIDE = gas("hydrogen_sulfide");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> VOLATILE_GAS = gas("volatile_gas");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> BUTANE = gas("butane");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> PROPANE = gas("propane");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> LPG = gas("lpg");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> ETHYLENE = gas("ethylene");

    public static FluidEntry<ForgeFlowingFluid.Flowing> gas(String name){
        return REGISTRATE
            .fluid(name, Petrochem.asResource("fluid/" + name + "_still"), Petrochem.asResource("fluid/" + name + "_flow"), TransparentFluidType::new)
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
