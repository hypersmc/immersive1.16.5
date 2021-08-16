package com.jumpwatch.tit.Registry;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class TagsRegistry {
    public static final ITag.INamedTag<Item> WRENCH_TAG = ItemTags.createOptional(new ResourceLocation("forge", "tools/wrench"));

}
