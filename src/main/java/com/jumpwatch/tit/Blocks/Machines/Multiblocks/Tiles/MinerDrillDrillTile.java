package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class MinerDrillDrillTile extends MinerBaseTile {
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerDrillDrillTile::new;
    public MinerDrillDrillTile() {
        super(TYPE);
    }
}
