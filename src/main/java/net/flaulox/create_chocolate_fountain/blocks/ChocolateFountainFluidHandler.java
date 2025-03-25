package net.flaulox.create_chocolate_fountain.blocks;

import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ChocolateFountainFluidHandler implements IFluidHandler {

    private SmartFluidTank internalTank;
    public ChocolateFountainFluidHandler(SmartFluidTank internalTank) {

    }


    @Override
    public int getTanks() {
        return internalTank.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return internalTank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return internalTank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return internalTank.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!internalTank.isEmpty() && !FluidStack.isSameFluidSameComponents(resource, internalTank.getFluid()))
            return 0;
        if (resource.isEmpty() || !FluidHelper.hasBlockState(resource.getFluid()))
            return 0;

        int diff = resource.getAmount();

        FluidStack remaining = resource.copy();
        boolean deposited = false;

        if (action.simulate())
            return diff <= 0 ? resource.getAmount() : internalTank.fill(remaining, action);
        if (diff <= 0) {
            internalTank.drain(-diff, FluidAction.EXECUTE);
            return resource.getAmount();
        }

        return internalTank.fill(remaining, action) + (deposited ? 1000 : 0);
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return null;
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return null;
    }
}
