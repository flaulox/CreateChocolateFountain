package net.flaulox.create_chocolate_fountain.ponder.scenes;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
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
        scene.world().propagatePipeChange(util.grid().at(2, 1, 3));
        scene.idle(10);

        scene.world().modifyBlockEntity(fountainPos, ChocolateFountainBlockEntity.class, be -> be.getTank()
                .getPrimaryHandler().fill(content, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(10);

        scene.overlay().showText(70)
                .text("Chocolate Fountains can be supplied with chocolate from below")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fountainPos, Direction.UP));
        scene.idle(80);

        // lever
        ElementLink<WorldSectionElement> leverLink = scene.world().showIndependentSection(lever, Direction.UP);
        scene.world().moveSection(leverLink, util.vector().of(0, 0, 0), 0);
        scene.idle(10);

        scene.world().toggleRedstonePower(util.select().fromTo(2, 2, 1, 3, 2, 1));
        scene.effects().indicateRedstone(leverPos);

        scene.overlay().showText(80)
                .text("A redstone signal will disable the fountain")
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
        scene.world().propagatePipeChange(util.grid().at(9, 1, 12));
        scene.idle(20);

        scene.world().modifyBlockEntity(fountainPos, ChocolateFountainBlockEntity.class, be -> be.getTank()
                .getPrimaryHandler().fill(content, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(10);

        scene.overlay().showText(70)
                .text("Chocolate Fountains have a range of 10 blocks by default")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fountainPos.west(), Direction.UP));
        scene.idle(10);


        // range - draw sphere
        int range = 10;
        Vec3 center = Vec3.atCenterOf(fountainPos);
        int segments = 32;
        
        // Draw 3 perpendicular circles to form a sphere wireframe
        for (int i = 0; i <= segments; i++) {
            double angle1 = 2 * Math.PI * i / segments;
            double angle2 = 2 * Math.PI * (i + 1) / segments;
            
            // XY circle
            Vec3 p1 = center.add(range * Math.cos(angle1), range * Math.sin(angle1), 0);
            Vec3 p2 = center.add(range * Math.cos(angle2), range * Math.sin(angle2), 0);
            scene.overlay().showBigLine(PonderPalette.GREEN, p1, p2, 200);
            
            // XZ circle
            p1 = center.add(range * Math.cos(angle1), 0, range * Math.sin(angle1));
            p2 = center.add(range * Math.cos(angle2), 0, range * Math.sin(angle2));
            scene.overlay().showBigLine(PonderPalette.GREEN, p1, p2, 200);
            
            // YZ circle
            p1 = center.add(0, range * Math.cos(angle1), range * Math.sin(angle1));
            p2 = center.add(0, range * Math.cos(angle2), range * Math.sin(angle2));
            scene.overlay().showBigLine(PonderPalette.GREEN, p1, p2, 200);
        }
        

        scene.idle(70);

        scene.overlay().showText(60)
                .text("Players within this range are automatically fed with chocolate")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(util.grid().at(4, 10, 10), Direction.UP));
        scene.idle(60);



    }

    public static void bees(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("chocolate_fountain_bees", "Calming Bees with Honey");
        scene.configureBasePlate(1, 0, 6);
        scene.showBasePlate();
        scene.idle(5);

        // Key components
        Selection fountain = util.select().position(3, 2, 2);
        Selection fountain_top = util.select().position(3, 3, 2);
        BlockPos fountainPos = util.grid().at(3, 2, 2);
        Selection tank = util.select().fromTo(2, 1, 5, 2, 3, 5);
        BlockPos tankPos = util.grid().at(2, 1, 5);
        Selection pump = util.select().position(2, 1, 4);
        Selection pipes = util.select().fromTo(2, 1, 4, 3, 1, 2);
        Selection pipe_seethrough = util.select().position(2, 1, 3);
        Selection small_cog = util.select().position(1, 1, 4);
        Selection large_cog = util.select().position(0, 0, 4);
        Selection beehive = util.select().position(5, 2, 1);
        BlockPos beehivePos = util.grid().at(5,2,1);
        Selection fence = util.select().position(5, 1, 1);

        scene.world().showIndependentSection(fountain, Direction.UP);
        scene.world().showIndependentSection(fountain_top, Direction.UP);
        scene.idle(10);

        scene.world().showIndependentSection(pump, Direction.UP);
        scene.world().showIndependentSection(pipes, Direction.UP);
        scene.world().showIndependentSection(pipe_seethrough, Direction.UP);
        scene.world().showIndependentSection(tank, Direction.UP);
        
        FluidStack honey = new FluidStack(AllFluids.HONEY.get().getSource(), 16000);
        scene.world().modifyBlockEntity(tankPos, FluidTankBlockEntity.class, be -> be.getTankInventory()
                .fill(honey, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(10);

        scene.world().showIndependentSection(small_cog, Direction.UP);
        scene.world().setKineticSpeed(small_cog, 64);
        scene.world().setKineticSpeed(pump, 64);
        scene.world().showIndependentSection(large_cog, Direction.UP);
        scene.world().setKineticSpeed(large_cog, -64);
        scene.world().propagatePipeChange(util.grid().at(2, 1, 4));
        scene.idle(10);



        scene.world().modifyBlockEntity(fountainPos, ChocolateFountainBlockEntity.class, be -> be.getTank()
                .getPrimaryHandler().fill(honey, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(10);

        scene.overlay().showText(80)
                .text("Chocolate Fountains also work with Honey")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fountainPos, Direction.UP));
        scene.idle(90);

        scene.world().showIndependentSection(beehive, Direction.DOWN);
        scene.world().showIndependentSection(fence, Direction.DOWN);
        scene.idle(10);


        scene.overlay().showText(80)
                .text("Nearby bees will not become angry around the Chocolate Fountain")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(beehivePos, Direction.WEST));
        scene.idle(90);

        scene.overlay().showText(80)
                .text("Already angry bees will be calmed when entering the fountain's range")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fountainPos, Direction.UP));
        scene.idle(90);
    }
}
