package net.flaulox.create_chocolate_fountain;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


@EventBusSubscriber(modid = Create_chocolate_fountain.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();



    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_RANGE = BUILDER.comment("Set the range of Chocolate Fountain (in Blocks)").defineInRange("chocolate_fountain_range", 10, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_TANK_CAPACITY = BUILDER.comment("Set the internal Tank Capacity of the Chocolate Fountain (in mB)").defineInRange("chocolateFountainTankCapacity", 1000, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_CONSUMED_PER_USAGE = BUILDER.comment("Amount of Chocolate consumed when autofeeding the Player (in mB)").defineInRange("chocolateFountainConsumedPerUsage", 250, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_COOLDOWN = BUILDER.comment("Time between possible feeding actions (in ticks)").defineInRange("chocolateFountainCooldown", 40, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CHOCOLATE_FOUNTAIN_FOOD_AMOUNT = BUILDER.comment("Amount of Food added to the Player)").defineInRange("chocolateFountainFoodAmount", 3, 0, 20);
    private static final ModConfigSpec.DoubleValue CHOCOLATE_FOUNTAIN_SATURATION_AMOUNT = BUILDER.comment("Amount of Saturation added to the Player").defineInRange("chocolateFountainSaturationAmount", 0.3, 0.0, 20.0 );
    private static final ModConfigSpec.BooleanValue AUTOFEED_HONEY = BUILDER.comment("Enable Honey in Chocolate Fountain").define("autofeedHoney", true);
    private static final ModConfigSpec.IntValue HONEY_FOUNTAIN_FOOD_AMOUNT = BUILDER.comment("Amount of Food added to the Player when using Honey)").defineInRange("honeyFountainFoodAmount", 2, 0, 20);
    private static final ModConfigSpec.DoubleValue HONEY_FOUNTAIN_SATURATION_AMOUNT = BUILDER.comment("Amount of Saturation added to the Player when using Honey").defineInRange("honeyFountainSaturationAmount", 0.2, 0.0, 20.0 );
    private static final ModConfigSpec.IntValue BEE_CALMING_CONSUMED_PER_USAGE = BUILDER.comment("Amount of Honey consumed when calming a bee (in mB)").defineInRange("beeCalmingConsumed", 10, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue AUTOFEED_WATER = BUILDER.comment("Enable Water in Chocolate Fountain").define("autofeedWater", true);
    private static final ModConfigSpec.IntValue WATER_FOUNTAIN_THIRST_AMOUNT = BUILDER.comment("Amount of Thirst restored when using Water (requires Thirst Was Taken mod)").defineInRange("waterThirstAmount", 6, 0, 20);
    private static final ModConfigSpec.IntValue WATER_FOUNTAIN_QUENCHED_AMOUNT = BUILDER.comment("Amount of Thirst quenched when using Water (requires Thirst Was Taken mod)").defineInRange("waterQuenchedAmount", 8, 0, 20);
    private static final ModConfigSpec.BooleanValue AUTOFEED_CONFECTIONERY = BUILDER.comment("Enable Create Confectionery fluids in Chocolate Fountain").define("autofeedConfectionery", true);



    static final ModConfigSpec SPEC = BUILDER.build();


    public static int chocolateFountainRange;
    public static int chocolateFountainTankCapacity;
    public static int chocolateFountainConsumedPerUsage;
    public static int chocolateFountainCooldown;
    public static int chocolateFountainFoodAmount;
    public static float chocolateFountainSaturationAmount;
    public static boolean autofeedHoney;
    public static int honeyFountainFoodAmount;
    public static float honeyFountainSaturationAmount;
    public static boolean autofeedWater;
    public static int waterThirstAmount;
    public static int waterQuenchedAmount;
    public static int beeCalmingConsumed;
    public static boolean autofeedConfectionery;




    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC)
            return;

        if (event instanceof ModConfigEvent.Loading) {
            chocolateFountainRange = CHOCOLATE_FOUNTAIN_RANGE.get();
            chocolateFountainTankCapacity = CHOCOLATE_FOUNTAIN_TANK_CAPACITY.get();
            chocolateFountainConsumedPerUsage = CHOCOLATE_FOUNTAIN_CONSUMED_PER_USAGE.get();
            chocolateFountainCooldown = CHOCOLATE_FOUNTAIN_COOLDOWN.get();
            chocolateFountainFoodAmount = CHOCOLATE_FOUNTAIN_FOOD_AMOUNT.get();
            chocolateFountainSaturationAmount = CHOCOLATE_FOUNTAIN_SATURATION_AMOUNT.get().floatValue();
            autofeedHoney = AUTOFEED_HONEY.get();
            honeyFountainFoodAmount = HONEY_FOUNTAIN_FOOD_AMOUNT.get();
            honeyFountainSaturationAmount = HONEY_FOUNTAIN_SATURATION_AMOUNT.get().floatValue();
            autofeedWater = AUTOFEED_WATER.get();
            autofeedConfectionery = AUTOFEED_CONFECTIONERY.get();
            waterThirstAmount = WATER_FOUNTAIN_THIRST_AMOUNT.get();
            waterQuenchedAmount = WATER_FOUNTAIN_QUENCHED_AMOUNT.get();
            beeCalmingConsumed = BEE_CALMING_CONSUMED_PER_USAGE.get();
        }
    }
}
