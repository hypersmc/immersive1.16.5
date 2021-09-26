package com.jumpwatch.tit.Registry;
import com.jumpwatch.tit.Multiblockhandeling.MultiblockHandler;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class theinventorsregistry {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = create(ForgeRegistries.CONTAINERS);
    public static final DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = create(ForgeRegistries.RECIPE_SERIALIZERS);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = create(ForgeRegistries.TILE_ENTITIES);

    public static final ItemGroup TIT_Group1 = new ItemGroup("TITBlocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.Solar_Panel_T1_SubPanels.get());
        }
    };

    public static void register(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        ContainerRegistry.containerRegistry();
        ITEMS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        ItemRegistry.register();
        BlockRegistry.register();
        TileentityRegistry.register();
        RecipeRegistry.register();
        ContainerRegistryTypes.register();
        ITMultiblocks.init();

    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, theinventorstech.MOD_ID);
    }

    public static void init(ParallelDispatchEvent ev) {
        LOGGER.info("Registering Multiblocks");
        MultiblockHandler.registerMultiblock(ITMultiblocks.MINER);
    }
}
