package com.jumpwatch.tit;

import com.jumpwatch.tit.Registry.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("theinventorstech")
public class theinventorstech
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "theinventorstech";
    public theinventorstech() {
        LOGGER.info("Starting Registry");
        theimmersiveregistry.register();
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, BlockRegistry::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ItemRegistry::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, BlockRegistry::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, TileentityRegistry::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TagProviders::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetups);
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(theinventorstech.this::clientSetup);
        });

    }

    public void commonSetups(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(BlockRegistry.ITEM_CABLE.getBlock(), RenderType.cutout() );
        TileentityRegistry.clientSetup();
    }



}
