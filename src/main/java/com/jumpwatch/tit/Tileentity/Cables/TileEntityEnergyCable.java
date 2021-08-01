package com.jumpwatch.tit.Tileentity.Cables;

import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Tileentity.Types.CableTypes;
import com.jumpwatch.tit.Tileentity.Types.EnergyCableType;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCable extends TileEntityCableLogic {
    public TileEntityEnergyCable() {
        super(TileentityRegistry.ENERGY_CABLE, new CableTypes[]{EnergyCableType.INSTANCE}, false, false, true);
    }
}
