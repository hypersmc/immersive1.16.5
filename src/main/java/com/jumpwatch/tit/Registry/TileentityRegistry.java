package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Tileentity.TileEntityElectronic_Crusher;
import com.jumpwatch.tit.Tileentity.TileEntityEnergyCable;
import com.jumpwatch.tit.Tileentity.TileEntitySolar_Panel_T1;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class TileentityRegistry {


    static void register() {}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return theimmersiveregistry.TILE_ENTITIES.register(name, () -> {
            //noinspection ConstantConditions - null in build
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }

    public static final RegistryObject<TileEntityType<TileEntityElectronic_Crusher>> Electronic_crusher = register("electronic_crusher", TileEntityElectronic_Crusher::new, BlockRegistry.Electronic_crusher);
    public static final RegistryObject<TileEntityType<TileEntitySolar_Panel_T1>> Solar_Panel_T1 = register("solar_panel_t1", TileEntitySolar_Panel_T1::new, BlockRegistry.Solar_Panel_T1);
    public static final RegistryObject<TileEntityType<TileEntityEnergyCable>> Energy_Cable_Base = register("energy_cable_b_t1", TileEntityEnergyCable::new, BlockRegistry.Energy_Cable_Base_T1);
}
