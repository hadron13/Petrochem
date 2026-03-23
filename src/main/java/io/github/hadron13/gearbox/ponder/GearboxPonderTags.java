package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class GearboxPonderTags {



    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);



        HELPER.addToTag(FLUIDS)
                .add(GearboxBlocks.CENTRIFUGE)
                .add(GearboxBlocks.ELECTROLYZER)
                .add(GearboxBlocks.PUMPJACK_WELL)
                .add(GearboxBlocks.STEEL_FLUID_PIPE)
                .add(GearboxBlocks.STEEL_FLUID_TANK)
                .add(GearboxBlocks.DISTILLATION_CONTROLLER)
                .add(GearboxBlocks.DISTILLATION_OUTPUT);
//                .add(GearboxBlocks.DIPPER);

        HELPER.addToTag(KINETIC_APPLIANCES)
                .add(GearboxBlocks.CENTRIFUGE)
                .add(GearboxBlocks.KILN)
                .add(GearboxBlocks.PUMPJACK_CRANK);

    }
}
