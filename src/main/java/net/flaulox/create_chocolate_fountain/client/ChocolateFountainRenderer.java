package net.flaulox.create_chocolate_fountain.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.FluidRenderHelper;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlock;
import net.flaulox.create_chocolate_fountain.blocks.ChocolateFountainBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChocolateFountainRenderer extends SafeBlockEntityRenderer<ChocolateFountainBlockEntity> {

    private final FluidRenderHelper<FluidStack> fluidRenderer = new FluidRenderHelper<>();

    public ChocolateFountainRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(ChocolateFountainBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.getBlockState().getValue(ChocolateFountainBlock.HALF) != DoubleBlockHalf.UPPER)
            return;

        ChocolateFountainBlockEntity lowerBE = (ChocolateFountainBlockEntity) be.getLevel().getBlockEntity(be.getBlockPos().below());
        if (lowerBE == null)
            return;
        FluidStack fluid = lowerBE.getTank().getPrimaryHandler().getFluidInTank(0);
        if (fluid.isEmpty())
            return;

        float level = (float) fluid.getAmount() / lowerBE.getTank().getPrimaryHandler().getTankCapacity(0);
        if (level != 0) {
            float yMin = 5f / 16f - 1;
            float min = 2f / 16f;
            float max = min + (12 / 16f);
            float yOffset = (7 / 16f) * level;
            ms.pushPose();
            Direction facing = lowerBE.getBlockState().getValue(ChocolateFountainBlock.HORIZONTAL_FACING).getOpposite();
            ms.translate(0.5f, 0, 0.5f);
            ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-facing.toYRot()));
            ms.translate(-0.5f, yOffset, -0.5f);
            fluidRenderer.renderFluidBox(fluid, min, yMin - yOffset, min, max, yMin, max, buffer, ms, light, false, false);
            ms.popPose();
        }


        if (lowerBE.getBlockState().getValue(ChocolateFountainBlock.POWERED))
            return;


        var vb = FluidRenderHelper.getFluidBuilder(buffer);
        var clientFluid = IClientFluidTypeExtensions.of(fluid.getFluid());
        var spriteAtlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        var flowTexture = spriteAtlas.apply(clientFluid.getFlowingTexture(fluid));
        var stillTexture = spriteAtlas.apply(clientFluid.getStillTexture(fluid));
        int color = clientFluid.getTintColor(fluid);
        int blockLightIn = (light >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, fluid.getFluid().getFluidType().getLightLevel(fluid));
        light = (light & 0xF00000) | luminosity << 4;

        ms.pushPose();
        
        Direction facing = be.getBlockState().getValue(ChocolateFountainBlock.HORIZONTAL_FACING).getOpposite();
        ms.translate(0.5f, 0, 0.5f);
        ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-facing.toYRot()));
        ms.translate(-0.5f, 0, -0.5f);
        
        renderFlowingStream(ms, vb, 6/16f, -11/16f, 2.9f/16f, 10/16f, 0, 3.9f/16f, light, color, flowTexture, stillTexture);
        renderFlowingStream(ms, vb, 6/16f, 0, 4.9f/16f, 10/16f, 5/16f, 5.9f/16f, light, color, flowTexture, stillTexture);
        renderFlowingStream(ms, vb, 6/16f, 5/16f, 6.9f/16f, 10/16f, 10/16f, 7.9f/16f, light, color, flowTexture, stillTexture);
        renderFlowingStream(ms, vb, 6/16f, 10/16f, 8.9f/16f, 10/16f, 15/16f, 9.9f/16f, light, color, flowTexture, stillTexture);
        
        fluidRenderer.renderFluidBox(fluid, 2/16f, 0/16f + 0.001f, 3.9f/16f, 14/16f, 0/16f + 0.001f, 15.9f/16f, buffer, ms, light, true, false);
        fluidRenderer.renderFluidBox(fluid, 3/16f, 5/16f + 0.001f, 5.9f/16f, 13/16f, 5/16f + 0.001f, 15.9f/16f, buffer, ms, light, true, false);
        fluidRenderer.renderFluidBox(fluid, 4/16f, 10/16f + 0.001f, 7.9f/16f, 12/16f, 10/16f + 0.001f, 15.9f/16f, buffer, ms, light, true, false);
        fluidRenderer.renderFluidBox(fluid, 5/16f, 15/16f + 0.001f, 9.9f/16f, 11/16f, 15/16f + 0.001f, 15.9f/16f, buffer, ms, light, true, false);

        ms.popPose();
    }

    private void renderFlowingStream(PoseStack ms, com.mojang.blaze3d.vertex.VertexConsumer vb, float x1, float y1, float z1, float x2, float y2, float z2, int light, int color, TextureAtlasSprite flowTexture, TextureAtlasSprite stillTexture) {
        FluidRenderHelper.renderTiledFace(Direction.NORTH, x1, y1, x2, y2 + 0.001f, z1, vb, ms, light, color, flowTexture, 0.5f);
        FluidRenderHelper.renderTiledFace(Direction.SOUTH, x1, y1, x2, y2 + 0.001f, z2, vb, ms, light, color, flowTexture, 0.5f);
        FluidRenderHelper.renderTiledFace(Direction.WEST, z1, y1, z2, y2 + 0.001f, x1, vb, ms, light, color, flowTexture, 0.5f);
        FluidRenderHelper.renderTiledFace(Direction.EAST, z1, y1, z2, y2 + 0.001f, x2, vb, ms, light, color, flowTexture, 0.5f);
        FluidRenderHelper.renderStillTiledFace(Direction.UP, x1, z1, x2, z2, y2 + 0.001f, vb, ms, light, color, stillTexture);
    }
}
