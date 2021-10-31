package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Registry.RecipeRegistry;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Utils.CustomEnergyStorage;
import com.jumpwatch.tit.crafting.recipe.MaceratorRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TileEntityBlockCrusher extends TileEntity implements IAnimatable, ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    private CustomEnergyStorage energyStorage = createEnergy();
    private ItemStackHandler itemHandler = createHandler();
    public boolean isrunning = false;
    public int forevery = 78;
    public static int cookTime;

    @Override
    public void tick() {
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        if (this.energyStorage.getEnergyStored() < forevery){
//            LOGGER.info("something did not pass!");
            isrunning = false;
            return;
        }
        if (inSlot.isEmpty()) return;
        craft();

    }
    public void craft() {
        Inventory inv = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Optional<MaceratorRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.Macerator_recipe, inv, level);
        recipe.ifPresent(iReci -> {
            ItemStack out = iReci.getOutput();
            if (itemHandler.getStackInSlot(1).equals(out)) return;
            isrunning = true;
            cookTime++;
            System.out.println(cookTime);
            int ener = this.energyStorage.getEnergyStored();
            ener -= forevery;
            this.energyStorage.setEnergy(ener);
            if (level.getGameTime() % 83 == 0){
                isrunning = true;
                check(inv);
            }
        });
    }
    private void check (IInventory inv) {
        Optional<MaceratorRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.Macerator_recipe, inv, level);
        recipe.ifPresent(iRecipe -> {
            LOGGER.info("Recipe found for: " + iRecipe + "!");
            ItemStack output = iRecipe.getOutput();

            craftTheItem(output);
            setChanged();
            cookTime = 0;
        });
    }
    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.insertItem(1, output, false);
        isrunning = false;
    }

    private void stopwork() {
        cookTime = 0;
    }

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(25000, 32) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private final AnimationFactory factory = new AnimationFactory(this);



    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 0;
        if (this.isrunning){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("on", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("off", true));

        }
        return PlayState.CONTINUE;
    }
    public TileEntityBlockCrusher() {
        super(TileentityRegistry.Block_Crusher.get());
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        energyStorage.deserializeNBT(tag.getCompound("energy"));
        cookTime = tag.getInt("counter");

        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("energy", energyStorage.serializeNBT());
        tag.putInt("counter", cookTime);
        return super.save(tag);
    }
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {

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
