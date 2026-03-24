package io.github.hadron13.gearbox.register;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.hadron13.gearbox.Petrochem;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PetrochemPartialModels {
    public static final PartialModel
            COMPRESSOR_ROLL = block("compressor/roll"),
            ELECTROLYZER_HEAD = block("electrolyzer/head"),
            CENTRIFUGE_COG = block("centrifuge/cogwheel"),
            PUMPJACK_ARM = block("pumpjack/arm"),
            PUMPJACK_HEAD = block("pumpjack/head"),
            PUMPJACK_CONNECTOR = block("pumpjack/connector"),
            PUMPJACK_PITMAN = block("pumpjack/pitman"),
            PUMPJACK_CRANK = block("pumpjack/crank"),
            PUMPJACK_SMOOTHROD = block("pumpjack/smooth_rod"),
            DIPPER_POLE = block("dipper/pole"),
            ULTIMATE_MECH_CORE = item("ultimate_mechanism/core"),
            TAU_CANNON_COIL = item("tau_cannon/coil"),
            STEEL_FLUID_PIPE_CASING = block("steel_fluid_pipe/casing"),
            STEEL_PUMP_COG = block("steel_pump/cog"),
            DISTILLATION_SELECTOR = block("distillation_controller/head"),
            DISTILLATION_GAUGE = block("distillation_controller/gauge"),
            DISTILLATION_GAUGE_DIAL = block("distillation_controller/gauge_dial"),
            DISTILLATION_OUTPUT_BASE_UNPOWERED = block("distillation_output/base_unpowered"),
            DISTILLATION_OUTPUT_BASE_POWERED = block("distillation_output/base_powered");
    ;


    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> STEEL_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);



    static {
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials
                .values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("steel_fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            STEEL_PIPE_ATTACHMENTS.put(type, map);
        }
    }

    private static PartialModel block(String path) {
        return PartialModel.of(Petrochem.asResource("block/" + path));
    }

    private static PartialModel item(String path) {
        return PartialModel.of(Petrochem.asResource("item/" + path));
    }

    private static PartialModel entity(String path) {
        return PartialModel.of(Petrochem.asResource("entity/" + path));
    }

    public static void init() {
        // init static fields
    }

}
