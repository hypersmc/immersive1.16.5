package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.MinerControllerTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;


import javax.annotation.Nullable;

public class MinerControllerBlock extends MinerBaseBlock {
    public MinerControllerBlock(Properties properties) {
        super();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MinerControllerTile();
    }

    @Override
    public boolean isGoodForFrame() {
        return true;
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
    public boolean isGoodForCorner() {
        return true;
    }

    @Override
    public boolean usesFaceDirection() {
        return false;
    }

    @Override
    public boolean usesAxisPositions() {
        return true;
    }
}
