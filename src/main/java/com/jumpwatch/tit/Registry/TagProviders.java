package com.jumpwatch.tit.Registry;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class TagProviders {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer() ||event.includeClient()) {
            gen.addProvider(new WrenchTagProvider(gen, existingFileHelper));
        }
    }
}
