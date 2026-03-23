package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import io.github.hadron13.gearbox.Gearbox;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class GearboxSpriteShifts {

    public static final CTSpriteShiftEntry
            STEEL_FLUID_TANK = getCT(AllCTTypes.RECTANGLE, "steel_tank/steel_fluid_tank"),
            STEEL_FLUID_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "steel_tank/steel_fluid_tank_top"),
            STEEL_FLUID_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "steel_tank/steel_fluid_tank_inner");


    public void register(){

    }

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }

    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }

    //

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Gearbox.asResource(originalLocation), Gearbox.asResource(targetLocation));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Gearbox.asResource("block/" + blockTextureName),
                Gearbox.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}
