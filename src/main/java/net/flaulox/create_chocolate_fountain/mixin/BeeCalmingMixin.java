package net.flaulox.create_chocolate_fountain.mixin;

import com.simibubi.create.AllFluids;
import net.flaulox.create_chocolate_fountain.Config;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public class BeeCalmingMixin {
    @Inject(method = "setRemainingPersistentAngerTime", at = @At("HEAD"), cancellable = true)
    private void calmBeeNearHoneyFountain(int time, CallbackInfo ci) {
        if (time <= 0)
            return;
        
        Bee bee = (Bee) (Object) this;
        Level level = bee.level();
        
        if (level.isClientSide || !Config.autofeedHoney)
            return;
        
        BlockPos beePos = bee.blockPosition();
        int range = Config.chocolateFountainRange;
        
        for (BlockPos pos : BlockPos.betweenClosed(
                beePos.offset(-range, -range, -range),
                beePos.offset(range, range, range))) {
            if (level.getBlockEntity(pos) instanceof ChocolateFountainBlockEntity fountain) {
                if (fountain.tryConsumeHoneyForBee()) {
                    ci.cancel();
                    return;
                }
            }
        }
    }
    
    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void calmAngryBeeNearHoneyFountain(CallbackInfo ci) {
        Bee bee = (Bee) (Object) this;
        Level level = bee.level();
        
        if (level.isClientSide || !Config.autofeedHoney || !bee.isAngry())
            return;
        
        BlockPos beePos = bee.blockPosition();
        int range = Config.chocolateFountainRange;
        
        for (BlockPos pos : BlockPos.betweenClosed(
                beePos.offset(-range, -range, -range),
                beePos.offset(range, range, range))) {
            if (level.getBlockEntity(pos) instanceof ChocolateFountainBlockEntity fountain) {
                if (fountain.tryConsumeHoneyForBee()) {
                    bee.stopBeingAngry();
                    return;
                }
            }
        }
    }
}
