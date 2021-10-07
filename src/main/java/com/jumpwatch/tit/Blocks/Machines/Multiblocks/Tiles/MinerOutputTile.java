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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock.PortDirection.INLET;
import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock.PortDirection.PORT_DIRECTION_ENUM_PROPERTY;

public class MinerOutputTile extends MinerBaseTile implements INamedContainerProvider, IOnDisassemblyTile, IOnAssemblyTile {
    private ItemStackHandler itemHandler = createHandler();
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerOutputTile::new;

    public MinerOutputTile() {
        super(TYPE);
    }
    private MineItemBlock.PortDirection direction = INLET;


    @Override
    public void onAssembly() {
        assert level != null;
        //level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(PORT_DIRECTION_ENUM_PROPERTY, direction));
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
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack);
            }


            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return super.extractItem(slot, amount, simulate);
            }
        };
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }


    Direction itemOutputDirection;
    boolean connected;
    LazyOptional<IItemHandler> itemOutput = LazyOptional.empty();
}
