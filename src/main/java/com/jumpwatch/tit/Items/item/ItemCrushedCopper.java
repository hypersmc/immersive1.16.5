package com.jumpwatch.tit.Items.item;

import com.jumpwatch.tit.theinventorstech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemCrushedCopper extends Item {
    public ItemCrushedCopper() {
        super(new Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(64).setNoRepair());
        setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "itemcrushedcopper"));
    }
}
