package com.jumpwatch.tit.Items;

import com.jumpwatch.tit.theinventorstech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemVoltmeter extends Item {
    public ItemVoltmeter() {
        super(new Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(1).setNoRepair());
        setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "voltmeter"));
    }


}
