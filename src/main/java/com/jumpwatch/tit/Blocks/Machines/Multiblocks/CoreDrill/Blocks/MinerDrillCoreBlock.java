package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Blocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Tiles.MinerDrillCoreTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MinerDrillCoreBlock extends MinerBaseBlock {

    public MinerDrillCoreBlock(Properties properties){
        super();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MinerDrillCoreTile();
    }

    @Override
    public boolean isGoodForExterior() {
        return false;
    }

    @Override
    public boolean isGoodForInterior() {
        return true;
    }

    @Override
    public boolean isGoodForFrame() {
        return false;
    }

    @Override
    public boolean isGoodForCorner() {
        return false;
    }
}
