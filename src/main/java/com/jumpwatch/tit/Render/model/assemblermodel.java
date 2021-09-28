package com.jumpwatch.tit.Render.model;

import com.jumpwatch.tit.Tileentity.TileEntitiyBlockElectronicAssembler;
import com.jumpwatch.tit.Tileentity.TileEntityBlockCrusher;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class assemblermodel extends AnimatedGeoModel<TileEntitiyBlockElectronicAssembler> {
    @Override
    public ResourceLocation getModelLocation(TileEntitiyBlockElectronicAssembler object) {
        return new ResourceLocation(theinventorstech.MOD_ID, "geo/assembler.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntitiyBlockElectronicAssembler object) {
        return new ResourceLocation(theinventorstech.MOD_ID, "textures/assemblermap.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TileEntitiyBlockElectronicAssembler animatable) {
        return new ResourceLocation(theinventorstech.MOD_ID, "animations/assembler.animation.json");
    }
}
