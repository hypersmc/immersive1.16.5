package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.MinerScaffoldingTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MinerScaffoldingBlock extends MinerBaseBlock {
    public static MinerScaffoldingBlock INSTANCE;

    public MinerScaffoldingBlock(Properties properties){
        super();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MinerScaffoldingTile();
    }

    @Override
    public boolean isGoodForFrame() {
        return true;
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
    public boolean isGoodForCorner() {
        return true;
    }

}
