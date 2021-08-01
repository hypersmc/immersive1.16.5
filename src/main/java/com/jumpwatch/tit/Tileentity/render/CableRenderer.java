package com.jumpwatch.tit.Tileentity.render;

import com.jumpwatch.tit.Tileentity.TileEntityBaseCable;
import com.jumpwatch.tit.Utils.CachedValue;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.List;

public abstract class CableRenderer extends TileEntityRenderer<TileEntityBaseCable> {

    protected Minecraft minecraft;

    protected CachedValue<IBakedModel> cachedModel;
    public CableRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        minecraft = Minecraft.getInstance();

        cachedModel = new CachedValue<>(() -> {
            IUnbakedModel modelOrMissing = ModelLoader.instance().getModelOrMissing(getModel());
            return modelOrMissing.bake(ModelLoader.instance(), ModelLoader.instance().getSpriteMap()::getSprite, ModelRotation.X0_Y0, getModel());
        });

    }

    @Override
    public void render(TileEntityBaseCable cable, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        IBakedModel iBakedModel = cachedModel.get();
        List<BakedQuad> quads = iBakedModel.getQuads(null, null, minecraft.level.random, EmptyModelData.INSTANCE);
        IVertexBuilder b = buffer.getBuffer(RenderType.solid());

        for (Direction side : Direction.values()) {
            if (cable.isExtracting(side)) {
                renderExtractor(side, matrixStack, b, quads, combinedLight, combinedOverlay);
            }
        }
    }

    private void renderExtractor(Direction direction, MatrixStack matrixStack, IVertexBuilder b, List<BakedQuad> quads, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(direction.getStepX() * 0.001D, direction.getStepY() * 0.001D, direction.getStepZ() * 0.001D);
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.mulPose(getRotation(direction));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        for (BakedQuad quad : quads) {
            b.putBulkData(matrixStack.last(), quad, 1F, 1F, 1F, combinedLight, combinedOverlay);
        }

        matrixStack.popPose();
    }

    private Quaternion getRotation(Direction direction) {
        Quaternion q = Quaternion.ONE.copy();
        switch (direction) {
            case NORTH:
                return q;
            case SOUTH:
                q.mul(Vector3f.YP.rotationDegrees(180F));
                return q;
            case WEST:
                q.mul(Vector3f.YP.rotationDegrees(90F));
                return q;
            case EAST:
                q.mul(Vector3f.YP.rotationDegrees(270F));
                return q;
            case UP:
                q.mul(Vector3f.XP.rotationDegrees(90F));
                return q;
            case DOWN:
            default:
                q.mul(Vector3f.XP.rotationDegrees(270F));
                return q;
        }
    }

    abstract ResourceLocation getModel();
}
