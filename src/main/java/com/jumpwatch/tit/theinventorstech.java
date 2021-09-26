package com.jumpwatch.tit;

import com.jumpwatch.tit.Registry.*;
import com.jumpwatch.tit.Render.MaceratorTileRenderer;

import com.jumpwatch.tit.Screen.assemblerBlockScreen;
import com.jumpwatch.tit.Screen.maceratorBlockScreen;
import com.jumpwatch.tit.World.Ore.OreGeneration;
import com.jumpwatch.tit.crafting.recipe.AssemblerRecipes;
import com.jumpwatch.tit.crafting.recipe.CrusherRecipes;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod("theinventorstech")
public class theinventorstech
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "theinventorstech";
    public theinventorstech() {
        LOGGER.info("Starting Registry");
        GeckoLib.initialize();
        theinventorsregistry.register();
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, BlockRegistry::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ItemRegistry::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, BlockRegistry::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, TileentityRegistry::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TagProviders::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetups);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGeneration::generateOres);
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(theinventorstech.this::clientSetup);
        });
    }

    public void commonSetups(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
        theinventorsregistry.init(event);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.register(ContainerRegistry.macerator_container.get(), maceratorBlockScreen::new);
        ScreenManager.register(ContainerRegistry.assembler_container.get(), assemblerBlockScreen::new);
        RenderTypeLookup.setRenderLayer(BlockRegistry.ENERGY_CABLE.getBlock(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.ITEM_CABLE.getBlock(), RenderType.cutout());
        // geckolib Registry
        RenderRegistry.renderRegistry();
        //Other registry stuff.
        TileentityRegistry.clientSetup();
        CrusherRecipes.CrusherRecipes();
        AssemblerRecipes.AssemblerRecipes();
        LOGGER.info("Registered Macerator recipes.");
    }

}
