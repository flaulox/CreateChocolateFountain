package net.flaulox.create_chocolate_fountain.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.flaulox.create_chocolate_fountain.ponder.scenes.ChocolateFountainScenes;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlocks;
import net.minecraft.resources.ResourceLocation;

public class CreateChocolateFountainPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(CreateChocolateFountainBlocks.CHOCOLATE_FOUNTAIN)
                .addStoryBoard("chocolate_fountain/intro", ChocolateFountainScenes::intro, AllCreatePonderTags.FLUIDS)
                .addStoryBoard("chocolate_fountain/range", ChocolateFountainScenes::range);
    }
}
