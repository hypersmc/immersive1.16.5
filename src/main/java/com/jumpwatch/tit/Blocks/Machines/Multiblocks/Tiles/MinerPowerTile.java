package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.IOnAssemblyTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.IOnDisassemblyTile;
import com.jumpwatch.tit.Utils.BlockStates;
import com.jumpwatch.tit.Utils.CustomEnergyStorage;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MinerPowerBlock.ConnectionState.*;

public class MinerPowerTile extends MinerBaseTile implements IOnAssemblyTile, IOnDisassemblyTile {
    private CustomEnergyStorage energyStorage = createEnergy();
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerPowerTile::new;
    public int energy = 0;
    public int capacity = 1200;
    public MinerPowerTile() {
        super(TYPE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }
    private boolean connected = false;
    Direction powerOutputDirection = null;
    private static final EnergyStorage ENERGY_ZERO = new EnergyStorage(0);

    public void setConnected(boolean newState) {
        if (newState != connected) {
            connected = newState;
            assert level != null;
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CONNECTION_STATE_ENUM_PROPERTY, connected ? CONNECTED : DISCONNECTED));
        }
    }
    LazyOptional<?> inputOptinal = LazyOptional.empty();


    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(capacity, 256) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }



    @Override
    public void onAssembly() {
        powerOutputDirection = getBlockState().getValue(BlockStates.FACING);
    }

    @Override
    public void onDisassembly() {
        powerOutputDirection = null;
    }
}
