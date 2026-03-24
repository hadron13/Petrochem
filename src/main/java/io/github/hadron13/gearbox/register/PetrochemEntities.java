package io.github.hadron13.gearbox.register;


import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;

import io.github.hadron13.gearbox.Petrochem;
import io.github.hadron13.gearbox.item.RadioactiveItemEntity;

import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.MobCategory;

public class PetrochemEntities {
    private static final CreateRegistrate REGISTRATE = Petrochem.registrate();



    public static final EntityEntry<RadioactiveItemEntity> RADIACTIVE_ITEM_ENTITY = REGISTRATE
            .entity("radioactive_item", RadioactiveItemEntity::new, MobCategory.MISC)
            .properties(b -> b.setTrackingRange(3).setUpdateInterval(3))
            .renderer(() -> ItemEntityRenderer::new)
            .register();


    public static void register(){

    }
}
