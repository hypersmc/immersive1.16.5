package com.jumpwatch.tit.Multiblockhandeling;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.world.level.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public abstract class ITBaseTileEntity extends TileEntity {


    public ITBaseTileEntity(TileEntityType<?> type) {
        super(type);
    }
}
