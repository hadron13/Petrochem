package io.github.hadron13.gearbox.register;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.item.RadioactiveItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GearboxItems {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate().setCreativeTab(GearboxCreativeTabs.MAIN_TAB);

    public static void register() {}

    public static final ItemEntry<Item>
        SULFUR_DUST = ingredient("sulfur_dust"),
        SALT_DUST = ingredient("salt_dust"),
        CAUSTIC_SODA = ingredient("caustic_soda");

    public static final ItemEntry<CombustibleItem> PET_COKE = REGISTRATE.item("petroleum_coke", CombustibleItem::new)
            .onRegister(i -> i.setBurnTime(4800))
            .register();


    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

}
