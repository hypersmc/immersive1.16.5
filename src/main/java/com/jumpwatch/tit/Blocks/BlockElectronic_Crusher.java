package com.jumpwatch.tit.Blocks;

import com.jumpwatch.tit.Tileentity.TileEntityElectronic_Crusher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.TextComponentMessageFormatHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockElectronic_Crusher extends Block {
    public BlockElectronic_Crusher(Properties properties) {
        super(properties);
    }
    static final VoxelShape SHAPE = VoxelShapes.or(Block.box(0, 0, -13, 16, 16, -8), Block.box(-13, 0, 0, -8, 16, 16), Block.box(0, 0, 24, 16, 16, 29), Block.box(24, 0, 0, 29, 16, 16), Block.box(-8, 1, -8, 24, 1, 24), Block.box(-8, 9, -8, 24, 9, 24), Block.box(23, 1, -7, 24, 25, 23), Block.box(-7, 1, 23, 23, 25, 24), Block.box(-8, 1, -7, -7, 25, 23), Block.box(-7, 1, -8, 23, 25, -7), Block.box(-11, 0, -10.75, -1, 8, -0.75), Block.box(-10.75, 0, 17, -0.75, 8, 27), Block.box(17, 0, 16.75, 27, 8, 26.75), Block.box(16.75, 0, -11, 26.75, 8, -1), Block.box(24, 24, -7, 25, 26, 23), Block.box(-7, 24, -9, 23, 26, -8), Block.box(-9, 24, -7, -8, 26, 23), Block.box(-7, 24, 24, 23, 26, 25), Block.box(23, 8, 23, 25, 26, 25), Block.box(-9, 8, 23, -7, 26, 25), Block.box(23, 8, -9, 25, 26, -7), Block.box(-9, 8, -9, -7, 26, -7), Block.box(4, 4, -14, 12, 12, -13), Block.box(4, 4, 29, 12, 12, 30), Block.box(-14, 4, 4, -13, 12, 12),
            Block.box(29, 4, 4, 30, 12, 12));

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return super.getCollisionShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
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
        if (tileEntity instanceof TileEntityElectronic_Crusher) {

            TileEntityElectronic_Crusher te = (TileEntityElectronic_Crusher) tileEntity;
            player.sendMessage(new StringTextComponent( te.getEnergyStored() + " energy stored"), player.getUUID());
            NetworkHooks.openGui((ServerPlayerEntity) player, te, (packetBuffer)->{});
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityElectronic_Crusher();
    }
    @Override
    public boolean hasTileEntity(BlockState state) {

        return true;
    }
}
