package com.jumpwatch.tit.Tileentity.Types;


import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Tileentity.TileEntityBaseCable;
import com.jumpwatch.tit.Utils.EnergyUtils;
import com.jumpwatch.tit.Utils.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnergyCableType extends CableTypes<Void>{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final EnergyCableType INSTANCE = new EnergyCableType();

    @Override
    public String getKey() {
        return "Energy";
    }

    @Override
    public void tick(TileEntityCableLogic tileEntity) {

    }

    @Override
    public boolean canInsert(TileEntity tileEntity, Direction direction) {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY, direction).isPresent();
    }

    @Override
    public String getTranslationKey() {
        return "tooltip.cable.energytip";
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }
    public void pullEnergy(TileEntityCableLogic tileEntity, Direction side) {
        if (!tileEntity.isExtracting(side)) {
            return;
        }
        if (!tileEntity.shouldWork(side, this)) {
            return;
        }
        IEnergyStorage energyStorage = getEnergyStorage(tileEntity, tileEntity.getBlockPos().relative(side), side.getOpposite());
        if (energyStorage == null || !energyStorage.canExtract()) {
            return;
        }

        List<TileEntityBaseCable.Connection> connections = tileEntity.getSortedConnections(side, this);

        insertOrdered(tileEntity, side, connections, energyStorage);
    }




    public int receive(TileEntityCableLogic tileEntity, Direction side, int amount, boolean simulate) {
        if (!tileEntity.isExtracting(side)) {
            return 0;
        }
        if (!tileEntity.shouldWork(side, this)) {
            return 0;
        }

        List<TileEntityBaseCable.Connection> connections = tileEntity.getSortedConnections(side, this);

        int maxTransfer = Math.min(getRate(), amount);

        return receiveOrdered(tileEntity, side, connections, maxTransfer, simulate);
    }
    protected void insertEqually(TileEntityCableLogic tileEntity, Direction side, List<TileEntityBaseCable.Connection> connections, IEnergyStorage energyStorage) {
        if (connections.isEmpty()) {
            return;
        }
        int completeAmount = getRate();
        int energyToTransfer = completeAmount;

        int p = tileEntity.getRoundRobinIndex(side, this) % connections.size();

        List<IEnergyStorage> destinations = new ArrayList<>(connections.size());
        for (int i = 0; i < connections.size(); i++) {
            int index = (i + p) % connections.size();

            TileEntityBaseCable.Connection connection = connections.get(index);
            IEnergyStorage destination = getEnergyStorage(tileEntity, connection.getPos(), connection.getDirection());
            if (destination != null && destination.canReceive() && destination.receiveEnergy(1, true) >= 1) {
                destinations.add(destination);
            }
        }

        for (IEnergyStorage destination : destinations) {
            int simulatedExtract = energyStorage.extractEnergy(Math.min(Math.max(completeAmount / destinations.size(), 1), energyToTransfer), true);
            if (simulatedExtract > 0) {
                int transferred = EnergyUtils.pushEnergy(energyStorage, destination, simulatedExtract);
                if (transferred > 0) {
                    energyToTransfer -= transferred;
                }
            }

            p = (p + 1) % connections.size();

            if (energyToTransfer <= 0) {
                break;
            }
        }

        tileEntity.setRoundRobinIndex(side, this, p);
    }
    protected int receiveEqually(TileEntityCableLogic tileEntity, Direction side, List<TileEntityBaseCable.Connection> connections, int maxReceive, boolean simulate) {
        if (connections.isEmpty() || maxReceive <= 0) {
            return 0;
        }
        int actuallyTransferred = 0;
        int energyToTransfer = maxReceive;
        int p = tileEntity.getRoundRobinIndex(side, this) % connections.size();

        List<Pair<IEnergyStorage, Integer>> destinations = new ArrayList<>(connections.size());
        for (int i = 0; i < connections.size(); i++) {
            int index = (i + p) % connections.size();

            TileEntityBaseCable.Connection connection = connections.get(index);
            IEnergyStorage destination = getEnergyStorage(tileEntity, connection.getPos(), connection.getDirection());
            if (destination != null && destination.canReceive() && destination.receiveEnergy(1, true) >= 1) {
                destinations.add(new Pair<>(destination, index));
            }
        }

        for (Pair<IEnergyStorage, Integer> destination : destinations) {
            int maxTransfer = Math.min(Math.max(maxReceive / destinations.size(), 1), energyToTransfer);
            int extracted = destination.getKey().receiveEnergy(Math.min(maxTransfer, maxReceive), simulate);
            if (extracted > 0) {
                energyToTransfer -= extracted;
                actuallyTransferred += extracted;
            }

            p = destination.getValue() + 1;

            if (energyToTransfer <= 0) {
                break;
            }
        }

        if (!simulate) {
            tileEntity.setRoundRobinIndex(side, this, p);
        }

        return actuallyTransferred;
    }
    protected void insertOrdered(TileEntityCableLogic tileEntity, Direction side, List<TileEntityBaseCable.Connection> connections, IEnergyStorage energyStorage) {
        int energyToTransfer = getRate();

        for (TileEntityBaseCable.Connection connection : connections) {
            if (energyToTransfer <= 0) {
                break;
            }
            IEnergyStorage destination = getEnergyStorage(tileEntity, connection.getPos(), connection.getDirection());
            if (destination == null || !destination.canReceive()) {
                continue;
            }

            int simulatedExtract = energyStorage.extractEnergy(energyToTransfer, true);
            if (simulatedExtract > 0) {
                int extract = EnergyUtils.pushEnergy(energyStorage, destination, simulatedExtract);
                energyToTransfer -= extract;
            }
        }
    }
    protected int receiveOrdered(TileEntityCableLogic tileEntity, Direction side, List<TileEntityBaseCable.Connection> connections, int maxReceive, boolean simulate) {
        int actuallyTransferred = 0;
        int energyToTransfer = maxReceive;

        for (TileEntityBaseCable.Connection connection : connections) {
            if (energyToTransfer <= 0) {
                break;
            }
            IEnergyStorage destination = getEnergyStorage(tileEntity, connection.getPos(), connection.getDirection());
            if (destination == null || !destination.canReceive()) {
                continue;
            }

            int extracted = destination.receiveEnergy(Math.min(energyToTransfer, maxReceive), simulate);
            energyToTransfer -= extracted;
            actuallyTransferred += extracted;
        }
        return actuallyTransferred;
    }
    private boolean hasNotInserted(boolean[] inventoriesFull) {
        for (boolean b : inventoriesFull) {
            if (!b) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private IEnergyStorage getEnergyStorage(TileEntityCableLogic tileEntity, BlockPos pos, Direction direction) {
        TileEntity te = tileEntity.getLevel().getBlockEntity(pos);
        if (te == null) {
            return null;
        }
        return te.getCapability(CapabilityEnergy.ENERGY, direction).orElse(null);
    }
    public int getRate() {
        return 51200;
    }
}
