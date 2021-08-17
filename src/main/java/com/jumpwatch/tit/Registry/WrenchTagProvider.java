package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.theinventorstech;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WrenchTagProvider extends TagsProvider<Item> {
    public WrenchTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Registry.ITEM, theinventorstech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TagsRegistry.WRENCH_TAG).add(ItemRegistry.WRENCH);
    }

    @Override
    protected Path getPath(ResourceLocation id) {
        return generator.getOutputFolder().resolve(Paths.get("data", id.getNamespace(), "tags", "items", id.getPath() + ".json"));
    }

    @Override
    public String getName() {
        return "CableWrencheTags";    }
}
