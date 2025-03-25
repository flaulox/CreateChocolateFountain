package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ChocolateFountainBlock extends HorizontalKineticBlock implements IBE<ChocolateFountainBlockEntity>, IWrenchable {
    public ChocolateFountainBlock(Properties properties) {
        super(properties);
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
}
