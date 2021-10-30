package com.jumpwatch.tit;

import com.jumpwatch.tit.Multiblockhandeling.generic.MultiblockController;
import com.jumpwatch.tit.Multiblockhandeling.generic.MultiblockTile;
import com.jumpwatch.tit.Registry.*;
import com.jumpwatch.tit.Screen.MinerMBControllerBlockScreen;
import com.jumpwatch.tit.Screen.assemblerBlockScreen;
import com.jumpwatch.tit.Screen.maceratorBlockScreen;
import com.jumpwatch.tit.World.Ore.OreGeneration;
import com.jumpwatch.tit.crafting.recipe.AssemblerRecipes;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@Mod("theinventorstech")
public class theinventorstech
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final boolean devmode = false;
    public static final String MOD_ID = "theinventorstech";
    private static long tick = 0;

    public theinventorstech() {
        if (devmode) {
            LOGGER.warn("WARNING DEV MODE ACTIVATED");
            LOGGER.info("Please report to author!");
        }

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
        ScreenManager.register(ContainerRegistry.minermbcontrollerblock_container.get(), MinerMBControllerBlockScreen::new);
        RenderTypeLookup.setRenderLayer(BlockRegistry.ENERGY_CABLE.getBlock(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.ITEM_CABLE.getBlock(), RenderType.cutout());
        //RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBFrame.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBController.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBItemBlock.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBPowerBlock.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBDrillCore.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBScaffoldingBlock.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockRegistry.BlockMinerMBDrillDrill.get(), RenderType.cutout());
        // geckolib Registry
        RenderRegistry.renderRegistry();
        //Other registry stuff.
        TileentityRegistry.clientSetup();
        //CrusherRecipes.CrusherRecipes();
        AssemblerRecipes.AssemblerRecipes();
        LOGGER.info("Registered Macerator recipes.");
    }


    private static final HashMap<ServerWorld, ArrayList<MultiblockController<?, ?, ?>>> controllersToTick = new HashMap<>();
    private static final HashMap<ServerWorld, ArrayList<MultiblockTile<?, ?, ?>>> tilesToAttach = new HashMap<>();
    private static final ArrayList<MultiblockController<?, ?, ?>> newControllers = new ArrayList<>();
    private static final ArrayList<MultiblockController<?, ?, ?>> oldControllers = new ArrayList<>();
    private static final ArrayList<MultiblockTile<?, ?, ?>> newTiles = new ArrayList<>();
    public static void addController(MultiblockController<?, ?, ?> controller) {
        newControllers.add(controller);
    }

    public static void removeController(MultiblockController<?, ?, ?> controller) {
        oldControllers.add(controller);
    }

    public static void attachTile(MultiblockTile<?, ?, ?> tile) {
        newTiles.add(tile);
    }
    @SubscribeEvent
    void onWorldUnload(final WorldEvent.Unload worldUnloadEvent) {
        if (!worldUnloadEvent.getWorld().isClientSide()) {
            ArrayList<MultiblockController<?, ?, ?>> controllersToTick = theinventorstech.controllersToTick.remove(worldUnloadEvent.getWorld());
            if (controllersToTick != null) {
                for (MultiblockController<?, ?, ?> multiblockController : controllersToTick) {
                    multiblockController.suicide();
                }
            }
            tilesToAttach.remove(worldUnloadEvent.getWorld());
            newControllers.removeIf(multiblockController -> multiblockController.getWorld() == worldUnloadEvent.getWorld());
            oldControllers.removeIf(multiblockController -> multiblockController.getWorld() == worldUnloadEvent.getWorld());
            newTiles.removeIf(multiblockTile -> multiblockTile.getLevel() == worldUnloadEvent.getWorld());
        }
    }
    @SubscribeEvent
    public void advanceTick(TickEvent.ServerTickEvent e) {
        if (!e.side.isServer()) {
            return;
        }
        if (e.phase != TickEvent.Phase.END) {
            return;
        }
        tick++;



        for (MultiblockController<?, ?, ?> newController : newControllers) {
            controllersToTick.computeIfAbsent((ServerWorld) newController.getWorld(), k -> new ArrayList<>()).add(newController);
        }
        newControllers.clear();
        for (MultiblockController<?, ?, ?> oldController : oldControllers) {
            //noinspection SuspiciousMethodCalls
            ArrayList<MultiblockController<?, ?, ?>> controllers = controllersToTick.get(oldController.getWorld());
            controllers.remove(oldController);
        }
        oldControllers.clear();
        for (MultiblockTile<?, ?, ?> newTile : newTiles) {
            tilesToAttach.computeIfAbsent((ServerWorld) newTile.getLevel(), k -> new ArrayList<>()).add(newTile);
        }
        newTiles.clear();
    }
    public static long tickNumber() {
        return tick;
    }
    @SubscribeEvent
    public void tickWorld(TickEvent.WorldTickEvent e) {
        if (!(e.world instanceof ServerWorld)) {
            return;
        }
        if (e.phase != TickEvent.Phase.END) {
            return;
        }

        ArrayList<MultiblockController<?, ?, ?>> controllersToTick = theinventorstech.controllersToTick.get(e.world);
        if (controllersToTick != null) {
            for (MultiblockController<?, ?, ?> controller : controllersToTick) {
                if (controller != null) {
                    controller.update();
                }
            }
        }

        ArrayList<MultiblockTile<?, ?, ?>> tilesToAttach = theinventorstech.tilesToAttach.get(e.world);
        if (tilesToAttach != null) {
            tilesToAttach.sort(Comparator.comparing(TileEntity::getBlockPos));
            for (MultiblockTile<?, ?, ?> toAttach : tilesToAttach) {
                if (toAttach != null) {
                    toAttach.attachToNeighbors();
                }
            }
            tilesToAttach.clear();
        }

    }

}
