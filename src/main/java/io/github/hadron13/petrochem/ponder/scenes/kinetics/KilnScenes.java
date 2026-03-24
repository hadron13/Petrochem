package io.github.hadron13.petrochem.ponder.scenes.kinetics;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import io.github.hadron13.petrochem.blocks.kiln.KilnBlockEntity;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class KilnScenes {
    public static void kiln(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("kiln", "Processing items with a Kiln");
        scene.configureBasePlate(0, 0, 5);

        BlockPos kiln = util.grid().at(2, 1, 2);
        BlockPos funnel = kiln.west();
        Vec3 kiln_top = util.vector().topOf(kiln);
        Selection cog = util.select().fromTo(2, 1, 3, 2, 1, 5);
        Selection large_cog = util.select().position(1, 0, 5);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().position(kiln), Direction.DOWN);

        scene.world().setKineticSpeed(large_cog, -16f);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Kinetic Ovens process items by burning them with friction")
                .pointAt(kiln_top)
                .placeNearTarget();
        scene.idle(70);

        scene.rotateCameraY(-90);

        scene.idle(20);


        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("They can be powered from the back using shafts")
                .pointAt(util.vector().centerOf(util.grid().at(2, 1, 2)).add(0, 0, 0.5))
                .placeNearTarget();

        scene.idle(60);

        scene.world().setKineticSpeed(cog.add(util.select().position(kiln)), 32f);
        scene.world().showSection(cog, Direction.DOWN);

        scene.effects().indicateSuccess(kiln);
        scene.idle(10);

        scene.rotateCameraY(90);
        scene.idle(20);

        scene.addKeyframe();
        ItemStack itemStack = new ItemStack(Items.COBBLESTONE);
        Vec3 entitySpawn = util.vector().topOf(kiln.above(3));

        ElementLink<EntityElement> entity1 =
                scene.world().createItemEntity(entitySpawn, util.vector().of(0, 0.2, 0), itemStack);

        scene.idle(18);
        scene.world().modifyEntity(entity1, Entity::discard);
        scene.world().modifyBlockEntity(kiln, KilnBlockEntity.class,
                ms -> ms.inputInv.setStackInSlot(0, itemStack));
        scene.world().toggleRedstonePower(util.select().position(kiln));
        scene.idle(10);
        scene.overlay().showControls(kiln_top, Pointing.DOWN, 30).withItem(itemStack);
        scene.idle(7);

        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Throw or Insert items at the top")
                .pointAt(kiln_top)
                .placeNearTarget();

        scene.idle(60);

        scene.world().modifyBlockEntity(kiln, KilnBlockEntity.class,
                ms -> ms.inputInv.setStackInSlot(0, ItemStack.EMPTY));
        scene.world().modifyBlockEntity(kiln, KilnBlockEntity.class,
                ms -> ms.outputInv.setStackInSlot(0, ItemStack.EMPTY));

        scene.overlay().showText(50)
                .text("After some time, the result can be obtained via Right-click")
                .pointAt(util.vector().blockSurface(kiln, Direction.WEST))
                .placeNearTarget();

        scene.world().toggleRedstonePower(util.select().position(kiln));

        ItemStack stone = new ItemStack(Items.STONE);
        scene.overlay().showControls(util.vector().blockSurface(kiln, Direction.NORTH), Pointing.RIGHT, 40).rightClick().withItem(stone);
        scene.idle(50);

        scene.addKeyframe();

        scene.world().showSection(util.select().position(funnel), Direction.EAST);
        scene.idle(20);

        entitySpawn = util.vector().centerOf(funnel);

        scene.world().flapFunnel(funnel, true);
        ElementLink<EntityElement> entity2 =
                scene.world().createItemEntity(entitySpawn, util.vector().of(0, 0, 0), stone);


        scene.overlay().showText(50)
                .text("The outputs can also be extracted by automation")
                .pointAt(util.vector().blockSurface(kiln, Direction.WEST)
                        .add(-.5, .4, 0))
                .placeNearTarget();
    }
}
