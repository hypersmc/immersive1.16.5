package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Tileentity.Types.CableTypes;
import com.jumpwatch.tit.Tileentity.Types.EnergyCableType;
import com.jumpwatch.tit.Tileentity.Types.FluidCableType;
import com.jumpwatch.tit.Tileentity.Types.ItemCableType;
import com.jumpwatch.tit.Utils.CableEnergyStorage;
import com.jumpwatch.tit.Utils.CableItemHandler;
import com.jumpwatch.tit.Utils.CachedMap;
import com.jumpwatch.tit.Utils.FluidHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TileEntityCableLogic extends TileEntityBaseCable {
    private static final Logger LOGGER = LogManager.getLogger();
    protected CableTypes<?>[] types;
    protected final int[][] rrIndex;
    protected CachedMap<Direction, CableEnergyStorage> energyCache;
    public TileEntityCableLogic (TileEntityType<?> tileEntityType, CableTypes<?>[] types, boolean isFluid, boolean isItem, boolean isEnergy) {
        super(tileEntityType, isItem, isFluid, isEnergy);
        this.types = types;
        rrIndex = new int[Direction.values().length][types.length];
        energyCache = new CachedMap<>();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (remove) {
            return super.getCapability(cap, side);
        }

        if (cap == CapabilityEnergy.ENERGY && hasType(EnergyCableType.INSTANCE)) {
            if (side != null && !isExtracting(side)) {
                setExtracting(side, true);
                return LazyOptional.of(() -> energyCache.get(side, () -> new CableEnergyStorage(this, side))).cast();
            }
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasType(FluidCableType.INSTANCE)) {
            if (side == null || !isExtracting(side)) {
                setExtracting(side, true);
                return LazyOptional.of(() -> FluidHandler.INSTANCE).cast();
            }
        } else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && hasType(ItemCableType.INSTANCE)) {
            if (side == null || !isExtracting(side)) {
                setExtracting(side, true);
                return LazyOptional.of(() -> CableItemHandler.INSTANCE).cast();
            }
        }

        return super.getCapability(cap, side);
    }

    public boolean hasType(CableTypes<?> type) {
        for (CableTypes<?> t : types) {
            if (t == type) {
                return true;
            }
        }
        return false;
    }

    public int getRoundRobinIndex(Direction direction, CableTypes<?> cableType) {
        return rrIndex[direction.get3DDataValue()][getIndex(cableType)];
    }

    public void setRoundRobinIndex(Direction direction, CableTypes<?> cableType, int value) {
        rrIndex[direction.get3DDataValue()][getIndex(cableType)] = value;
    }


    public int getPreferredPipeIndex(Direction side) {
        /*for (int i = 0; i < types.length; i++) {
            if (isEnabled(side, types[i])) {
                return i;
            }
        }*/
        return 0;
    }

    public boolean shouldWork(Direction side, CableTypes<?> pipeType) {
        return true;
    }

    public CableTypes<?>[] getCableTypes() {
        return types;
    }

    public int getIndex(CableTypes<?> cableType) {
        for (int i = 0; i < getCableTypes().length; i++) {
            CableTypes<?> type = getCableTypes()[i];
            if (type == cableType) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            return;
        }

        for (CableTypes<?> type : getCableTypes()) {
            type.tick(this);
        }
        if (hasType(EnergyCableType.INSTANCE)) {
            for (Direction side : Direction.values()) {
                if (isExtracting(side)) {
                    CableEnergyStorage energyStorage = energyCache.get(side, () -> new CableEnergyStorage(this, side));
                    energyStorage.tick();
                }
            }

        }
    }


    public boolean canInsert(TileEntity tileEntity, Direction direction) {
        for (CableTypes<?> type : types) {
            if (type.canInsert(tileEntity, direction)) {
                return true;
            }
        }
        return false;
    }
    public List<Connection> getSortedConnections(Direction side, CableTypes pipeType) {
        return getConnections().stream().sorted(Comparator.comparingInt(TileEntityBaseCable.Connection::getDistance)).collect(Collectors.toList());
    }

}
