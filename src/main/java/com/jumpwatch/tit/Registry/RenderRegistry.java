package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Render.AssemblerTileRenderer;
import com.jumpwatch.tit.Render.MaceratorTileRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class RenderRegistry {

    public static void renderRegistry() {
        ClientRegistry.bindTileEntityRenderer(TileentityRegistry.Block_Crusher.get(), MaceratorTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileentityRegistry.Block_electronicassembler.get(), AssemblerTileRenderer::new);
    }
}
