package net.flaulox.create_chocolate_fountain.ponder.scenes;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ChocolateFountainScenes {
    public static void intro(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("chocolate_fountain", "The Chocolate Fountain in Action");
        scene.configureBasePlate(1, 0, 5);
        scene.showBasePlate();
        scene.idle(5);

        // Key components
        Selection fountain = util.select().position(3, 2, 1);
        Selection fountain_top = util.select().position(3, 3, 1);
        BlockPos fountainPos = util.grid().at(3, 2, 1);
        Selection tank = util.select().fromTo(2, 1, 4, 2, 3, 4);
        BlockPos tankPos = util.grid().at(2, 1, 4);
        Selection pump = util.select().position(2, 1, 3);
        Selection pipes = util.select().fromTo(2, 1, 1, 3, 1, 1);
        Selection pipe_seethrough = util.select().position(2, 1, 2);
        Selection small_cog = util.select().position(1, 1, 3);
        Selection large_cog = util.select().position(0, 0, 3);
        Selection lever = util.select().position(2, 2, 1);
        BlockPos leverPos = util.grid().at(2, 2, 1);


        // fountain
        ElementLink<WorldSectionElement> fountainLink = scene.world().showIndependentSection(fountain, Direction.UP);
        scene.world().moveSection(fountainLink, util.vector().of(0, 0, 0), 0);
        ElementLink<WorldSectionElement> fountain_topLink = scene.world().showIndependentSection(fountain_top, Direction.UP);
        scene.world().moveSection(fountain_topLink, util.vector().of(0, 0, 0), 0);
        scene.idle(10);

        //pipes
        ElementLink<WorldSectionElement> pumpLink = scene.world().showIndependentSection(pump, Direction.UP);
        scene.world().moveSection(pumpLink, util.vector().of(0, 0, 0), 0);


        ElementLink<WorldSectionElement> pipesLink = scene.world().showIndependentSection(pipes, Direction.UP);
        scene.world().moveSection(pipesLink, util.vector().of(0, 0, 0), 0);


        ElementLink<WorldSectionElement> pipe_seethroughLink = scene.world().showIndependentSection(pipe_seethrough, Direction.UP);
        scene.world().moveSection(pipe_seethroughLink, util.vector().of(0, 0, 0), 0);


        ElementLink<WorldSectionElement> tankLink = scene.world().showIndependentSection(tank, Direction.UP);
        scene.world().moveSection(tankLink, util.vector().of(0, 0, 0), 0);
        FluidStack content = new FluidStack(AllFluids.CHOCOLATE.get()
                .getSource(), 16000);
        scene.world().modifyBlockEntity(tankPos, FluidTankBlockEntity.class, be -> be.getTankInventory()
                .fill(content, IFluidHandler.FluidAction.EXECUTE));

        scene.idle(10);



        // cogs
        ElementLink<WorldSectionElement> small_cogLink = scene.world().showIndependentSection(small_cog, Direction.UP);
        scene.world().moveSection(small_cogLink, util.vector().of(0, 0, 0), 0);
        scene.world().setKineticSpeed(small_cog, 64);


        ElementLink<WorldSectionElement> large_cogLink = scene.world().showIndependentSection(large_cog, Direction.UP);
        scene.world().moveSection(large_cogLink, util.vector().of(0, 0, 0), 0);
        scene.world().setKineticSpeed(large_cog, -64);
        scene.world().setKineticSpeed(pump, -64);
        scene.idle(10);

        scene.world().modifyBlock(fountainPos.above(), state ->
                state.setValue(ChocolateFountainBlock.RUNNING, true), false);

        scene.overlay().showText(80)
                .text("Chocolate Fountains can be supplied with chocolate from the bottom")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fountainPos.west(), Direction.UP));
        scene.idle(80);

        // lever
        ElementLink<WorldSectionElement> leverLink = scene.world().showIndependentSection(lever, Direction.UP);
        scene.world().moveSection(leverLink, util.vector().of(0, 0, 0), 0);
        scene.idle(10);

        scene.world().toggleRedstonePower(util.select().fromTo(2, 2, 1, 3, 2, 1));
        scene.effects().indicateRedstone(leverPos);

        scene.world().modifyBlock(fountainPos.above(), state ->
                state.setValue(ChocolateFountainBlock.RUNNING, false), false);
        scene.idle(10);

        scene.overlay().showText(80)
                .text("Chocolate Fountains can be turned off by a redstone signal")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(leverPos, Direction.UP));
        scene.idle(60);





    }

    public static void range(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("chocolate_fountain_range", "The Chocolate Fountain in Action");
        scene.configureBasePlate(0, 0, 21);
        scene.scaleSceneView(0.4f);
        scene.showBasePlate();


        // Key components
        Selection fountain = util.select().position(10, 2, 10);
        Selection fountain_top = util.select().position(10, 3, 10);
        BlockPos fountainPos = util.grid().at(10, 2, 10);
        Selection tank = util.select().fromTo(9, 1, 13, 9, 3, 13);
        BlockPos tankPos = util.grid().at(9, 1, 13);
        Selection pump = util.select().position(9, 1, 12);
        Selection pipes = util.select().fromTo(9, 1, 10, 10, 1, 10);
        Selection pipe_seethrough = util.select().position(9, 1, 11);
        Selection small_cog = util.select().position(8, 1, 12);
        Selection large_cog = util.select().position(7, 0, 12);



        // fountain
        ElementLink<WorldSectionElement> fountainLink = scene.world().showIndependentSection(fountain, Direction.UP);
        scene.world().moveSection(fountainLink, util.vector().of(0, 0, 0), 0);
        ElementLink<WorldSectionElement> fountain_topLink = scene.world().showIndependentSection(fountain_top, Direction.UP);
        scene.world().moveSection(fountain_topLink, util.vector().of(0, 0, 0), 0);

        scene.world().modifyBlock(fountainPos.above(), state ->
                state.setValue(ChocolateFountainBlock.RUNNING, true), false);


        //pipes
        ElementLink<WorldSectionElement> pumpLink = scene.world().showIndependentSection(pump, Direction.UP);
        scene.world().moveSection(pumpLink, util.vector().of(0, 0, 0), 0);


        ElementLink<WorldSectionElement> pipesLink = scene.world().showIndependentSection(pipes, Direction.UP);
        scene.world().moveSection(pipesLink, util.vector().of(0, 0, 0), 0);


        ElementLink<WorldSectionElement> pipe_seethroughLink = scene.world().showIndependentSection(pipe_seethrough, Direction.UP);
        scene.world().moveSection(pipe_seethroughLink, util.vector().of(0, 0, 0), 0);


        ElementLink<WorldSectionElement> tankLink = scene.world().showIndependentSection(tank, Direction.UP);
        scene.world().moveSection(tankLink, util.vector().of(0, 0, 0), 0);
        FluidStack content = new FluidStack(AllFluids.CHOCOLATE.get()
                .getSource(), 16000);
        scene.world().modifyBlockEntity(tankPos, FluidTankBlockEntity.class, be -> be.getTankInventory()
                .fill(content, IFluidHandler.FluidAction.EXECUTE));





        // cogs
        ElementLink<WorldSectionElement> small_cogLink = scene.world().showIndependentSection(small_cog, Direction.UP);
        scene.world().moveSection(small_cogLink, util.vector().of(0, 0, 0), 0);
        scene.world().setKineticSpeed(small_cog, 64);


        ElementLink<WorldSectionElement> large_cogLink = scene.world().showIndependentSection(large_cog, Direction.UP);
        scene.world().moveSection(large_cogLink, util.vector().of(0, 0, 0), 0);
        scene.world().setKineticSpeed(large_cog, -64);
        scene.world().setKineticSpeed(pump, -64);
        scene.idle(20);


        // range
        scene.overlay().showText(60)
                .text("By default chocolate Fountains have a range of 10 Blocks")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fountainPos.west(), Direction.UP));
        scene.idle(70);

        scene.overlay().showText(60)
                .text("Within this range Players get auto-fed with chocolate")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(util.grid().at(10, 2, 4), Direction.UP));
        scene.idle(60);



    }
}
