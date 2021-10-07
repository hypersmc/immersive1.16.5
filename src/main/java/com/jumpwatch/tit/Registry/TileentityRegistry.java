package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.*;
import com.jumpwatch.tit.Multiblockhandeling.generic.MultiblockTile;
import com.jumpwatch.tit.Tileentity.Cables.TileEntityEnergyCable;
import com.jumpwatch.tit.Tileentity.Cables.TileEntityFluidCable;
import com.jumpwatch.tit.Tileentity.Cables.TileEntityItemCable;
import com.jumpwatch.tit.Tileentity.TileEntitiyBlockElectronicAssembler;
import com.jumpwatch.tit.Tileentity.TileEntityBlockCrusher;
import com.jumpwatch.tit.Tileentity.TileEntitySolar_Panel_T1;
import com.jumpwatch.tit.Tileentity.render.EnergyCableRenderer;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.function.Supplier;

public class TileentityRegistry {


    static void register() {}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return theinventorsregistry.TILE_ENTITIES.register(name, () -> {
            //noinspection ConstantConditions - null in build
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }

    public static final RegistryObject<TileEntityType<TileEntitySolar_Panel_T1>> Solar_Panel_T1 = register("solar_panel_t1", TileEntitySolar_Panel_T1::new, BlockRegistry.Solar_Panel_T1);
    public static final RegistryObject<TileEntityType<TileEntityBlockCrusher>> Block_Crusher = register("macerator", TileEntityBlockCrusher::new, BlockRegistry.Macerator);
    public static final RegistryObject<TileEntityType<TileEntitiyBlockElectronicAssembler>> Block_electronicassembler = register("electronicassembler", TileEntitiyBlockElectronicAssembler::new, BlockRegistry.ElectronicAssembler);

    /**
     * Multiblock tiles
     **/

    public static final RegistryObject<TileEntityType<MinerControllerTile>> Miner_controller_tile = register("miner_mb_controller", MinerControllerTile::new, BlockRegistry.BlockMinerMBController);
    public static final RegistryObject<TileEntityType<MinerScaffoldingTile>> Miner_scaffolding_tile = register("miner_mb_scaffolding", MinerScaffoldingTile::new, BlockRegistry.BlockMinerMBScaffoldingBlock);
    public static final RegistryObject<TileEntityType<MinerOutputTile>> Miner_item_tile = register("miner_mb_item", MinerOutputTile::new, BlockRegistry.BlockMinerMBItemBlock);
    public static final RegistryObject<TileEntityType<MinerPowerTile>> Miner_power_tile = register("miner_mb_power", MinerPowerTile::new, BlockRegistry.BlockMinerMBPowerBlock);
    public static final RegistryObject<TileEntityType<MinerDrillCoreTile>> Miner_drillcore_tile = register("miner_mb_drillcore", MinerDrillCoreTile::new, BlockRegistry.BlockMinerMBDrillCore);
    public static final RegistryObject<TileEntityType<MinerDrillDrillTile>> Miner_drilldrill_tile = register("miner_mb_drilldrill", MinerDrillDrillTile::new, BlockRegistry.BlockMinerMBDrillDrill);

    public static TileEntityType<TileEntityEnergyCable> ENERGY_CABLE;
    public static TileEntityType<TileEntityItemCable> ITEM_CABLE;
    public static TileEntityType<TileEntityFluidCable> FLUID_CABLE;

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ENERGY_CABLE = TileEntityType.Builder.of(TileEntityEnergyCable::new, BlockRegistry.ENERGY_CABLE).build(null);
        ENERGY_CABLE.setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "energy_cable"));
        event.getRegistry().register(ENERGY_CABLE);

        ITEM_CABLE = TileEntityType.Builder.of(TileEntityItemCable::new, BlockRegistry.ITEM_CABLE).build(null);
        ITEM_CABLE.setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "item_cable"));
        event.getRegistry().register(ITEM_CABLE);

        FLUID_CABLE = TileEntityType.Builder.of(TileEntityFluidCable::new, BlockRegistry.FLUID_CABLE).build(null);
        FLUID_CABLE.setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "fluid_cable"));
        event.getRegistry().register(FLUID_CABLE);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.bindTileEntityRenderer(ENERGY_CABLE, EnergyCableRenderer::new);
    }


}
