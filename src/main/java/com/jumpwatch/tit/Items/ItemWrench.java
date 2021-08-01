package com.jumpwatch.tit.Items;

import com.jumpwatch.tit.Registry.TagsRegistry;
import com.jumpwatch.tit.theimmersivetech;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemWrench extends Item {
    public ItemWrench() {
        super(new Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(1));
        setRegistryName(new ResourceLocation(theimmersivetech.MOD_ID, "wrench"));
    }

    public static boolean isWrench(ItemStack stack) {
        return stack.getItem().getRegistryName().equals("wrench");
    }

    public static boolean isHoldingWrenchItem(PlayerEntity player) {
        for (ItemStack stack : player.getHandSlots()) {
            if (isWrench(stack)) {
                return true;
            }
        }
        return false;
    }
}
