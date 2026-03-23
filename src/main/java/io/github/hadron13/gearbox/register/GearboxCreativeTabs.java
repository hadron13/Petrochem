package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.Gearbox;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GearboxCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gearbox.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gearbox.main"))
                    .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                    .icon(AllBlocks.GEARBOX::asStack)
                    .displayItems(new RegistrateDisplayItemsGenerator(GearboxCreativeTabs.MAIN_TAB))
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {



        private final RegistryObject<CreativeModeTab> tabFilter;
        public RegistrateDisplayItemsGenerator(RegistryObject<CreativeModeTab> tabFilter) {

            this.tabFilter = tabFilter;
        }

        private List<Item> collectBlocks() {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : Gearbox.registrate().getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;


                items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();


            for (RegistryEntry<Item> entry : Gearbox.registrate().getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }
        List<Item> exclude = List.of(
        );
        private static void outputAll(CreativeModeTab.Output output, List<Item> items) {
            for (Item item : items) {
                output.accept(item);
            }
        }




        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
            List<Item> items = new LinkedList<>();
            items.addAll(collectBlocks());

            items.addAll(collectItems(tabFilter, (item) -> exclude.contains(item) || item instanceof SequencedAssemblyItem));

            outputAll(output, items);
        }
    }
}
