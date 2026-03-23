package io.github.hadron13.gearbox.config.server;

import net.createmod.catnip.config.ConfigBase;

public class GBServer extends ConfigBase {

    public final GBKinetics kinetics = this.nested(0, GBKinetics::new, "Parameters and abilities of Gearbox's kinetic mechanisms");

    @Override
    public String getName() {
        return "server";
    }
}
