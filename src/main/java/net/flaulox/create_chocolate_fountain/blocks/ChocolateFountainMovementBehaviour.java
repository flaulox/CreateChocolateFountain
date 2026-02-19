package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class ChocolateFountainMovementBehaviour implements MovementBehaviour {

    @Override
    public void tick(MovementContext context) {
        if (context.world.isClientSide)
            return;

        StructureBlockInfo info = context.contraption.getBlocks().get(context.localPos);
        if (info == null || !info.state().hasProperty(ChocolateFountainBlock.HALF))
            return;

        syncFluidTypeWithOtherHalf(context, info);
    }

    private void syncFluidTypeWithOtherHalf(MovementContext context, StructureBlockInfo info) {
        BlockPos otherPos = getOtherHalfPos(context.localPos, info.state());
        StructureBlockInfo otherInfo = context.contraption.getBlocks().get(otherPos);

        if (otherInfo == null || !otherInfo.state().hasProperty(ChocolateFountainBlock.FLUID_TYPE))
            return;

        int fluidType = info.state().getValue(ChocolateFountainBlock.FLUID_TYPE);
        if (otherInfo.state().getValue(ChocolateFountainBlock.FLUID_TYPE) == fluidType)
            return;

        BlockState newState = otherInfo.state().setValue(ChocolateFountainBlock.FLUID_TYPE, fluidType);
        context.contraption.entity.setBlock(otherPos, new StructureBlockInfo(otherInfo.pos(), newState, otherInfo.nbt()));
    }

    private BlockPos getOtherHalfPos(BlockPos pos, BlockState state) {
        DoubleBlockHalf half = state.getValue(ChocolateFountainBlock.HALF);
        return half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
    }
}
