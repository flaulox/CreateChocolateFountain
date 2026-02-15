package net.flaulox.create_chocolate_fountain.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.flaulox.create_chocolate_fountain.Create_chocolate_fountain;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlocks;
import net.minecraft.resources.ResourceLocation;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class CreateChocolateFountainPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Create_chocolate_fountain.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CreateChocolateFountainPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addToTag(AllCreatePonderTags.FLUIDS).add(CreateChocolateFountainBlocks.CHOCOLATE_FOUNTAIN);
    }
}
