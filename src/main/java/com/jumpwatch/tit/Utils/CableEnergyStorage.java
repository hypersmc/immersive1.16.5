package com.jumpwatch.tit.Utils;

import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Tileentity.Types.EnergyCableType;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.IEnergyStorage;

public class CableEnergyStorage implements IEnergyStorage {

    protected TileEntityCableLogic cable;
    protected Direction side;
    protected long lastReceived;

    public CableEnergyStorage(TileEntityCableLogic cable, Direction side){
        this.cable = cable;
        this.side = side;
    }

    public void tick(){
        if (cable.getLevel().getGameTime() - lastReceived > 1) {
            EnergyCableType.INSTANCE.pullEnergy(cable, side);
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        lastReceived = cable.getLevel().getGameTime();
        return EnergyCableType.INSTANCE.receive(cable, side, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
