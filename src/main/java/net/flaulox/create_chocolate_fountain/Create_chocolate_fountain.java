package net.flaulox.create_chocolate_fountain;

import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlockEntity;
import net.flaulox.create_chocolate_fountain.ponder.CreateChocolateFountainPonderPlugin;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlocks;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainCreativeModeTab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(Create_chocolate_fountain.MODID)
public class Create_chocolate_fountain {

    public static final String MODID = "create_chocolate_fountain";
    public static final CreateChocolateFountainRegistrate REGISTRATE = CreateChocolateFountainRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    static {
        REGISTRATE.setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(null)));
    }

    // Initialization

    public Create_chocolate_fountain(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        CreateChocolateFountainBlocks.register();
        CreateChocolateFountainBlockEntityTypes.register();
        CreateChocolateFountainCreativeModeTab.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }

    public static CreateChocolateFountainRegistrate registrate() {
        return REGISTRATE;
    }

    // Event Handlers

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new CreateChocolateFountainPonderPlugin());
        }
    }

    @EventBusSubscriber(modid = MODID)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> BlockMovementChecks.registerAttachedCheck(Create_chocolate_fountain::checkChocolateFountainMovement));
        }

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            ChocolateFountainBlockEntity.registerCapabilities(event);
        }
    }

    // Contraption Movement Logic

    private static BlockMovementChecks.CheckResult checkChocolateFountainMovement(
            BlockState state,
            LevelAccessor world,
            BlockPos pos,
            Direction direction) {

        if (!(state.getBlock() instanceof ChocolateFountainBlock))
            return BlockMovementChecks.CheckResult.PASS;

        if (!state.hasProperty(ChocolateFountainBlock.HALF))
            return BlockMovementChecks.CheckResult.PASS;

        if (direction.getAxis() != Direction.Axis.Y)
            return BlockMovementChecks.CheckResult.FAIL;

        DoubleBlockHalf half = state.getValue(ChocolateFountainBlock.HALF);
        if (half == DoubleBlockHalf.LOWER && direction == Direction.UP)
            return BlockMovementChecks.CheckResult.SUCCESS;

        if (direction == Direction.DOWN)
            return BlockMovementChecks.CheckResult.SUCCESS;

        return BlockMovementChecks.CheckResult.FAIL;
    }
}
