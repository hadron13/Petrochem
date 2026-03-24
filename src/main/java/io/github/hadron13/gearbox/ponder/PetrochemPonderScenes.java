package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.ponder.scenes.fluids.PumpjackScenes;
import io.github.hadron13.gearbox.ponder.scenes.kinetics.KilnScenes;
import io.github.hadron13.gearbox.register.PetrochemBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class PetrochemPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(PetrochemBlocks.PUMPJACK_WELL, PetrochemBlocks.PUMPJACK_CRANK, PetrochemBlocks.PUMPJACK_ARM)
                .addStoryBoard("pumpjack", PumpjackScenes::pumpjack, FLUIDS);

        HELPER.forComponents(PetrochemBlocks.KILN)
                .addStoryBoard("kiln", KilnScenes::kiln, KINETIC_APPLIANCES);

    }

}
