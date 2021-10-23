package com.jumpwatch.tit.Items;

import com.jumpwatch.tit.Registry.theinventorsregistry;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import net.minecraft.item.Item.Properties;

public class ItemVoltmeter extends Item {
    public ItemVoltmeter() {
        super(new Properties().tab(theinventorsregistry.TIT_Group1).stacksTo(1).setNoRepair());
        setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "voltmeter"));
    }


}
