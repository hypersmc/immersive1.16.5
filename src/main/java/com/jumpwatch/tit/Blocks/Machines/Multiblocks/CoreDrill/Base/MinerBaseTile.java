package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Base;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.MinerMBController;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockTile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MinerBaseTile extends RectangularMultiblockTile<MinerMBController, MinerBaseTile, MinerBaseBlock> {

    public MinerBaseTile(@Nonnull TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Nonnull
    @Override
    public MinerMBController createController() {
        if (level == null){
            throw new IllegalStateException("Attempt to create controller with null world");
        }
        return new MinerMBController(level);
    }
    public boolean isCurrentController(@Nullable MinerMBController minerMBController) {
        return controller == minerMBController;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return new CompoundNBT();
    }


    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
    }
}
