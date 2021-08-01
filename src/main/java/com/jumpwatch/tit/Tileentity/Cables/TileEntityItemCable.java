package com.jumpwatch.tit.Tileentity.Cables;

import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Tileentity.Types.CableTypes;
import com.jumpwatch.tit.Tileentity.Types.ItemCableType;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityItemCable extends TileEntityCableLogic {
    public TileEntityItemCable() {
        super(TileentityRegistry.ITEM_CABLE, new CableTypes[]{ItemCableType.INSTANCE}, false, true, false);
    }
}
