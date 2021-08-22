package com.jumpwatch.tit.Render.model;

import com.jumpwatch.tit.Tileentity.TileEntityBlockCrusher;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class maceratormodel extends AnimatedGeoModel<TileEntityBlockCrusher> {
    @Override
    public ResourceLocation getModelLocation(TileEntityBlockCrusher animatable) {
        return new ResourceLocation(theinventorstech.MOD_ID, "geo/macerator.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityBlockCrusher animatable) {
        return new ResourceLocation(theinventorstech.MOD_ID, "textures/block/maceratormp.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityBlockCrusher animatable) {
        return new ResourceLocation(theinventorstech.MOD_ID, "animations/macerator.animation.json");
    }
}
