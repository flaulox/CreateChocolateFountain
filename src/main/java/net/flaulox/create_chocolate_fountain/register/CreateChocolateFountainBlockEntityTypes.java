package net.flaulox.create_chocolate_fountain.register;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.pulley.HosePulleyVisual;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.flaulox.create_chocolate_fountain.CreateChocolateFountainRegistrate;
import net.flaulox.create_chocolate_fountain.Create_chocolate_fountain;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlockEntity;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainRenderer;

public class CreateChocolateFountainBlockEntityTypes {
    private static final CreateChocolateFountainRegistrate CREATE_CHOCOLATE_FOUNTAIN_REGISTRATE = Create_chocolate_fountain.registrate();

    public static final BlockEntityEntry<ChocolateFountainBlockEntity> CHOCOLATE_FOUNTAIN = CREATE_CHOCOLATE_FOUNTAIN_REGISTRATE
            .blockEntity("chocolate_fountain", ChocolateFountainBlockEntity::new)
            .validBlocks(CreateChocolateFountainBlocks.CHOCOLATE_FOUNTAIN)
            .renderer(() -> ChocolateFountainRenderer::new)
            .register();

    public static void register() {
    }
}
