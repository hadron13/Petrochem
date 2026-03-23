package io.github.hadron13.gearbox.data;

import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.data.GearboxDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GearboxGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, GearboxDamageTypes::bootstrap);

    public GearboxGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Gearbox.MODID));
    }

    @Override
    public String getName() {
        return "Gearbox's Generated Registry Entries";
    }
}
