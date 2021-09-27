package com.jumpwatch.tit.Blocks.Machines.Multiblocks;

import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockBlock;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockController;

import javax.annotation.Nonnull;

public class MinerMultiblock extends RectangularMultiblockBlock {
    public MinerMultiblock(@Nonnull Properties properties) {
        super(properties);
    }

    @Override
    public boolean isGoodForInterior() {
        return true;
    }

    @Override
    public boolean isGoodForExterior() {
        return true;
    }

    @Override
    public boolean isGoodForFrame() {
        return true;
    }
}
