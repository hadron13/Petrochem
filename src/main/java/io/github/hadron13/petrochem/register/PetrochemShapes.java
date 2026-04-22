package io.github.hadron13.petrochem.register;


import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.*;

public class PetrochemShapes {


    public static final VoxelShaper
        PUMPJACK_CRANK = shape(2, 0, 2, 14, 16,14).forDirectional(NORTH),
        PUMPJACK_WELL = shape(2, 0, 0, 14, 16, 14).forDirectional(NORTH),
        PUMPJACK_PIVOT = shape(1, 0, 1, 15, 14, 15).forDirectional(NORTH),
        DIPPER = shape(0, -16, 0, 16, 16, 16).forDirectional(DOWN),
        DISTILLATION_OUTPUT = shape(3, 3, 3, 13, 13, 16).forDirectional(SOUTH),
        FLARESTACK = shape(1, 0, 1, 15, 10, 15)
                .add(2, 10, 2, 14, 20, 14).forDirectional(),
        SMALL_ENGINE = shape(1, 0, 0, 15, 3, 16)
                .add(3, 3, 1, 13, 13, 15)
                .forDirectional(NORTH),
    
        MEDIUM_ENGINE = shape(1, 0, 1, 15, 3, 15) //thick base
                .add(2, 0, 2, 14, 15, 14) //main body
//                .add(1, 5, 4, 15, 13, 12)
                .forHorizontalAxis(),
        MEDIUM_ENGINE_CEILING = shape(1, 13, 1, 15, 16, 15)
                .add(2, 1, 2, 14, 16, 14)
//                .add(1, 3, 4, 15, 11, 12)
                .forHorizontalAxis(),
        MEDIUM_ENGINE_WALL = shape(1, 1, 0, 15, 15, 3)
                .add(2, 2, 0, 14, 14, 15)
//                .add(1, 4, 5, 15, 12, 13)
                .forHorizontal(SOUTH);


    private static PetrochemShapes.Builder shape(VoxelShape shape) {
        return new PetrochemShapes.Builder(shape);
    }

    private static PetrochemShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {

        private VoxelShape shape;

        public Builder(VoxelShape shape) {
            this.shape = shape;
        }

        public PetrochemShapes.Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public PetrochemShapes.Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public PetrochemShapes.Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return shape;
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return factory.apply(shape, axis);
        }

        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Direction.Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }

        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional() {
            return forDirectional(UP);
        }

    }

}
