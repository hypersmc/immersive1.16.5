package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Blocks.*;
import com.jumpwatch.tit.Blocks.Machines.BlockCrusher;
import com.jumpwatch.tit.Blocks.Machines.BlockElectronicAssembler;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.MinerMultiblock;
import com.jumpwatch.tit.Blocks.Ore.BlockCasseriteOre;
import com.jumpwatch.tit.Blocks.Ore.BlockCopperOre;
import com.jumpwatch.tit.Blocks.Ore.BlockRutileOre;
import com.jumpwatch.tit.crafting.recipe.CrusherRecipes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

    static void register() {}

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {

        return theinventorsregistry.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        theinventorsregistry.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(theinventorsregistry.TIT_Group1)));
        return ret;
    }

    //blocks
    /*
    * Example:
    *
    *   public static final RegistryObject<Block> test_block = register("test_block", () -> new testBlock(AbstractBlock.Properties.of(Material.BAMBOO).dynamicShape()));
    */
    public static final RegistryObject<Block> ElectronicAssembler = register("electronicassembler", () -> new BlockElectronicAssembler(AbstractBlock.Properties.of(Material.METAL).strength(3.5F).noOcclusion().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> Macerator = register("macerator", () -> new BlockCrusher(AbstractBlock.Properties.of(Material.METAL).strength(3.5F).noOcclusion().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> Solar_Panel_T1 = register("solar_panel_t1", () -> new BlockSolar_Panel_T1(AbstractBlock.Properties.of(Material.HEAVY_METAL).noOcclusion().strength(3.8F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> Solar_Panel_T1_SubPanels = register("solar_panel_t1_subpanel", () -> new BlockSolar_Panel_T1_SubPanels(AbstractBlock.Properties.of(Material.HEAVY_METAL).noOcclusion().strength(3.8F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BlockCopperOre = register("copper_ore", () -> new BlockCopperOre(AbstractBlock.Properties.of(Material.METAL).harvestLevel(0).harvestTool(ToolType.PICKAXE).strength(3.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BlockCasseriteOre = register("casserite_ore", () -> new BlockCasseriteOre(AbstractBlock.Properties.of(Material.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE).strength(3.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BlockRutileOre = register("rutile_ore", () -> new BlockRutileOre(AbstractBlock.Properties.of(Material.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE).strength(3.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BlockMinerMultiblock = register("miner_mb", () -> new MinerMultiblock(AbstractBlock.Properties.of(Material.HEAVY_METAL).noOcclusion().strength(3.8F).requiresCorrectToolForDrops()));
    public static final BlockEnergyCable ENERGY_CABLE = new BlockEnergyCable();
    public static final BlockItemCable ITEM_CABLE = new BlockItemCable();
    public static final BlockFluidCable FLUID_CABLE = new BlockFluidCable();
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                ENERGY_CABLE,
                ITEM_CABLE,
                FLUID_CABLE
        );
    }
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ENERGY_CABLE.toItem(),
                ITEM_CABLE.toItem(),
                FLUID_CABLE.toItem()

        );
    }

    public static final class Multiblocks
    {
        public static Block miner;
    }

}
