package com.jumpwatch.tit.Items;

import com.jumpwatch.tit.theimmersivetech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemIronPlate extends Item {
    public ItemIronPlate() {
        super(new Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(1).setNoRepair());
        setRegistryName(new ResourceLocation(theimmersivetech.MOD_ID, "itemironplate"));
    }

}
