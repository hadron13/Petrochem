package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class ReactingRecipe extends BasinRecipe {
    public int rpm_min, rpm_max;
    public FluidIngredient atmosphere = FluidIngredient.EMPTY;

    public ReactingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(GearboxRecipeTypes.REACTING, params);
    }

    public static boolean match(ReactorBlockEntity be, ReactingRecipe recipe){
        if(be.getBasin().isEmpty())
            return false;
        if(be.getSpeed() > recipe.rpm_max || be.getSpeed() < recipe.rpm_min)
            return false;
        if(!recipe.atmosphere.test(be.atmosphere_tank.getPrimaryHandler().getFluid()))
            return false;
        if(recipe.atmosphere.getRequiredAmount() > be.atmosphere_tank.getPrimaryHandler().getFluidAmount())
            return false;

        return BasinRecipe.match(be.getBasin().get(), recipe);
    }

    public void readAdditional(JsonObject json) {
        rpm_min = GsonHelper.getAsInt(json, "rpm_min", 0);
        rpm_max = GsonHelper.getAsInt(json, "rpm_max", 256);
        if (json.has("atmosphere")) {
            atmosphere = FluidIngredient.deserialize(json.get("atmosphere"));
        }
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        rpm_min = buffer.readInt();
        rpm_max = buffer.readInt();
        atmosphere = FluidIngredient.read(buffer);
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("rpm_min", rpm_min);
        json.addProperty("rpm_max", rpm_max);
        json.add("atmosphere", atmosphere.serialize());
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(rpm_min);
        buffer.writeInt(rpm_max);
        atmosphere.write(buffer);
    }
}
