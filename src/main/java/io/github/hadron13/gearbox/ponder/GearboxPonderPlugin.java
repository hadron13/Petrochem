package io.github.hadron13.gearbox.ponder;

import io.github.hadron13.gearbox.Gearbox;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;

public class GearboxPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Gearbox.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        GearboxPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        GearboxPonderTags.register(helper);
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
