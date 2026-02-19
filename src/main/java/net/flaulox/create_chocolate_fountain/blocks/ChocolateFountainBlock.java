package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChocolateFountainBlock extends HorizontalKineticBlock implements IBE<ChocolateFountainBlockEntity>, IWrenchable {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty FLUID_TYPE = IntegerProperty.create("fluid_type", 0, 2);

    public ChocolateFountainBlock(Properties properties) {
        super(properties);
    }

    // Block State Definition

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF, POWERED, FLUID_TYPE);
    }

    // Placement

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() >= level.getMaxBuildHeight() - 1)
            return null;
        if (!level.getBlockState(pos.above()).canBeReplaced(context))
            return null;

        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(POWERED, false)
                .setValue(FLUID_TYPE, 0);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.is(this) || isMoving || state.getValue(HALF) != DoubleBlockHalf.LOWER)
            return;
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    // Double Block Logic

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return true;
        return level.getBlockState(pos.below()).is(this);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
                                   BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        boolean isVerticalNeighbor = (half == DoubleBlockHalf.LOWER && facing == Direction.UP) ||
                                     (half == DoubleBlockHalf.UPPER && facing == Direction.DOWN);

        if (!isVerticalNeighbor)
            return state;

        return state;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity()))
            level.removeBlockEntity(pos);

        if (isMoving || state.is(newState.getBlock()))
            return;

        BlockPos otherPos = isLower(state) ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(this) && otherState.getValue(HALF) != state.getValue(HALF)) {
            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            level.levelEvent(2001, otherPos, Block.getId(otherState));
        }
    }

    // Redstone

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide || !isLower(state))
            return;

        boolean isPowered = level.hasNeighborSignal(pos);

        if (isPowered != state.getValue(POWERED))
            level.setBlock(pos, state.setValue(POWERED, isPowered), 2);
    }

    // Wrench Interaction

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide)
            return InteractionResult.SUCCESS;

        InteractionResult result = super.onWrenched(state, context);
        syncOtherHalf(context.getLevel(), context.getClickedPos(), state);
        return result;
    }

    // Kinetics

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    // Shape

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(HORIZONTAL_FACING);
        boolean isUpper = state.getValue(HALF) == DoubleBlockHalf.UPPER;
        return isUpper
                ? ChocolateFountainShapes.TOP_SHAPES.getOrDefault(facing, Shapes.block())
                : ChocolateFountainShapes.BOTTOM_SHAPES.getOrDefault(facing, Shapes.block());
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return isLower(state) ? Shapes.block() : Shapes.empty();
    }

    // Block Entity

    @Override
    public Class<ChocolateFountainBlockEntity> getBlockEntityClass() {
        return ChocolateFountainBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChocolateFountainBlockEntity> getBlockEntityType() {
        return CreateChocolateFountainBlockEntityTypes.CHOCOLATE_FOUNTAIN.get();
    }

    // Helpers

    private static boolean isLower(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    private void syncOtherHalf(Level level, BlockPos pos, BlockState state) {
        BlockPos otherPos = isLower(state) ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(this) && otherState.getValue(HALF) != state.getValue(HALF)) {
            BlockState newState = level.getBlockState(pos);
            level.setBlock(otherPos, otherState.setValue(HORIZONTAL_FACING, newState.getValue(HORIZONTAL_FACING)), 3);
        }
    }
}
