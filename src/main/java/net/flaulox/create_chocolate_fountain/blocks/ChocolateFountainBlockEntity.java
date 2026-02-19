package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.flaulox.create_chocolate_fountain.Config;
import net.flaulox.create_chocolate_fountain.compat.ConfectioneryCompat;
import net.flaulox.create_chocolate_fountain.compat.ThirstCompat;
import net.flaulox.create_chocolate_fountain.registry.CreateChocolateFountainBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock.HALF;

public class ChocolateFountainBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IProxyHoveringInformation {

    private SmartFluidTankBehaviour tank;
    private int cooldown;

    private static final Map<Fluid, FluidFoodProperties> FLUID_PROPERTIES = new HashMap<>();

    static {
        FLUID_PROPERTIES.put(AllFluids.CHOCOLATE.getSource(), new FluidFoodProperties(
            () -> Config.chocolateFountainFoodAmount,
            () -> Config.chocolateFountainSaturationAmount
        ));
        FLUID_PROPERTIES.put(AllFluids.HONEY.getSource(), new FluidFoodProperties(
            () -> Config.honeyFountainFoodAmount,
            () -> Config.honeyFountainSaturationAmount
        ));
    }

    private record FluidFoodProperties(java.util.function.IntSupplier food, java.util.function.Supplier<Float> saturation) {}

    public ChocolateFountainBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    // Behaviours

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, Config.chocolateFountainTankCapacity);
        tank.getPrimaryHandler().setValidator(fluidStack -> {
            if (fluidStack.getFluid().defaultFluidState().is(AllTags.AllFluidTags.CHOCOLATE.tag))
                return true;
            if (Config.autofeedHoney && fluidStack.getFluid().isSame(AllFluids.HONEY.get()))
                return true;
            if (Config.autofeedWater && fluidStack.getFluid().isSame(Fluids.WATER))
                return true;
            if (Config.autofeedConfectionery && ConfectioneryCompat.isConfectioneryFluid(fluidStack.getFluid()))
                return true;
            return false;
        });
        behaviours.add(tank);
    }

    // Capabilities

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                CreateChocolateFountainBlockEntityTypes.CHOCOLATE_FOUNTAIN.get(),
                (be, context) -> context == Direction.DOWN ? be.tank.getCapability() : null
        );
    }

    // Tick Logic

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide || !isLowerHalf())
            return;

        if (isPowered())
            return;

        updateRunningState();
        tickFeedingLogic();
    }

    private void updateRunningState() {
        Fluid fluid = tank.getCapability().getFluidInTank(0).getFluid();
        int fluidType = fluid.isSame(AllFluids.HONEY.getSource()) ? 1 : fluid.isSame(Fluids.WATER) ? 2 : 0;
        BlockPos pos = getBlockPos();
        
        updateBlockFluidType(pos, fluidType);
        updateBlockFluidType(pos.above(), fluidType);
    }

    private void updateBlockFluidType(BlockPos pos, int fluidType) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(ChocolateFountainBlock.FLUID_TYPE))
            level.setBlock(pos, state.setValue(ChocolateFountainBlock.FLUID_TYPE, fluidType), 3);
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
        Fluid fluid = tank.getCapability().getFluidInTank(0).getFluid();
        
        // Feed Water
        if (fluid.isSame(Fluids.WATER) && ThirstCompat.isLoaded()) {
            if (player.isCreative() || !ThirstCompat.needsThirst(player))
                return false;
            int consumed = Config.chocolateFountainConsumedPerUsage;
            if (tank.getCapability().getFluidInTank(0).getAmount() < consumed)
                return false;
            ThirstCompat.addThirst(player, Config.waterThirstAmount, Config.waterQuenchedAmount, tank.getCapability().getFluidInTank(0).copy());
            playDrinkingSounds(player);
            tank.getCapability().drain(consumed, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }

        // Feed Food
        if (player.isCreative() || !player.getFoodData().needsFood())
            return false;

        int consumed = Config.chocolateFountainConsumedPerUsage;
        if (tank.getCapability().getFluidInTank(0).getAmount() < consumed)
            return false;

        FluidFoodProperties props = FLUID_PROPERTIES.get(fluid);
        if (props != null) {
            player.getFoodData().eat(props.food.getAsInt(), props.saturation.get());
            if (fluid.isSame(AllFluids.HONEY.getSource())) {
                player.removeEffect(MobEffects.POISON);
            }
            playFeedingSounds(player);
            tank.getCapability().drain(consumed, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }

        // Confectionery Fluids
        if (ConfectioneryCompat.isConfectioneryFluid(fluid)) {
            player.getFoodData().eat(Config.chocolateFountainFoodAmount, Config.chocolateFountainSaturationAmount);
            ConfectioneryCompat.applyEffect(player, fluid);
            playFeedingSounds(player);
            tank.getCapability().drain(consumed, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }

        return false;
    }

    private void playFeedingSounds(Player player) {
        Fluid fluid = tank.getCapability().getFluidInTank(0).getFluid();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                fluid.isSame(AllFluids.HONEY.getSource()) ? SoundEvents.HONEY_DRINK : SoundEvents.GENERIC_EAT,
                SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, getBlockPos(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
                SoundSource.BLOCKS, 0.7f, 1.0f);
    }

    private void playDrinkingSounds(Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, getBlockPos(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
                SoundSource.BLOCKS, 0.7f, 1.0f);
    }

    // Goggle Information

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BlockPos infoSourcePos = getInformationSource(level, worldPosition, getBlockState());
        ChocolateFountainBlockEntity infoSourceBE = (ChocolateFountainBlockEntity) level.getBlockEntity(infoSourcePos);

        if (infoSourceBE != null)
            return infoSourceBE.containedFluidTooltip(tooltip, isPlayerSneaking, infoSourceBE.tank.getCapability());

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
        return level.hasNeighborSignal(getBlockPos());
    }

    public SmartFluidTankBehaviour getTank() {
        return tank;
    }
    
    public boolean tryConsumeHoneyForBee() {
        if (isPowered())
            return false;
        if (tank.getCapability().getFluidInTank(0).getFluid().isSame(AllFluids.HONEY.getSource())) {
            int consumed = Config.beeCalmingConsumed;
            if (tank.getCapability().getFluidInTank(0).getAmount() >= consumed) {
                tank.getCapability().drain(consumed, IFluidHandler.FluidAction.EXECUTE);
                level.playSound(null, getBlockPos(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
                        SoundSource.BLOCKS, 0.7f, 1.0f);
                return true;
            }
        }
        return false;
    }
}
