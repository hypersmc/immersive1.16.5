package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Registry.RecipeRegistry;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Utils.CustomEnergyStorage;
import com.jumpwatch.tit.crafting.recipe.CrusherRecipeNew;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
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

public class TileEntityBlockCrusher extends TileEntity implements IAnimatable, ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private CustomEnergyStorage energyStorage = createEnergy();
    private ItemStackHandler itemHandler = createHandler();
    private NonNullList<ItemStack> items;

    public boolean isrunning = false;
    public int forevery = 78;
    public static int cookTime;
    @Override
    public void tick() {
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        if (this.energyStorage.getEnergyStored() < forevery){
            LOGGER.info("something did not pass!");
            isrunning = false;
            return;
        }
        if (inSlot.isEmpty()) return;
        CrusherRecipeNew recipe = getRecipe();
        if (recipe != null){
            isrunning = true;
            dowork(recipe);
        }else{
            LOGGER.info("no recipe found!");
            stopwork();
        }

//        if (this.energyStorage.getEnergyStored() < forevery){
//            isrunning = false;
//            return;
//        }
//
//        if (canProcess()) {
//            processTick();
//        }
    }
//    public boolean canProcess(){
//        // No input
//        if(itemHandler.getStackInSlot(0).isEmpty()){
//            isrunning = false;
//            //Make the progress bar go down if slot is empty and there was progress.
//            if (!(cookTime == 0)) {
//                isrunning = false;
//                cookTime--;
//            }
//            return false;
//        }
//
//        // No recipe
//        CrusherRecipes.CrusherRecipe recipe = CrusherRecipes.findRecipe(itemHandler.getStackInSlot(0));
//
//        if(recipe == null) return false;
//        ItemStack outputSlot = itemHandler.getStackInSlot(1);
//
//        // it is missing
//        // Check recipe output matches the content of the output slot
//        if (!outputSlot.isEmpty() && !ItemStack.isSame(recipe.getOutput(), outputSlot)) return false;
//
//        // No space
//        if(outputSlot.getCount() + recipe.getOutput().getCount() > outputSlot.getMaxStackSize()) return false;
//        // Passes all checks
//        return true;
//    }
//    public void processTick(){
//        cookTime++;
//        System.out.println(cookTime);
//        isrunning = true;
//        //Spend resources
//        int ener = this.energyStorage.getEnergyStored();
//        ener -= forevery;
//        this.energyStorage.setEnergy(ener);
//        if (cookTime == 100) {
//            cookTime = 0;
//            isrunning = false;
//            doProcess();
//        }
//    }
//    public void doProcess(){
//        isrunning = false;
//        itemHandler.insertItem(1, CrusherRecipes.findRecipe(itemHandler.getStackInSlot(0)).getOutput(), false);
//        itemHandler.insertItem(1, CrusherRecipes.findRecipe(itemHandler.getStackInSlot(0)).getOutput(), false);
//        itemHandler.extractItem(0, 1, false);
//
//    }
    @Nullable
    public CrusherRecipeNew getRecipe() {
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        LOGGER.info("Trying to fetch recipe!"); //this is checking
        if (this.level == null || inSlot.isEmpty()){
            LOGGER.info("slot empty!"); //this is checking
            return null;
        }
        LOGGER.info(level.getRecipeManager().getRecipeFor(RecipeRegistry.CRUSHER_RECIPE_NEW_I_RECIPE_TYPE,new RecipeWrapper(itemHandler), level));
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.CRUSHER_RECIPE_NEW_I_RECIPE_TYPE,new RecipeWrapper(itemHandler), level).orElse(null);
    }
    private ItemStack getWorkOutput(@Nullable CrusherRecipeNew recipe) {

        if (recipe != null){
            return recipe.assemble(new RecipeWrapper(itemHandler));
        }
        return ItemStack.EMPTY;
    }
    private void dowork(CrusherRecipeNew recipe){
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        LOGGER.info("Trying to do work.");
        isrunning = true;
        assert this.level != null;
        ItemStack current = inSlot;
        ItemStack output = getWorkOutput(recipe);
        if (!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();
            if (!ItemStack.matches(current, output) || newCount > output.getMaxStackSize()){
                LOGGER.info("Can't do work. E504");
                stopwork();
                return;
            }
        }
        if (cookTime < 100)  {
            isrunning = true;
            ++cookTime;
        }
        if (cookTime >= 100) {
            isrunning = false;
            finishwork(recipe, current, output);
        }
    }
    private void stopwork() {
        cookTime = 0;
    }
    private void finishwork(CrusherRecipeNew recipe, ItemStack current, ItemStack output){
        ItemStack inSlot = itemHandler.getStackInSlot(0);
        ItemStack outSlot = itemHandler.getStackInSlot(0);
        if (!current.isEmpty()) {
            current.grow(output.getCount());
        }else{
            itemHandler.insertItem(1, output, false);
        }
        cookTime = 0;
        itemHandler.extractItem(0,1, false);
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
