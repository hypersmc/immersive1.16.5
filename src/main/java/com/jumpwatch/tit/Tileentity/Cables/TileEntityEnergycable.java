package com.jumpwatch.tit.Tileentity.Cables;

import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Tileentity.Types.CableTypes;
import com.jumpwatch.tit.Tileentity.Types.EnergyCableType;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergycable extends TileEntityCableLogic {
    public TileEntityEnergycable() {
        super(TileentityRegistry.ENERGY_CABLE, new CableTypes[]{EnergyCableType.INSTANCE});
    }
}
