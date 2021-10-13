package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.tileentity.TileEntityType;

public class MinerDrillCoreTile extends MinerBaseTile {
    public static final TileSupplier SUPPLIER = MinerDrillCoreTile::new;
    public static TileEntityType<?> TYPE;
    public MinerDrillCoreTile() {
        super(TileentityRegistry.Miner_drillcore_tile.get());
    }

}
