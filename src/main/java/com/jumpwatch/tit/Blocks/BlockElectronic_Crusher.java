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
