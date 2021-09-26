package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Registry.BlockRegistry;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySolar_Panel_T1 extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private List<Block> structureBlocks = new ArrayList<Block>();
    private static final Logger LOGGER = LogManager.getLogger();
    private int powergeneration = 32;
    private boolean alreadyUpdated = false;
    public int energy = 0;
    public int capacity = 1200;
    private boolean canProducePower;
    private int xStart = 0;
    private int zStart = 0;
    private int xEnd = 0;
    private int zEnd = 0;
    private int solarBlockCount = 0;
    private boolean solarBuilt = false;
    public TileEntitySolar_Panel_T1(){
        super (TileentityRegistry.Solar_Panel_T1.get());
        structureBlocks.add(BlockRegistry.Solar_Panel_T1.get().getBlock());
        structureBlocks.add(BlockRegistry.Solar_Panel_T1_SubPanels.get().getBlock());
    }
    @Override
    public void tick() {
//        BlockPos pos = this.getBlockPos();
//        if (!this.level.isClientSide) {
//            if (level.getGameTime() % 20 == 0) {
//                solarBuilt = checkSolarStructure();
//                if (!alreadyUpdated) {
//                    solarBuilt = checkSolarStructure();
//                    alreadyUpdated = true;
//                }
//                if (canProducePower = (level.canSeeSky(pos.above()) && level.isDay()) && ((!level.isRaining() && !level.isThundering()) || level.getBiome(pos).getPrecipitation().equals("RainType.RAIN")));
//                else
//                    canProducePower = false;
//            }
//            if (alreadyUpdated) {
//                if (canProducePower) {
//                    if (this.energy >= capacity) {
//                        this.energy = capacity;
//                    }else {
//                  l      this.energy += powergeneration;
//                    }
//                }
//            }
//        }
    }


    private boolean checkSolarStructure(){
        boolean isSolar = true;
        boolean hasController = false;
        boolean checking = true;
        BlockPos pos = this.getBlockPos();
        Block b;
        pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        Direction f = null;
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            b = this.level.getBlockState(pos.offset(facing.getNormal())).getBlock();
            if (!structureBlocks.contains(b)){
                LOGGER.info("no structures matched " + b);
                f = facing;
            }else {
                LOGGER.info("structures matched " + b);
            }
        }
        if (f == null) return false;
        f = f.getOpposite();

        checking = true;
        int x1 = pos.getX();
        int z1 = pos.getZ();
        int x2 = 0;
        int z2 = 0;
        int x3 = 0;
        int z3 = 0;
        int x4 = 0;
        int z4 = 0;
        while (checking) {
            pos = pos.offset(f.getNormal());
            LOGGER.info(pos + " dw");
            b = level.getBlockState(pos).getBlock();
            if (structureBlocks.contains(b))
            {
                x2 = pos.getX();
                z2 = pos.getZ();
                LOGGER.info(x2);
                LOGGER.info(z2);
            }
            else
            {
                LOGGER.info("Didn't check x2 and z2");
                checking = false;
            }
        }
        f = f.getClockWise();
        pos = new BlockPos(pos.getX(), this.getBlockPos().getY(), pos.getZ());
        checking = true;
        while (checking)
        {
            pos = pos.offset(f.getNormal());
            b = level.getBlockState(pos).getBlock();
            if (structureBlocks.contains(b))
            {
                x3 = pos.getX();
                z3 = pos.getZ();
                LOGGER.info(x3);
                LOGGER.info(z3);
            }
            else
            {
                LOGGER.info("Didn't check x3 and z3");
                checking = false;
            }
        }
        f = f.getOpposite();
        pos = new BlockPos(pos.getX(), this.getBlockPos().getY(), pos.getZ());
        checking = true;
        while (checking)
        {
            pos = pos.offset(f.getNormal());
            b = level.getBlockState(pos).getBlock();
            if (structureBlocks.contains(b))
            {
                x4 = pos.getX();
                z4 = pos.getZ();
                LOGGER.info(x4);
                LOGGER.info(z4);
            }
            else
            {
                LOGGER.info("Didn't check x4 and z4");
                checking = false;
            }
        }
        xStart = Math.min(Math.min(x1, x2), Math.min(x3, x4));
        xEnd = Math.max(Math.max(x1, x2), Math.max(x3, x4));

        zStart = Math.min(Math.min(z1, z2), Math.min(z3, z4));
        zEnd = Math.max(Math.min(z1, z2), Math.max(z3, z4));

        LOGGER.info(zStart);
        LOGGER.info(xEnd);
        LOGGER.info(zStart);
        LOGGER.info(zEnd);

        solarBlockCount = 0;
        solar_check:
        for (int x = xStart; x <= xEnd; ++x){
            for (int z = zStart; z <= zEnd; ++z)
            {
                LOGGER.info(z + " WHY YOU NO WORK?");
                pos = new BlockPos(x, getBlockPos().getY(), z);
                b = level.getBlockState(pos).getBlock();
                LOGGER.info(pos);
                LOGGER.info(b);
                if (b == BlockRegistry.Solar_Panel_T1.get().getBlock())
                    LOGGER.info("Controller found!");
                hasController = true;
                if (x == xStart || x == xEnd || z == zStart || z == zEnd){
                    if (!structureBlocks.contains(b)){
                        LOGGER.info("No controller or panels found!");
                        LOGGER.info(structureBlocks.listIterator().next().toString() + "1");
                        LOGGER.info(level.getBlockState(this.getBlockPos()).getBlock() + "22");
                        isSolar = false;
                        break solar_check;
                    }else {
                        if (b == BlockRegistry.Solar_Panel_T1_SubPanels.get().getBlock())
                            LOGGER.info("Found: " + solarBlockCount + " solar panel(s) ");
                        ++solarBlockCount;
                    }
                }
            }
        }
        if (!hasController)
            isSolar = false;
        LOGGER.info("I have registered: " + solarBlockCount + " Solar panel(s).");
        int number = solarBlockCount * powergeneration;
        LOGGER.info("this will produce a max of " + number + " RF/T");
        return isSolar;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 32;
    }

    public int getPowergeneration(){
        if (this.energy >= capacity) {
            return 0;
        }else {
            return powergeneration;
        }
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

}
