package io.github.hadron13.gearbox.item;

import io.github.hadron13.gearbox.data.GearboxDatagen;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import io.github.hadron13.gearbox.register.GearboxEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RadioactiveItem extends Item {
    public RadioactiveItem(Properties properties) {
        super(properties);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(level.random.nextInt(5) == 0){
            entity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), stack.getCount());
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
        ItemEntity e = GearboxEntities.RADIACTIVE_ITEM_ENTITY.create(level);
        e.setItem(stack);
        e.setPos(location.position());
        e.setDeltaMovement(location.getDeltaMovement());
        e.setPickUpDelay(10);
        return e;
    }
}
