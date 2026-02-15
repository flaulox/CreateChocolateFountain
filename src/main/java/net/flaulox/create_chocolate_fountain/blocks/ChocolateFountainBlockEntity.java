package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.flaulox.create_chocolate_fountain.Config;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock.HALF;

public class ChocolateFountainBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IProxyHoveringInformation {

    private SmartFluidTankBehaviour tank;
    private int cooldown;

    public ChocolateFountainBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    // Behaviours

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, Config.chocolateFountainTankCapacity);
        tank.getPrimaryHandler().setValidator(fluidStack ->
                fluidStack.getFluid().defaultFluidState().is(AllTags.AllFluidTags.CHOCOLATE.tag)
        );
        behaviours.add(tank);
    }

    // Capabilities

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side == Direction.DOWN)
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }

    // Tick Logic

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide || !isLowerHalf())
            return;

        updateRunningState();
        
        if (!isPowered())
            tickFeedingLogic();
    }

    private void updateRunningState() {
        boolean hasFluid = tank.getCapability().resolve().map(handler -> handler.getFluidInTank(0).getAmount() > 0).orElse(false);
        boolean shouldRun = hasFluid && !isPowered();
        BlockPos pos = getBlockPos();
        
        updateBlockRunningState(pos, shouldRun);
        updateBlockRunningState(pos.above(), shouldRun);
    }

    private void updateBlockRunningState(BlockPos pos, boolean running) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(ChocolateFountainBlock.RUNNING))
            level.setBlock(pos, state.setValue(ChocolateFountainBlock.RUNNING, running), 3);
    }

    private void tickFeedingLogic() {
        cooldown++;
        if (cooldown < Config.chocolateFountainCooldown)
            return;

        List<Player> players = getNearbyPlayers();
        for (Player player : players) {
            if (tryFeedPlayer(player)) {
                cooldown = 0;
                break;
            }
        }
    }

    private List<Player> getNearbyPlayers() {
        BlockPos pos = getBlockPos();
        int range = Config.chocolateFountainRange;
        return level.getEntitiesOfClass(Player.class,
                new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range,
                        pos.getX() + range + 1, pos.getY() + range + 1, pos.getZ() + range + 1));
    }

    private boolean tryFeedPlayer(Player player) {
        if (player.isCreative() || !player.getFoodData().needsFood())
            return false;

        int consumed = Config.chocolateFountainConsumedPerUsage;
        return tank.getCapability().resolve().map(handler -> {
            if (handler.getFluidInTank(0).getAmount() < consumed)
                return false;

            player.getFoodData().eat(Config.chocolateFountainFoodAmount, Config.chocolateFountainSaturationAmount);
            playFeedingSounds(player);
            handler.drain(consumed, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }).orElse(false);
    }

    private void playFeedingSounds(Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, getBlockPos(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
                SoundSource.BLOCKS, 0.7f, 1.0f);
    }

    // Goggle Information

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BlockPos infoSourcePos = getInformationSource(level, worldPosition, getBlockState());
        ChocolateFountainBlockEntity infoSourceBE = (ChocolateFountainBlockEntity) level.getBlockEntity(infoSourcePos);

        if (infoSourceBE != null) {
            LazyOptional<? extends IFluidHandler> capability = infoSourceBE.tank.getCapability();
            return capability.resolve().map(handler -> 
                infoSourceBE.containedFluidTooltip(tooltip, isPlayerSneaking, capability.cast())
            ).orElse(false);
        }

        return false;
    }

    @Override
    public BlockPos getInformationSource(Level level, BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
    }

    // Helpers

    private boolean isLowerHalf() {
        return getBlockState().getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    private boolean isPowered() {
        BlockPos pos = getBlockPos();
        return level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
    }
}
