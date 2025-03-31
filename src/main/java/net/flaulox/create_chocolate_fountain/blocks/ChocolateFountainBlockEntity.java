package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.flaulox.create_chocolate_fountain.Config;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;

import static net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock.HALF;

public class ChocolateFountainBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IProxyHoveringInformation {


    private static int range;
    private static int tankCapacity;
    private static int consumedPerUsage;
    private int cooldown;
    private static int baseCooldown;
    private static int foodAmount;
    private static float saturationAmount;

    SmartFluidTankBehaviour tank;

    public ChocolateFountainBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, tankCapacity);
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
    public void onLoad() {
        super.onLoad();
        range = Config.chocolateFountainRange;
        tankCapacity = Config.chocolateFountainTankCapacity;
        consumedPerUsage = Config.chocolateFountainConsumedPerUsage;
        cooldown = Config.chocolateFountainCooldown;
        baseCooldown = Config.chocolateFountainCooldown;
        foodAmount = Config.chocolateFountainFoodAmount;
        saturationAmount = Config.chocolateFountainSaturationAmount;
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) return;

        BlockPos pos = getBlockPos();

        if (!stillValid(level, pos, getBlockState())) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return;
        }

        if (getBlockState().getValue(HALF) == Half.TOP) return;
        if (level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above())) return;

        List<Player> players = level.getEntitiesOfClass(Player.class,
                new net.minecraft.world.phys.AABB(
                        pos.getX() - range, pos.getY() - range, pos.getZ() - range,
                        pos.getX() + range, pos.getY() + range, pos.getZ() + range));


        if (tank.getCapability().getFluidInTank(0).getAmount() > 0) {
            level.setBlock(pos, getBlockState().setValue(ChocolateFountainBlock.RUNNING, true), 3);
            level.setBlock(pos.above(), level.getBlockState(pos.above()).setValue(ChocolateFountainBlock.RUNNING, true), 3);
        } else {
            level.setBlock(pos, getBlockState().setValue(ChocolateFountainBlock.RUNNING, false), 3);
            level.setBlock(pos.above(), level.getBlockState(pos.above()).setValue(ChocolateFountainBlock.RUNNING, false), 3);
        }

        this.cooldown += 1;
        if (this.cooldown >= baseCooldown) {
            for (Player player : players) {
                if (player.isCreative()) continue;
                if (player.getFoodData().needsFood() && tank.getCapability().getFluidInTank(0).getAmount() >= consumedPerUsage) {
                    player.getFoodData().eat(foodAmount, saturationAmount);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);
                    level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.7f, 1.0f);
                    tank.getCapability().drain(consumedPerUsage, IFluidHandler.FluidAction.EXECUTE);
                    this.cooldown = 0;
                }
            }
        }
    }

    public boolean stillValid(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == Half.TOP) {
            if (level.getBlockState(pos.below()).getBlock() == state.getBlock() && level.getBlockState(pos.below()).getValue(HALF) == Half.BOTTOM) {
                return true;
            }
        }
        if (state.getValue(HALF) == Half.BOTTOM) {
            if (level.getBlockState(pos.above()).getBlock() == state.getBlock() && level.getBlockState(pos.above()).getValue(HALF) == Half.TOP) {
                return true;
            }
        }
        return false;
    }






    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BlockState state = getBlockState();
        BlockPos infoSourcePos = getInformationSource(level, worldPosition, state);
        ChocolateFountainBlockEntity infoSourceBE = (ChocolateFountainBlockEntity) level.getBlockEntity(infoSourcePos);

        if (infoSourceBE != null) {
            return infoSourceBE.containedFluidTooltip(tooltip, isPlayerSneaking, infoSourceBE.tank.getCapability());
        }

        return false;
    }


    @Override
    public BlockPos getInformationSource(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == Half.TOP) {
            return pos.below();
        }
        return pos;
    }

}

