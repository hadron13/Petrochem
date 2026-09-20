package io.github.hadron13.petrochem.config.common;

import io.github.hadron13.petrochem.config.server.GBServer;
import net.createmod.catnip.config.ConfigBase;

public class GBCommon extends ConfigBase {

    public final ConfigInt turbineEnergyProduction =
            i(128, 1, "turbineProduction", "[in FE/tick]", Comments.turbine_production);

    public final ConfigBool basinsLeakGas = b(true, "basinsLeakGas", "", Comments.basin_leak);

    @Override
    public String getName() {
        return "common";
    }


    public static class Comments{
        static String turbine_production = "The amount of FE produced by the gas turbine every tick, does not affect consumption rate";
        static String basin_leak = "Whether basins leak gasses without a basin shroud";
    }
}
