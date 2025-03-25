package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;

public class ChocolateFountainBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {


    private static final int radius = 10;
    private int cooldown = 40;

    SmartFluidTankBehaviour tank;

    public ChocolateFountainBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 1000);
        tank.getPrimaryHandler().setValidator(fluidStack ->
                fluidStack.getFluid().defaultFluidState().is(AllTags.commonFluidTag("chocolates"))
        );
        behaviours.add(tank);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                CreateChocolateFountainBlockEntityTypes.CHOCOLATE_FOUNTAIN.get(),
                (be, context) -> {
                    // Only allow input from the bottom
                    if (context != Direction.DOWN) {
                        return null;
                    }

                    return be.tank.getCapability();
                }
        );
    }


    @Override
    public void tick() {
        super.tick();
        System.out.println("test");
        if (level == null || level.isClientSide) return;
        BlockPos pos = getBlockPos();
        List<Player> players = level.getEntitiesOfClass(Player.class,
                new net.minecraft.world.phys.AABB(
                        pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                        pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius));

        this.cooldown += 1;
        if (this.cooldown >= 40) {
            for (Player player : players) {
                if (player.isCreative()) continue;
                if (player.getFoodData().needsFood() && tank.getCapability().getFluidInTank(0).getAmount() >= 250) {
                    player.getFoodData().eat(6, 0.3f);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);
                    level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.7f, 1.0f);
                    tank.getCapability().drain(250, IFluidHandler.FluidAction.EXECUTE);
                    this.cooldown = 0;
                }
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability());
    }

}

