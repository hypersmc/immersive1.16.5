package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Blocks.BlockBaseEnergyCable;
import com.jumpwatch.tit.Blocks.BlockElectronic_Crusher;
import com.jumpwatch.tit.Blocks.BlockSolar_Panel_T1;
import com.jumpwatch.tit.Blocks.BlockSolar_Panel_T1_SubPanels;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

    static void register() {}

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {

        return theimmersiveregistry.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        theimmersiveregistry.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
        return ret;
    }

    //blocks
    /*
    * Example:
    *
    *   public static final RegistryObject<Block> test_block = register("test_block", () -> new testBlock(AbstractBlock.Properties.of(Material.BAMBOO).dynamicShape()));
    */
    public static final RegistryObject<Block> Electronic_crusher = register("electronic_crusher", () -> new BlockElectronic_Crusher(AbstractBlock.Properties.of(Material.METAL)));
    public static final RegistryObject<Block> Solar_Panel_T1 = register("solar_panel_t1", () -> new BlockSolar_Panel_T1(AbstractBlock.Properties.of(Material.HEAVY_METAL).noOcclusion()));
    public static final RegistryObject<Block> Solar_Panel_T1_SubPanels = register("solar_panel_t1_subpanel", () -> new BlockSolar_Panel_T1_SubPanels(AbstractBlock.Properties.of(Material.HEAVY_METAL).noOcclusion()));
    public static final RegistryObject<Block> Energy_Cable_Base_T1 = register("energy_cable_b_t1", () -> new BlockBaseEnergyCable(AbstractBlock.Properties.of(Material.HEAVY_METAL).noOcclusion()));
}
