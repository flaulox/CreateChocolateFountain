package net.flaulox.create_chocolate_fountain.register;

import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.infrastructure.config.CStress;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.flaulox.create_chocolate_fountain.Create_chocolate_fountain.CREATE_CHOCOLATE_FOUNTAIN_REGISTRATE;

@SuppressWarnings({"removal", "unused"})
public class CreateChocolateFountainBlocks {



    public static final BlockEntry<ChocolateFountainBlock> CHOCOLATE_FOUNTAIN = CREATE_CHOCOLATE_FOUNTAIN_REGISTRATE.block("chocolate_fountain", ChocolateFountainBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_CYAN))
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {}

}
