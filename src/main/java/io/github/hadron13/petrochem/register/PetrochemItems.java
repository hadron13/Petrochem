package io.github.hadron13.petrochem.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.datamaps.BlazeBurnerFuel;
import com.simibubi.create.api.registry.CreateDataMaps;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.hadron13.petrochem.Petrochem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class PetrochemItems {
    private static final CreateRegistrate REGISTRATE = Petrochem.registrate().setCreativeTab(PetrochemCreativeModeTabs.INGREDIENTS);

    public static void register() {}
    public static final ItemEntry<Item>
        SULFUR_DUST = ingredient("sulfur_dust"),
        SALT_DUST = ingredient("salt_dust"),
        CAUSTIC_SODA = ingredient("caustic_soda"),
        STEEL_INGOT = taggedIngredient("steel_ingot", CommonMetal.STEEL.ingots),
        STEEL_SHEET = taggedIngredient("steel_sheet", CommonMetal.STEEL.plates),
        BRONZE_INGOT = taggedIngredient("bronze_ingot", PetrochemTags.Metals.BRONZE.ingots),
        BRONZE_SHEET = taggedIngredient("bronze_sheet", PetrochemTags.Metals.BRONZE.plates),
        TIN_NUGGET = taggedIngredient("tin_nugget", CommonMetal.TIN.nuggets)
                ;

    public static final ItemEntry<Item> PET_COKE = REGISTRATE.item("petroleum_coke", Item::new)
            .burnTime(3200)
            .register();


    public static final ItemEntry<Item> CARBON_DUST = REGISTRATE.item("carbon_dust", Item::new)
            .burnTime(2000)
            .register();


    public static final ItemEntry<Item> BLAZE_CANDY = REGISTRATE.item("blaze_candy", Item::new)
            .dataMap(CreateDataMaps.SUPERHEATED_BLAZE_BURNER_FUELS, new BlazeBurnerFuel(4000))
            .burnTime(8000)
            .register();

    static{
        PetrochemCreativeModeTabs.expert_item_ids.add("sulfur_dust");
    }


    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

}
