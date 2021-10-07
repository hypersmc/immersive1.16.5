package com.jumpwatch.tit.Containers;

import com.jumpwatch.tit.Registry.BlockRegistry;
import com.jumpwatch.tit.Registry.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MinerMBControllerBlockContainer extends Container {
    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    public MinerMBControllerBlockContainer(int windowid, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ContainerRegistry.minermbcontrollerblock_container.get(), windowid);
        tileEntity = world.getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, BlockRegistry.BlockMinerMBController.get());
    }
}
