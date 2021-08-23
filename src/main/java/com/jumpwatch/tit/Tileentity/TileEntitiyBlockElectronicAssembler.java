package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.crafting.recipe.AssemblerRecipes;
import com.jumpwatch.tit.crafting.recipe.CrusherRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitiyBlockElectronicAssembler extends TileEntity implements /*IAnimatable,*/ ITickableTileEntity {
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public TileEntitiyBlockElectronicAssembler() {
        super(TileentityRegistry.Block_electronicassembler.get());
    }
    public static int cookTime;
    public boolean isrunning = false;

    @Override
    public void tick() {
        if (canProcess()) processTick();
    }

    public boolean canProcess(){
        // No input
        if(itemHandler.getStackInSlot(0).isEmpty()){
            isrunning = false;
            //Make the progress bar go down if slot is empty and there was progress.
            if (!(cookTime == 0)) {
                isrunning = false;
                cookTime--;
            }
            return false;
        }
        // No recipe
        AssemblerRecipes.AssemblerRecipe recipe = AssemblerRecipes.findRecipe(itemHandler.getStackInSlot(0),itemHandler.getStackInSlot(1),itemHandler.getStackInSlot(2),itemHandler.getStackInSlot(3),itemHandler.getStackInSlot(4),itemHandler.getStackInSlot(5),itemHandler.getStackInSlot(6),itemHandler.getStackInSlot(7),itemHandler.getStackInSlot(8));
        if (recipe == null) return false;
        ItemStack outputSlot = itemHandler.getStackInSlot(9);
        // it is missing
        // Check recipe output matches the content of the output slot
        if (!outputSlot.isEmpty() && !ItemStack.isSame(recipe.getOutput(), outputSlot)) return false;
        // No space
        if(outputSlot.getCount() + recipe.getOutput().getCount() > outputSlot.getMaxStackSize()) return false;
        // Passes all checks
        return true;
    }
    public void processTick(){
        cookTime++;
        System.out.println(cookTime);
        isrunning = true;
        if (cookTime == 100) {
            cookTime = 0;
            isrunning = false;
            doProcess();
        }
    }

    public void doProcess(){
        isrunning = false;
        itemHandler.insertItem(9, AssemblerRecipes.findRecipe(itemHandler.getStackInSlot(0),itemHandler.getStackInSlot(1),itemHandler.getStackInSlot(2),itemHandler.getStackInSlot(3),itemHandler.getStackInSlot(4),itemHandler.getStackInSlot(5),itemHandler.getStackInSlot(6),itemHandler.getStackInSlot(7),itemHandler.getStackInSlot(8)).getOutput(), false);
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);
        itemHandler.extractItem(3, 1, false);
        itemHandler.extractItem(4, 1, false);
        itemHandler.extractItem(5, 1, false);
        itemHandler.extractItem(6, 1, false);
        itemHandler.extractItem(7, 1, false);
        itemHandler.extractItem(8, 1, false);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
    @Override
    public void load(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        super.load(state, tag);
    }
    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        return super.save(tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(11) {

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
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}
