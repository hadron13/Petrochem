package io.github.hadron13.gearbox.register;


import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;

import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.item.RadioactiveItemEntity;

import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.MobCategory;

public class GearboxEntities {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate();



    public static final EntityEntry<RadioactiveItemEntity> RADIACTIVE_ITEM_ENTITY = REGISTRATE
            .entity("radioactive_item", RadioactiveItemEntity::new, MobCategory.MISC)
            .properties(b -> b.setTrackingRange(3).setUpdateInterval(3))
            .renderer(() -> ItemEntityRenderer::new)
            .register();


    public static void register(){

    }
}
