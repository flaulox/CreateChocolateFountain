package net.flaulox.create_chocolate_fountain.compat;

import net.mcreator.createconfectionery.init.CreateConfectioneryModFluids;
import net.mcreator.createconfectionery.init.CreateConfectioneryModMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class ConfectioneryCompat {
    private static final boolean LOADED = ModList.get().isLoaded("create_confectionery");
    private static final Map<Fluid, EffectData> FLUID_PROPERTIES = new HashMap<>();

    static {
        if (LOADED) {
            try {
                map(CreateConfectioneryModFluids.BLACK_CHOCOLATE.get(), CreateConfectioneryModMobEffects.STIMULATION, 120);
                map(CreateConfectioneryModFluids.WHITE_CHOCOLATE.get(), CreateConfectioneryModMobEffects.REST, 600);
                map(CreateConfectioneryModFluids.CARAMEL.get(), MobEffects.MOVEMENT_SPEED, 200);
                map(CreateConfectioneryModFluids.RUBY_CHOCOLATE.get(), MobEffects.SATURATION, 40);
            } catch (Exception ignored) {}
        }
    }



    public static boolean isConfectioneryFluid(Fluid fluid) {
        return LOADED && FLUID_PROPERTIES.containsKey(fluid);
    }

    public static void applyEffect(Player player, Fluid fluid) {
        if (!LOADED) return;
        EffectData data = FLUID_PROPERTIES.get(fluid);
        if (data != null) {
            player.addEffect(new MobEffectInstance(data.effect, data.duration, 0));
        }
    }

    private record EffectData(Holder<MobEffect> effect, int duration) {}


    private static void map(Fluid fluid, Holder<MobEffect> effect, int duration) {
        FLUID_PROPERTIES.put(fluid, new EffectData(effect, duration));
    }
}
