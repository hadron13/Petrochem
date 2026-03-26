package io.github.hadron13.petrochem.misc;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.Create;
import io.github.hadron13.petrochem.Petrochem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.HashMap;
import java.util.Map;


@Mod.EventBusSubscriber
public class RemapHelper {


    @SubscribeEvent
    public static void remapBlocks(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Block> mapping : event.getMappings(Registries.BLOCK, "gearbox")) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();

            ResourceLocation remappedId = new ResourceLocation(Petrochem.MODID, path);
            Block remapped = ForgeRegistries.BLOCKS.getValue(remappedId);
            if(remapped != null) {
                Create.LOGGER.warn("Remapping block '{}' to '{}'", key, remappedId );
                try {
                    mapping.remap(remapped);
                } catch (Throwable t) {
                    Create.LOGGER.warn("Remapping block '{}' to '{}' failed: {}", key, remappedId, t);
                }
            }
        }
    }

    @SubscribeEvent
    public static void remapItems(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getMappings(Registries.ITEM, "gearbox")) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();

            ResourceLocation remappedId = new ResourceLocation(Petrochem.MODID, path);
            Item remapped = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Petrochem.MODID, path));
            if(remapped != null) {
                Create.LOGGER.warn("Remapping item '{}' to '{}'", key, remappedId );
                try {
                    mapping.remap(remapped);
                } catch (Throwable t) {
                    Create.LOGGER.warn("Remapping item '{}' to '{}' failed: {}", key, remappedId, t);
                }
            }

        }
    }

    @SubscribeEvent
    public static void remapFluids(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Fluid> mapping : event.getMappings(Registries.FLUID, "gearbox")) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();

            ResourceLocation remappedId = new ResourceLocation(Petrochem.MODID, path);
            Fluid remapped = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(Petrochem.MODID, path));
            if(remapped != null) {
                Create.LOGGER.warn("Remapping fluid '{}' to '{}'", key, remappedId );
                try {
                    mapping.remap(remapped);
                } catch (Throwable t) {
                    Create.LOGGER.warn("Remapping fluid '{}' to '{}' failed: {}", key, remappedId, t);
                }
            }
        }
    }

    @SubscribeEvent
    public static void remapBlockEntities(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<BlockEntityType<?>> mapping : event.getMappings(Registries.BLOCK_ENTITY_TYPE, "gearbox")) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();

            ResourceLocation remappedId = new ResourceLocation(Petrochem.MODID, path);
            BlockEntityType <?> remapped = ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(new ResourceLocation(Petrochem.MODID, path));
            if(remapped != null) {
                Create.LOGGER.warn("Remapping BE '{}' to '{}'", key, remappedId );
                try {
                    mapping.remap(remapped);
                } catch (Throwable t) {
                    Create.LOGGER.warn("Remapping BE '{}' to '{}' failed: {}", key, remappedId, t);
                }
            }
        }
    }
}
