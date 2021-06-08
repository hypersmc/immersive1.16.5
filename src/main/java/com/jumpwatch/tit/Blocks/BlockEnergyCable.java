package com.jumpwatch.tit.Blocks;

import com.jumpwatch.tit.Tileentity.Cables.TileEntityEnergycable;
import com.jumpwatch.tit.Tileentity.TileEntityEnergyCable;
import com.jumpwatch.tit.theimmersivetech;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.energy.CapabilityEnergy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockEnergyCable extends BlockBaseEnergyCable{
    public BlockEnergyCable() {
        super();
        setRegistryName(new ResourceLocation(theimmersivetech.MOD_ID, "energy_cable"));
    }
    private static final Logger LOGGER = LogManager.getLogger();


    @Override
    public boolean canConnectTo(IWorldReader world, BlockPos pos, Direction facing) {
        TileEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).isPresent());
    }

    @Override
    TileEntity createTileEntity() {
        LOGGER.info("Used TileEntity!");
        return new TileEntityEnergycable();
    }

    @Override
    public boolean isCable(IWorldReader world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos.relative(facing));
        return state.getBlock().equals(this);
    }
}
