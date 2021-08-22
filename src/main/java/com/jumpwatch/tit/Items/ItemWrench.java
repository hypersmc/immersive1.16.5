package com.jumpwatch.tit.Items;

import com.jumpwatch.tit.Registry.TagsRegistry;
import com.jumpwatch.tit.Registry.theinventorsregistry;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemWrench extends Item {
    public ItemWrench() {
        super(new Properties().tab(theinventorsregistry.TIT_Group1).stacksTo(1));
        setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "wrench"));
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
