package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe;

import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockBlock;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractBlock.Properties;

public class LatheMultiblock extends RectangularMultiblockBlock {
    public LatheMultiblock(@Nonnull Properties properties) { super(properties); }
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
