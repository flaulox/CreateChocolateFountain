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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChocolateFountainBlock extends HorizontalKineticBlock implements IBE<ChocolateFountainBlockEntity>, IWrenchable {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public ChocolateFountainBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED, RUNNING, HALF);

    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        // Ensure there is space for both halves
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            BlockState lowerState = this.defaultBlockState()
                    .setValue(HALF, Half.BOTTOM)
                    .setValue(POWERED, level.hasNeighborSignal(pos))
                    .setValue(RUNNING, false)
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());

            BlockState upperState = this.defaultBlockState()
                    .setValue(HALF, Half.TOP)
                    .setValue(POWERED, level.hasNeighborSignal(pos))
                    .setValue(RUNNING, false)
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());

            // Set upper block
            level.setBlock(pos.above(), upperState, 3);
            return lowerState;
        }

        return null;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();

        // Get the rotated state for the clicked block
        BlockState rotatedState = getRotatedBlockState(state, clickedFace);
        if (!rotatedState.canSurvive(level, pos)) return InteractionResult.PASS;

        // Identify the other half's position
        BlockPos otherHalfPos = (state.getValue(HALF) == Half.BOTTOM) ? pos.above() : pos.below();
        BlockState otherHalfState = level.getBlockState(otherHalfPos);

        // Check if the other half belongs to the same Chocolate Fountain
        if (otherHalfState.getBlock() == state.getBlock() &&
                otherHalfState.getValue(HALF) != state.getValue(HALF)) {

            // Rotate the other half as well
            BlockState rotatedOtherHalf = getRotatedBlockState(otherHalfState, clickedFace);
            level.setBlock(otherHalfPos, rotatedOtherHalf, 3);
        }

        // Update the clicked block
        level.setBlock(pos, rotatedState, 3);
        System.out.println("After Rotation: " + level.getBlockState(otherHalfPos));

        return InteractionResult.SUCCESS;
    }



    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide || state.getValue(HALF) == Half.TOP) {
            return;
        }

        boolean isPowered = state.getValue(POWERED);
        boolean hasSignal = level.hasNeighborSignal(pos);

        if (isPowered == hasSignal) {
            return;
        }

        level.setBlock(pos, state.setValue(POWERED, hasSignal), 2);
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (aboveState.hasProperty(POWERED)) {
            level.setBlock(abovePos, aboveState.setValue(POWERED, hasSignal), 2);
        }

        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(abovePos, this);

        if (hasSignal) {
            scheduleActivation(level, pos);
        }
    }

    private void scheduleActivation(Level pLevel, BlockPos pPos) {
        if (!pLevel.getBlockTicks()
                .hasScheduledTick(pPos, this))
            pLevel.scheduleTick(pPos, this, 1);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return null;
    }

    @Override
    public Class<ChocolateFountainBlockEntity> getBlockEntityClass() {
        return ChocolateFountainBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChocolateFountainBlockEntity> getBlockEntityType() {
        return CreateChocolateFountainBlockEntityTypes.CHOCOLATE_FOUNTAIN.get();
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (state.getValue(HALF) == Half.TOP) {
            return ChocolateFountainShapes.TOP_SHAPES.getOrDefault(direction, Shapes.block());
        } else {
            return ChocolateFountainShapes.BOTTOM_SHAPES.getOrDefault(direction, Shapes.block());
        }

    }


    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (state.getValue(HALF) == Half.TOP) {
            return ChocolateFountainShapes.TOP_SHAPES.getOrDefault(direction, Shapes.block());
        } else {
            return Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        }
    }




}
