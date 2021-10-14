package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Base.MinerBaseTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.IOnAssemblyTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.IOnDisassemblyTile;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Utils.BlockStates;
import com.jumpwatch.tit.Utils.CustomEnergyStorage;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Blocks.MinerPowerBlock.ConnectionState.*;

public class MinerPowerTile extends MinerBaseTile implements IOnAssemblyTile, IOnDisassemblyTile, ITickable {
    private CustomEnergyStorage energyStorage = createEnergy();
    private LazyOptional<IEnergyStorage> energys = LazyOptional.of(() -> energyStorage);
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerPowerTile::new;
    public static int energyST;
    public int energy = 0;
    public int capacity = 1200;
    public MinerPowerTile() {
        super(TileentityRegistry.Miner_power_tile.get());

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energys.cast();
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
    public int getEnergy(){
        return energyStorage.getEnergyStored();
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("energy", energyStorage.serializeNBT());

        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        energyStorage.deserializeNBT(tag.getCompound("energy"));
        super.load(state, tag);

    }

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(25000, 32) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Override
    public void setEnergyAmount(Integer amount) {
        super.setEnergyAmount(amount);
    }

    @Override
    public int getEnergyAmount() {
        return super.getEnergyAmount();
    }

    @Override
    public void onAssembly() {
        powerOutputDirection = getBlockState().getValue(BlockStates.FACING);
    }

    @Override
    public void onDisassembly() {
        powerOutputDirection = null;
    }

    @Override
    public void tick() {
        if (energyST < energyStorage.getMaxEnergyStored()) {
            energyST += energyStorage.getEnergyStored();
        }else if (energyST <= energyStorage.getMaxEnergyStored()) {
            energyST = energyStorage.getEnergyStored();
        }
//        if (getEnergyAmount() >= energyST) {
//            setEnergyAmount(energyST);
//        }

    }
}
