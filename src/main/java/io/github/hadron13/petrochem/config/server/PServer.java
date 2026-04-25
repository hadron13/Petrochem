package io.github.hadron13.petrochem.config.server;

import net.createmod.catnip.config.ConfigBase;

public class PServer extends ConfigBase {

    public final PKinetics kinetics = this.nested(0, PKinetics::new, "Parameters and abilities of Petrochem's kinetic mechanisms");

    @Override
    public String getName() {
        return "server";
    }
}
