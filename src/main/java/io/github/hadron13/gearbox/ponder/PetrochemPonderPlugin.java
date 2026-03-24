package io.github.hadron13.gearbox.ponder;

import io.github.hadron13.gearbox.Petrochem;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;

public class PetrochemPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Petrochem.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PetrochemPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PetrochemPonderTags.register(helper);
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {

    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {

    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {

    }
}
