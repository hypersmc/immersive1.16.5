package com.jumpwatch.tit.Render;

import com.jumpwatch.tit.Render.model.assemblermodel;
import com.jumpwatch.tit.Tileentity.TileEntitiyBlockElectronicAssembler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AssemblerTileRenderer extends GeoBlockRenderer<TileEntitiyBlockElectronicAssembler> {
    public AssemblerTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new assemblermodel());
    }
    @Override
    public RenderType getRenderType(TileEntitiyBlockElectronicAssembler animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

}
