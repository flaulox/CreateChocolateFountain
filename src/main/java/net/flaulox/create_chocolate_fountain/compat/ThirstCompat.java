package net.flaulox.create_chocolate_fountain.compat;

import dev.ghen.thirst.content.purity.WaterPurity;
import dev.ghen.thirst.foundation.common.capability.ModAttachment;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.FluidStack;

public class ThirstCompat {
    private static final boolean LOADED = ModList.get().isLoaded("thirst");

    public static boolean isLoaded() {
        return LOADED;
    }

    public static boolean needsThirst(Player player) {
        if (!LOADED) return false;
        return ThirstRegistry.needsThirstInternal(player);
    }

    public static void addThirst(Player player, int amount, int quenched, FluidStack fluid) {
        if (!LOADED) return;
        ThirstRegistry.addThirstInternal(player, amount, quenched, fluid);
    }

    private static class ThirstRegistry {
        static boolean needsThirstInternal(Player player) {
            try {
                return player.getData(ModAttachment.PLAYER_THIRST).getThirst() < 20;
            } catch (Exception ignored) {
                return false;
            }
        }

        static void addThirstInternal(Player player, int thirst, int quenched, FluidStack fluid) {
            try {
                int purity = WaterPurity.getPurity(fluid);
                if (WaterPurity.givePurityEffects(player, purity)) {
                    player.getData(ModAttachment.PLAYER_THIRST).drink(thirst, quenched);
                }
            } catch (Exception ignored) {}
        }
    }
}
