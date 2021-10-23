package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill;

import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockBlock;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractBlock.Properties;

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
