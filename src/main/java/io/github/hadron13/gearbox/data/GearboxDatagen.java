package io.github.hadron13.gearbox.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.ponder.GearboxPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class GearboxDatagen {
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        GearboxGeneratedEntriesProvider generatedEntriesProvider = new GearboxGeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);

    }

    private static void addExtraRegistrateData() {

        Gearbox.registrate().addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            //AllAdvancements.provideLang(langConsumer);
            //AllSoundEvents.provideLang(langConsumer);
            //AllKeys.provideLang(langConsumer);
            providePonderLang(langConsumer);
        });
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/gearbox/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register this since FMLClientSetupEvent does not run during datagen
        PonderIndex.addPlugin(new GearboxPonderPlugin());

        PonderIndex.getLangAccess().provideLang(Gearbox.MODID, consumer);
    }
}
