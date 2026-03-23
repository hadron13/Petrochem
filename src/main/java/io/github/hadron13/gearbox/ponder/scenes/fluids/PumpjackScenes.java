package io.github.hadron13.gearbox.ponder.scenes.fluids;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PumpjackScenes {
    public static void pumpjack(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("pumpjack", "Using a pumpjack to gather resources");
        scene.configureBasePlate(0, 0, 7);

        BlockPos arm = util.grid().at(3, 3, 3);
        BlockPos well = util.grid().at(1, 1, 3);
        BlockPos crank= util.grid().at(5, 1, 3);


        scene.world().showSection(util.select().fromTo(0, 0, 0, 6, 0, 6), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(1, 1, 3, 5, 4, 3), Direction.DOWN);

        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The pumpjack is a multi-block resource extractor")
                .pointAt(Vec3.atCenterOf(arm));

        scene.idle(80);

        scene.overlay().showText(50)
                .placeNearTarget()
                .attachKeyFrame()
                .text("It consists of 3 blocks")
                .pointAt(Vec3.atCenterOf(arm));

        scene.idle(70);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Arm")
                .pointAt(Vec3.atCenterOf(arm));

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Crank")
                .pointAt(Vec3.atCenterOf(crank));

        scene.overlay().showText(60)
                .independent()
                .placeNearTarget()
                .attachKeyFrame()
                .text("Well")
                .pointAt(Vec3.atCenterOf(well));

        scene.idle(80);


        scene.world().hideSection(util.select().fromTo(0, 0, 0, 1, 0, 2), Direction.DOWN);

        scene.overlay().showText(80)
                .independent()
                .placeNearTarget()
                .attachKeyFrame()
                .text("The well needs to be connected to bedrock through pipes")
                .pointAt(Vec3.atCenterOf(well.below()));
        scene.idle(100);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 1, 0, 2), Direction.UP);

        scene.rotateCameraY(90);

        scene.world().showSection(util.select().fromTo(7, 0, 2, 7, 1,3), Direction.EAST);
        scene.world().showSection(util.select().position(6, 1, 3), Direction.EAST);
        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("And the crank be connected to a shaft to power the machine")
                .pointAt(Vec3.atCenterOf(crank));
        scene.idle(20);
        scene.world().setKineticSpeed(util.select().position(7, 0, 2), -32f);
        scene.world().setKineticSpeed(util.select().fromTo(7, 1, 3, 5, 1,3), 64f);

        scene.idle(80);

        scene.rotateCameraY(-180);

        scene.idle(20);

        scene.overlay().showText(80)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The results can then be extracted from the well with a pipe")
                .pointAt(Vec3.atCenterOf(well));

        scene.idle(40);

        scene.world().showSection(util.select().fromTo(1, 1, 4, 7, 2, 5), Direction.DOWN);
        scene.world().setKineticSpeed(util.select().fromTo(1, 1, 4, 7, 2, 4), -64f);
        scene.world().setKineticSpeed(util.select().position(2, 1, 5), 64f);

        scene.world().propagatePipeChange(util.grid().at(2, 1, 5));
        scene.idle(80);
        scene.rotateCameraY(90);
    }
}
