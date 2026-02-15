package net.flaulox.create_chocolate_fountain.registry;

import com.simibubi.create.AllCreativeModeTabs;
import net.flaulox.create_chocolate_fountain.Create_chocolate_fountain;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus;

public class CreateChocolateFountainCreativeModeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Create_chocolate_fountain.MODID);

    public static final RegistryObject<CreativeModeTab> BASE_CREATIVE_TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.create_chocolate_fountain.base"))
                    .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                    .icon(CreateChocolateFountainBlocks.CHOCOLATE_FOUNTAIN::asStack)
                    .displayItems((parameters, output) -> {
                        output.accept(CreateChocolateFountainBlocks.CHOCOLATE_FOUNTAIN.get());
                    }).build());

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

}
