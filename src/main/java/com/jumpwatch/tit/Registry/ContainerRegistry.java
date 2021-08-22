package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.Containers.assemblerBlockContainer;
import com.jumpwatch.tit.Containers.maceratorBlockContainer;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, theinventorstech.MOD_ID);

    public static void containerRegistry(){
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    public static final RegistryObject<ContainerType<assemblerBlockContainer>> assembler_container = CONTAINERS.register("electronicassembler", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new assemblerBlockContainer(windowId, world, pos, inv, inv.player);
    })));
    public static final RegistryObject<ContainerType<maceratorBlockContainer>> macerator_container = CONTAINERS.register("macerator", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new maceratorBlockContainer(windowId, world, pos, inv, inv.player);
    })));
}
