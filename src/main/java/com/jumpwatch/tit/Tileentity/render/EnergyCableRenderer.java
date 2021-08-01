package com.jumpwatch.tit.Tileentity.render;

import com.jumpwatch.tit.theimmersivetech;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class EnergyCableRenderer extends CableRenderer{

    public static final ResourceLocation EXTRACT_MODEL = new ResourceLocation(theimmersivetech.MOD_ID, "block/energy_cable_extract");

    public EnergyCableRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    ResourceLocation getModel() {
        return EXTRACT_MODEL;
    }
}
