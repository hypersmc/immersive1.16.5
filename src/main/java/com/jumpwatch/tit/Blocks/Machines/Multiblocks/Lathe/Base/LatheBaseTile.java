package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Base;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.LatheMBController;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockTile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class LatheBaseTile extends RectangularMultiblockTile<LatheMBController, LatheBaseTile, LatheBaseBlock> {
    public LatheBaseTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Nonnull
    @Override
    public LatheMBController createController() {
        if (level == null) {
            throw new IllegalStateException("Attempt to create controller with null world");
        }
        return new LatheMBController(level);
    }
    public boolean isCurrentController(@Nonnull LatheMBController latheMBController){
        return controller == latheMBController;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return new CompoundNBT();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
    }
}
