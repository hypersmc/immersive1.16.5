package com.jumpwatch.tit.Blocks;

import com.jumpwatch.tit.Tileentity.TileEntitySolar_Panel_T1;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSolar_Panel_T1 extends Block {
    public BlockSolar_Panel_T1(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;

        }

        this.interactWith(world, pos, player);
        return ActionResultType.CONSUME;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TileEntitySolar_Panel_T1) {

            TileEntitySolar_Panel_T1 te = (TileEntitySolar_Panel_T1) tileEntity;
            player.sendMessage(new StringTextComponent( te.getEnergyStored() + " energy stored"), player.getUUID());
            player.sendMessage(new StringTextComponent(te.getPowergeneration() + " Current power generation"), player.getUUID());
            player.sendMessage(new StringTextComponent(te.getMaxEnergyStored() + " Max supported energy in panel"), player.getUUID());
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntitySolar_Panel_T1();
    }
    @Override
    public boolean hasTileEntity(BlockState state) {

        return true;
    }
    static final VoxelShape SHAPE = VoxelShapes.or(Block.box(0, 0, 0, 1, 14, 1),
            Block.box(4, 3, 4, 12, 11, 12),
            Block.box(6, 0, 6, 10, 4, 10),
            Block.box(2, 4, 6, 6, 8, 10),
            Block.box(5, 11, 5, 6, 14, 6),
            Block.box(5, 0, 5, 6, 3, 6),
            Block.box(5, 11, 10, 6, 14, 11),
            Block.box(5, 0, 10, 6, 3, 11),
            Block.box(10, 11, 5, 11, 14, 6),
            Block.box(10, 0, 5, 11, 3, 6),
            Block.box(10, 11, 10, 11, 14, 11),
            Block.box(10, 0, 10, 11, 3, 11),
            Block.box(4, 5, 1, 5, 6, 4),
            Block.box(3, 6, 1, 4, 10, 2),
            Block.box(3, 10, 1, 4, 14, 2),
            Block.box(4, 6, 1, 5, 10, 2),
            Block.box(4, 10, 1, 5, 14, 2),
            Block.box(3, 5, 1, 4, 6, 4),
            Block.box(2, 4, 4, 4, 7, 6),
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
