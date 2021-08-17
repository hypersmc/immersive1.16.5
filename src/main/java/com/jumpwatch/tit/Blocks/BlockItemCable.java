package com.jumpwatch.tit.Blocks;

import com.jumpwatch.tit.Tileentity.Cables.TileEntityEnergyCable;
import com.jumpwatch.tit.Tileentity.Cables.TileEntityItemCable;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockItemCable extends BlockBaseCable{

    public BlockItemCable(){
        setRegistryName(new ResourceLocation(theinventorstech.MOD_ID, "item_cable"));


    }
    @Override
    public boolean canConnectTo(IWorldReader world, BlockPos pos, Direction facing) {
        TileEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).isPresent());
    }

    @Override
    TileEntity createTileEntity() {
        return new TileEntityItemCable();
    }

    @Override
    public boolean isCable(IWorldReader world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos.relative(facing));
        return state.getBlock().equals(this);
    }

    @Override
    public ActionResultType onCableSideActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, Direction direction) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof TileEntityEnergyCable && isExtracting(worldIn, pos, direction)) {
            if (worldIn.isClientSide) return ActionResultType.SUCCESS;
            TileEntityItemCable cable = (TileEntityItemCable) tileEntity;
        }
        return super.onCableSideActivated(state, worldIn, pos, player, handIn, hit, direction);
    }
}
