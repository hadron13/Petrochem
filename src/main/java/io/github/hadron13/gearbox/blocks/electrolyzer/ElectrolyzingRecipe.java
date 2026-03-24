package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.hadron13.gearbox.register.PetrochemRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class ElectrolyzingRecipe extends BasinRecipe {
    public int requiredEnergy = 0;
    public ElectrolyzingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(PetrochemRecipeTypes.ELECTROLYZING, params);
    }

    public void readAdditional(JsonObject json) {
        requiredEnergy = GsonHelper.getAsInt(json, "energy", 1000);
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        requiredEnergy = buffer.readInt();
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("energy", requiredEnergy);
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(requiredEnergy);
    }
}
