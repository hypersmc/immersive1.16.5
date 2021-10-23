package com.jumpwatch.tit.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockSolar_Panel_T1_SubPanels extends Block {
    public BlockSolar_Panel_T1_SubPanels(Properties properties) {
        super(properties);
    }
    static final VoxelShape SHAPE = VoxelShapes.or(Block.box(0, 0, 0, 1, 14, 1),
            Block.box(15, 0, 0, 16, 14, 1),
            Block.box(0, 0, 15, 1, 14, 16),
            Block.box(15, 0, 15, 16, 14, 16),
            Block.box(1, 0, 0, 15, 1, 1),
            Block.box(15, 0, 1, 16, 1, 15),
            Block.box(1, 0, 15, 15, 1, 16),
            Block.box(0, 0, 1, 1, 1, 15),
            Block.box(10, 14, 0, 11, 16, 16),
            Block.box(15, 14, 0, 16, 16, 16),
            Block.box(5, 14, 0, 6, 16, 16),
            Block.box(0, 14, 0, 1, 16, 16),
            Block.box(1, 14, 0, 5, 16, 1),
            Block.box(1, 14, 15, 5, 16, 16),
            Block.box(1, 15, 1, 5, 15.5, 15),
            Block.box(6, 14, 0, 10, 16, 1),
            Block.box(6, 14, 15, 10, 16, 16),
            Block.box(6, 15, 1, 10, 15.5, 15),
            Block.box(11, 14, 0, 15, 16, 1),
            Block.box(11, 14, 15, 15, 16, 16),
            Block.box(11, 15, 1, 15, 15.5, 15));
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return super.getCollisionShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
    }
}
