package net.flaulox.create_chocolate_fountain;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@EventBusSubscriber(modid = Create_chocolate_fountain.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();



    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_RANGE = BUILDER.comment("Set the range of Chocolate Fountain (in Blocks)").defineInRange("chocolate_fountain_range", 10, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_TANK_CAPACITY = BUILDER.comment("Set the internal Tank Capacity of the Chocolate Fountain (in mB)").defineInRange("chocolateFountainTankCapacity", 1000, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_CONSUMED_PER_USAGE = BUILDER.comment("Amount of Chocolate consumed when autofeeding the Player (in mB)").defineInRange("chocolateFountainConsumedPerUsage", 250, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_COOLDOWN = BUILDER.comment("Time between possible feeding actions (in ticks)").defineInRange("chocolateFountainCooldown", 40, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_FOOD_AMOUNT = BUILDER.comment("Amount of Food added to the Player)").defineInRange("chocolateFountainFoodAmount", 3, 0, 20);
    private static final ModConfigSpec.DoubleValue CHOCOLATE_FOUNTAIN_SATURATION_AMOUNT = BUILDER.comment("Amount of Saturation added to the Player").defineInRange("chocolateFountainSaturationAmount", 0.3, 0.0, 20.0 );



    static final ModConfigSpec SPEC = BUILDER.build();


    public static int chocolateFountainRange;
    public static int chocolateFountainTankCapacity;
    public static int chocolateFountainConsumedPerUsage;
    public static int chocolateFountainCooldown;
    public static int chocolateFountainFoodAmount;
    public static float chocolateFountainSaturationAmount;




    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

        chocolateFountainRange = CHOCOLATE_FOUNTAIN_RANGE.get();
        chocolateFountainTankCapacity = CHOCOLATE_FOUNTAIN_TANK_CAPACITY.get();
        chocolateFountainConsumedPerUsage = CHOCOLATE_FOUNTAIN_CONSUMED_PER_USAGE.get();
        chocolateFountainCooldown = CHOCOLATE_FOUNTAIN_COOLDOWN.get();
        chocolateFountainFoodAmount = CHOCOLATE_FOUNTAIN_FOOD_AMOUNT.get();
        chocolateFountainSaturationAmount = CHOCOLATE_FOUNTAIN_SATURATION_AMOUNT.get().floatValue();



    }
}
