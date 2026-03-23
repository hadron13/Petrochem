package io.github.hadron13.gearbox.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RadioactiveItemEntity extends ItemEntity {
    public RadioactiveItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide)
            return;

        float radiactivty = 5.0f * (float)Mth.log2(getItem().getCount()/2);

        if(level().random.nextInt(10) == 0){
            AABB surrounding = AABB.ofSize(position(), radiactivty, radiactivty, radiactivty);
            List<Entity> entities = level().getEntities(null, surrounding);
            for(Entity e : entities){
                if(e instanceof LivingEntity livingEntity){
                    livingEntity.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), (radiactivty)/distanceTo(e));
                }
            }
        }
    }
}
