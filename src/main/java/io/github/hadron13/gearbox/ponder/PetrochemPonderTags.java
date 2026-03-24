package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.register.PetrochemBlocks;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class PetrochemPonderTags {



    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);



        HELPER.addToTag(FLUIDS)
                .add(PetrochemBlocks.CENTRIFUGE)
                .add(PetrochemBlocks.ELECTROLYZER)
                .add(PetrochemBlocks.PUMPJACK_WELL)
                .add(PetrochemBlocks.STEEL_FLUID_PIPE)
                .add(PetrochemBlocks.STEEL_FLUID_TANK)
                .add(PetrochemBlocks.DISTILLATION_CONTROLLER)
                .add(PetrochemBlocks.DISTILLATION_OUTPUT);
//                .add(GearboxBlocks.DIPPER);

        HELPER.addToTag(KINETIC_APPLIANCES)
                .add(PetrochemBlocks.CENTRIFUGE)
                .add(PetrochemBlocks.KILN)
                .add(PetrochemBlocks.PUMPJACK_CRANK);

    }
}
