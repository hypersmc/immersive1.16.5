package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Base;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.LatheMBController;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;

import javax.annotation.Nonnull;

public class LatheBaseBlock extends RectangularMultiblockBlock<LatheMBController, LatheBaseTile, LatheBaseBlock> {
    public static final AbstractBlock.Properties PROPERTIES_SOLID = AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).harvestLevel(2).isValidSpawn((a, b, c, d) -> false).noOcclusion();
    public static final AbstractBlock.Properties PROPERTIES_GLASS = AbstractBlock.Properties.of(Material.METAL).sound(SoundType.GLASS).harvestLevel(2).isValidSpawn((a, b, c, d) -> false).noOcclusion();
    public LatheBaseBlock(){
        this(true);
    }
    public LatheBaseBlock(boolean solid) {
        super(solid ? PROPERTIES_SOLID : PROPERTIES_GLASS);
    }

    public boolean usesLatheState(){
        return false;
    }


    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean isGoodForInterior() {
        return false;
    }

    @Override
    public boolean isGoodForExterior() {
        return true;
    }

    @Override
    public boolean isGoodForFrame() {
        return false;
    }
}
