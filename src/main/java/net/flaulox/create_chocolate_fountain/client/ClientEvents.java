package net.flaulox.create_chocolate_fountain.client;

import net.flaulox.create_chocolate_fountain.Create_chocolate_fountain;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Create_chocolate_fountain.MODID, value = Dist.CLIENT)
public class ClientEvents {
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CreateChocolateFountainBlockEntityTypes.CHOCOLATE_FOUNTAIN.get(), ChocolateFountainRenderer::new);
    }
}
