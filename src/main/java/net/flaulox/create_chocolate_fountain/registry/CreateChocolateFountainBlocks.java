package net.flaulox.create_chocolate_fountain.registry;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.AssetLookup.customBlockItemModel;
import static com.simibubi.create.foundation.data.AssetLookup.standardModel;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.flaulox.create_chocolate_fountain.Create_chocolate_fountain.CREATE_CHOCOLATE_FOUNTAIN_REGISTRATE;

@SuppressWarnings({"removal", "unused"})
public class CreateChocolateFountainBlocks {



    public static final BlockEntry<ChocolateFountainBlock> CHOCOLATE_FOUNTAIN = CREATE_CHOCOLATE_FOUNTAIN_REGISTRATE.block("chocolate_fountain", ChocolateFountainBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_CYAN))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {}

}
