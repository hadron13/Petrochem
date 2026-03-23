package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.ponder.scenes.fluids.PumpjackScenes;
import io.github.hadron13.gearbox.ponder.scenes.kinetics.KilnScenes;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class GearboxPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(GearboxBlocks.PUMPJACK_WELL, GearboxBlocks.PUMPJACK_CRANK, GearboxBlocks.PUMPJACK_ARM)
                .addStoryBoard("pumpjack", PumpjackScenes::pumpjack, FLUIDS);

        HELPER.forComponents(GearboxBlocks.KILN)
                .addStoryBoard("kiln", KilnScenes::kiln, KINETIC_APPLIANCES);

    }

}
