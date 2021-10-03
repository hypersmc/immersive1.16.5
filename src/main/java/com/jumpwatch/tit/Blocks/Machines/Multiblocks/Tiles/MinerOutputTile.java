package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock;
import com.jumpwatch.tit.Multiblockhandeling.generic.IOnAssemblyTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.IOnDisassemblyTile;
import com.jumpwatch.tit.Utils.BlockStates;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock.PortDirection.INLET;
import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock.PortDirection.PORT_DIRECTION_ENUM_PROPERTY;

public class MinerOutputTile extends MinerBaseTile implements IItemHandler, INamedContainerProvider, IOnDisassemblyTile, IOnAssemblyTile {
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerOutputTile::new;

    public MinerOutputTile() {
        super(TYPE);
    }
    private MineItemBlock.PortDirection direction = INLET;


    @Override
    public void onAssembly() {
        assert level != null;
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(PORT_DIRECTION_ENUM_PROPERTY, direction));
        itemOutputDirection = getBlockState().getValue(BlockStates.FACING);
    }

    @Override
    public void onDisassembly() {
        itemOutputDirection = null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.tit.miner.item");

    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }

    @Override
    public int getSlots() {
        return 3;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    Direction itemOutputDirection;
    boolean connected;
    LazyOptional<IItemHandler> itemOutput = LazyOptional.empty();
}
