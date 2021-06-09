package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Tileentity.Cables.TileEntityEnergyCable;
import com.jumpwatch.tit.Tileentity.TileEntityElectronic_Crusher;
import com.jumpwatch.tit.Tileentity.TileEntitySolar_Panel_T1;
import com.jumpwatch.tit.theimmersivetech;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
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

    public static TileEntityType<TileEntityEnergyCable> ENERGY_CABLE;

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ENERGY_CABLE = TileEntityType.Builder.of(TileEntityEnergyCable::new, BlockRegistry.ENERGY_CABLE).build(null);
        ENERGY_CABLE.setRegistryName(new ResourceLocation(theimmersivetech.MOD_ID, "energy_cable"));
        event.getRegistry().register(ENERGY_CABLE);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {

    }

}
