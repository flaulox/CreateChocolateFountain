package net.flaulox.create_chocolate_fountain.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class ChocolateFountainShapes {
    public static final Map<Direction, VoxelShape> BOTTOM_SHAPES = new EnumMap<>(Direction.class);
    public static final Map<Direction, VoxelShape> TOP_SHAPES = new EnumMap<>(Direction.class);

    static {
        // Bottom Half Shapes
        VoxelShape bottomBase = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);

        BOTTOM_SHAPES.put(Direction.SOUTH, Shapes.or(
                bottomBase,
                Shapes.box(0.375, 0.8, 0.0625, 0.625, 2.0, 0.3125),
                Shapes.box(0.125, 0.9375, 0.0, 0.875, 1.0625, 0.75),
                Shapes.box(0.1875, 1.25, 0.0, 0.8125, 1.375, 0.625),
                Shapes.box(0.25, 1.5625, 0.0, 0.75, 1.6875, 0.5),
                Shapes.box(0.3125, 1.875, 0.0, 0.6875, 2.0, 0.375)
        ));

        BOTTOM_SHAPES.put(Direction.NORTH, Shapes.or(
                bottomBase,
                Shapes.box(0.375, 0.8, 0.6875, 0.625, 2.0, 0.9375),
                Shapes.box(0.125, 0.9375, 0.25, 0.875, 1.0625, 1.0),
                Shapes.box(0.1875, 1.25, 0.375, 0.8125, 1.375, 1.0),
                Shapes.box(0.25, 1.5625, 0.5, 0.75, 1.6875, 1.0),
                Shapes.box(0.3125, 1.875, 0.625, 0.6875, 2.0, 1.0)
        ));

        BOTTOM_SHAPES.put(Direction.WEST, Shapes.or(
                bottomBase,
                Shapes.box(0.6875, 0.8, 0.375, 0.9375, 2.0, 0.625),
                Shapes.box(0.25, 0.9375, 0.125, 1.0, 1.0625, 0.875),
                Shapes.box(0.375, 1.25, 0.1875, 1.0, 1.375, 0.8125),
                Shapes.box(0.5, 1.5625, 0.25, 1.0, 1.6875, 0.75),
                Shapes.box(0.625, 1.875, 0.3125, 1.0, 2.0, 0.6875)
        ));

        BOTTOM_SHAPES.put(Direction.EAST, Shapes.or(
                bottomBase,
                Shapes.box(0.0625, 0.8, 0.375, 0.3125, 2.0, 0.625),
                Shapes.box(0.0, 0.9375, 0.125, 0.75, 1.0625, 0.875),
                Shapes.box(0.0, 1.25, 0.1875, 0.625, 1.375, 0.8125),
                Shapes.box(0.0, 1.5625, 0.25, 0.5, 1.6875, 0.75),
                Shapes.box(0.0, 1.875, 0.3125, 0.375, 2.0, 0.6875)
        ));


        VoxelShape topBase = Shapes.box(0.0, -1.0, 0.0, 1.0, -0.1875, 1.0);

        TOP_SHAPES.put(Direction.SOUTH, Shapes.or(
                topBase,
                Shapes.box(0.375, -0.2, 0.0625, 0.625, 1.0, 0.3125),
                Shapes.box(0.125, -0.0625, 0.0, 0.875, 0.0625, 0.75),
                Shapes.box(0.1875, 0.25, 0.0, 0.8125, 0.375, 0.625),
                Shapes.box(0.25, 0.5625, 0.0, 0.75, 0.6875, 0.5),
                Shapes.box(0.3125, 0.875, 0.0, 0.6875, 1.0, 0.375)
        ));

        TOP_SHAPES.put(Direction.NORTH, Shapes.or(
                topBase,
                Shapes.box(0.375, -0.2, 0.6875, 0.625, 1.0, 0.9375),
                Shapes.box(0.125, -0.0625, 0.25, 0.875, 0.0625, 1.0),
                Shapes.box(0.1875, 0.25, 0.375, 0.8125, 0.375, 1.0),
                Shapes.box(0.25, 0.5625, 0.5, 0.75, 0.6875, 1.0),
                Shapes.box(0.3125, 0.875, 0.625, 0.6875, 1.0, 1.0)
        ));

        TOP_SHAPES.put(Direction.WEST, Shapes.or(
                topBase,
                Shapes.box(0.6875, -0.2, 0.375, 0.9375, 1.0, 0.625),
                Shapes.box(0.25, -0.0625, 0.125, 1.0, 0.0625, 0.875),
                Shapes.box(0.375, 0.25, 0.1875, 1.0, 0.375, 0.8125),
                Shapes.box(0.5, 0.5625, 0.25, 1.0, 0.6875, 0.75),
                Shapes.box(0.625, 0.875, 0.3125, 1.0, 1.0, 0.6875)
        ));

        TOP_SHAPES.put(Direction.EAST, Shapes.or(
                topBase,
                Shapes.box(0.0625, -0.2, 0.375, 0.3125, 1.0, 0.625),
                Shapes.box(0.0, -0.0625, 0.125, 0.75, 0.0625, 0.875),
                Shapes.box(0.0, 0.25, 0.1875, 0.625, 0.375, 0.8125),
                Shapes.box(0.0, 0.5625, 0.25, 0.5, 0.6875, 0.75),
                Shapes.box(0.0, 0.875, 0.3125, 0.375, 1.0, 0.6875)
        ));
    }
}
