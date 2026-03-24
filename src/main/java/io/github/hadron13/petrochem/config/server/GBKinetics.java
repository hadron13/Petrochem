package io.github.hadron13.petrochem.config.server;

import io.github.hadron13.petrochem.config.PetrochemStress;
import net.createmod.catnip.config.ConfigBase;

public class GBKinetics extends ConfigBase {
    public final PetrochemStress stressValues;

    public GBKinetics() {
        this.stressValues = this.nested(1, PetrochemStress::new, Comments.stress);
    }

    public String getName() {
        return "kinetics";
    }

    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";

        private Comments() {
        }
    }
}
