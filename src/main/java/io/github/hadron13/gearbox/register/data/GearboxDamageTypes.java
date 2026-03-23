package io.github.hadron13.gearbox.register.data;

import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class GearboxDamageTypes {
    public static final ResourceKey<DamageType>
        laser = key("laserdeath");

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Gearbox.asResource(name));
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(laser).scaling(DamageScaling.ALWAYS).register(ctx);
    }
}
