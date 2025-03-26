package net.flaulox.create_chocolate_fountain.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderScenes;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.flaulox.create_chocolate_fountain.Create_chocolate_fountain;
import net.minecraft.resources.ResourceLocation;

public class CreateChocolateFountainPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Create_chocolate_fountain.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CreateChocolateFountainPonderScenes.register(helper);
    }
}
