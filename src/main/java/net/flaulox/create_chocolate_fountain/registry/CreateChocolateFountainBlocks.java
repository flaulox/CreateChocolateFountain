package net.flaulox.create_chocolate_fountain.registry;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainMovementBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.flaulox.create_chocolate_fountain.Create_chocolate_fountain.REGISTRATE;


public class CreateChocolateFountainBlocks {

    public static final BlockEntry<ChocolateFountainBlock> CHOCOLATE_FOUNTAIN = REGISTRATE.block("chocolate_fountain", ChocolateFountainBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_CYAN))
            .transform(pickaxeOnly())
            .onRegister(MovementBehaviour.movementBehaviour(new ChocolateFountainMovementBehaviour()))
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {}

}
