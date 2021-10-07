package com.jumpwatch.tit.Multiblockhandeling.generic;

import com.jumpwatch.tit.theinventorstech;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import com.jumpwatch.tit.Multiblockhandeling.generic.Validator;
import javax.annotation.Nonnull;

import static  com.jumpwatch.tit.Multiblockhandeling.generic.MultiblockBlock.ASSEMBLED;

public abstract class MultiblockTile<ControllerType extends MultiblockController<ControllerType, TileType, BlockType>, TileType extends MultiblockTile<ControllerType, TileType, BlockType>, BlockType extends MultiblockBlock<ControllerType, TileType, BlockType>> extends TileEntity {
    protected ControllerType controller;

    public TileType self() {
        //noinspection unchecked
        return (TileType) this;
    }

    long lastSavedTick = 0;

    public void attemptAttach() {
        controller = null;
        attemptAttach = true;
        assert level != null;
        if (!level.isClientSide) {
            theinventorstech.attachTile(this);
        }
    }

    private boolean attemptAttach = true;
    private boolean allowAttach = true;
    boolean isSaveDelegate = false;

    public MultiblockTile(@Nonnull TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void attachToNeighbors() {
        assert level != null;
        if (allowAttach && attemptAttach && !level.isClientSide) {
            attemptAttach = false;
            Block thisBlock = this.getBlockState().getBlock();
            if (!(thisBlock instanceof MultiblockBlock)) {
                // can happen if a block is broken in the same tick it is placed
                return;
            }
            if (((MultiblockBlock<?, ?, ?>) thisBlock).usesAssemblyState()) {
                level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ASSEMBLED, false));
            }
            if (controller != null) {
                controller.detach(self());
                controller = null;
            }
            // at this point, i need to get or create a controller
            BlockPos.Mutable possibleTilePos = new BlockPos.Mutable();
            for (Direction value : Direction.values()) {
                possibleTilePos.set(getBlockPos());
                possibleTilePos.move(value);
                IChunk chunk = level.getChunk(possibleTilePos.getX() >> 4, possibleTilePos.getZ() >> 4, ChunkStatus.FULL, false);
                if (chunk != null) {
                    TileEntity possibleTile = chunk.getBlockEntity(possibleTilePos);
                    if (possibleTile instanceof MultiblockTile) {
                        if (((MultiblockTile<?, ?, ?>) possibleTile).controller != null) {
                            ((MultiblockTile<?, ?, ?>) possibleTile).controller.attemptAttach(this);
                        } else {
                            ((MultiblockTile<?, ?, ?>) possibleTile).attemptAttach = true;
                        }
                    }

                }
            }
            if (controller == null) {
                createController().attemptAttach(self());
            }
        }
    }


    public void validate() {
        validate();
        attemptAttach();
        if (level.isClientSide) {
            controllerData = null;
        }
    }

    @Override
    public void onLoad() {
        attemptAttach();
    }

    public final void remove() {
        if (controller != null) {
            controller.detach(self());
        }
        allowAttach = false;
        onRemoved(false);
        remove();
    }


    @Override
    public final void onChunkUnloaded() {
        if (controller != null) {
            controller.detach(self(), true);
        }
        allowAttach = false;
        onRemoved(true);
        super.onChunkUnloaded();
    }

    public void onRemoved(boolean chunkUnload) {
    }

    @Nonnull
    public abstract ControllerType createController();

    protected void readNBT(@Nonnull CompoundNBT compound) {
    }

    @Nonnull
    protected CompoundNBT writeNBT() {
        return new CompoundNBT();
    }

    boolean preExistingBlock = false;
    CompoundNBT controllerData = null;



    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        if (compound.contains("controllerData")) {
            controllerData = compound.getCompound("controllerData");
        }
        if (compound.contains("userdata")) {
            readNBT(compound.getCompound("userdata"));
        }
        isSaveDelegate = compound.getBoolean("isSaveDelegate");
        preExistingBlock = true;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if (isSaveDelegate && controller != null && controller.blocks.containsTile(self())) {
            compound.put("controllerData", controller.getNBT());
        }
        compound.put("userdata", writeNBT());
        compound.putBoolean("isSaveDelegate", isSaveDelegate);
        return super.save(compound);
    }


    protected String getDebugInfo() {
        return controller.getDebugInfo();
    }

    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull PlayerEntity player, @Nonnull Hand handIn) {

        if (handIn == Hand.MAIN_HAND) {
            if (player.getMainHandItem() == ItemStack.EMPTY && (!((MultiblockBlock) getBlockState().getBlock()).usesAssemblyState() || !getBlockState().getValue(ASSEMBLED))) {
                if (controller != null && controller.assemblyState() != MultiblockController.AssemblyState.ASSEMBLED) {
                    if (controller.lastValidationError != null) {
                        player.sendMessage(controller.lastValidationError.getTextComponent(), Util.NIL_UUID);
                    } else {
                        player.sendMessage(new TranslationTextComponent("multiblock.error.theinventorstech.unknown"), Util.NIL_UUID);
                    }

                }
                return ActionResultType.SUCCESS;

            } else if (player.getMainHandItem().getItem() == Items.STICK) {
                // no its not getting translated, its debug info, *english*
                if (controller != null) {
                    player.sendMessage(new StringTextComponent(getDebugInfo()), Util.NIL_UUID);
                } else if (!level.isClientSide) {
                    player.sendMessage(new StringTextComponent("null controller on server"), Util.NIL_UUID);
                }
                return ActionResultType.SUCCESS;

            }

            }
            return ActionResultType.PASS;
        }

        protected BlockState assembledBlockState () {
            BlockState state = getBlockState();
            //noinspection unchecked
            if (((BlockType) state.getBlock()).usesAssemblyState()) {
                state = state.setValue(MultiblockBlock.ASSEMBLED, true);
            }
            return state;
        }

        protected BlockState disassembledBlockState () {
            BlockState state = getBlockState();
            //noinspection unchecked
            if (((BlockType) state.getBlock()).usesAssemblyState()) {
                state = state.setValue(MultiblockBlock.ASSEMBLED, false);
            }
            return state;
        }
    }

