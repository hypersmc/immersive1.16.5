package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Base.MinerBaseTile;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.tileentity.TileEntityType;

public class MinerDrillDrillTile extends MinerBaseTile {
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerDrillDrillTile::new;
    public MinerDrillDrillTile() {
        super(TileentityRegistry.Miner_drilldrill_tile.get());
    }
}
