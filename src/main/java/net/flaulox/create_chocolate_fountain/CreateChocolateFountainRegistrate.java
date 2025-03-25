package net.flaulox.create_chocolate_fountain;

import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.AbstractRegistrate;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CreateChocolateFountainRegistrate extends AbstractRegistrate<CreateChocolateFountainRegistrate> {

    @Nullable
    protected Function<Item, TooltipModifier> currentTooltipModifierFactory;


    protected CreateChocolateFountainRegistrate(String modid) {
        super(modid);
    }

    public static CreateChocolateFountainRegistrate create(String modid) {
        return new CreateChocolateFountainRegistrate(modid);
    }

    public CreateChocolateFountainRegistrate setTooltipModifierFactory(@Nullable Function<Item, TooltipModifier> factory) {
        currentTooltipModifierFactory = factory;
        return self();
    }
}
