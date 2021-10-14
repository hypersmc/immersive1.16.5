package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Blocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Tiles.MinerDrillDrillTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MinerDrillDrillBlock extends MinerBaseBlock {
    public MinerDrillDrillBlock(Properties properties){
        super();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MinerDrillDrillTile();
    }
    @Override
    public boolean isGoodForExterior() {
        return true;
    }

    @Override
    public boolean isGoodForInterior() {
        return true;
    }

    @Override
    public boolean isGoodForFrame() {
        return true;
    }

    @Override
    public boolean isGoodForCorner() {
        return false;
    }
}
