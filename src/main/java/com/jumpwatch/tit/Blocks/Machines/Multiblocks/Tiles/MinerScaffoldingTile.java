package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class MinerScaffoldingTile extends MinerBaseTile {
    public static final TileSupplier SUPPLIER = MinerScaffoldingTile::new;
    public static TileEntityType<?> TYPE;
    public MinerScaffoldingTile() {
        super(TYPE);
    }

}
