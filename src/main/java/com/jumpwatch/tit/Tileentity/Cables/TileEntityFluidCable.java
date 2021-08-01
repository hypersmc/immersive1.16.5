package com.jumpwatch.tit.Tileentity.Cables;

import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Tileentity.Types.CableTypes;
import com.jumpwatch.tit.Tileentity.Types.FluidCableType;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityFluidCable extends TileEntityCableLogic {

    public TileEntityFluidCable() {
        super(TileentityRegistry.FLUID_CABLE, new CableTypes[]{FluidCableType.INSTANCE}, true, false, false);
    }
}
