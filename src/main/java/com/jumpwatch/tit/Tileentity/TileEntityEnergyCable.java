package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Blocks.BlockBaseEnergyCable;
import com.jumpwatch.tit.Registry.TileentityRegistry;
import com.jumpwatch.tit.Utils.DirectionalPosition;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileEntityEnergyCable extends TileEntity implements ITickable, IEnergyStorage {
    public List rendersides = new IntArrayList();

    Connection connection2;
    getab getab;
    @Nullable
    protected List<Connection> connectionCache;
    protected boolean[] extractingSides;
    protected boolean[] disconnectedSides;
    private int invalidateCountdown;

    public TileEntityEnergyCable() {
        super (TileentityRegistry.Energy_Cable_Base.get());
        extractingSides = new boolean[Direction.values().length];
        disconnectedSides = new boolean[Direction.values().length];
    }

    public List<Connection> getConnections(){
        if (level == null) {
            return new ArrayList<>();
        }
        if (connectionCache == null) {
            updateCache();
            if (connectionCache == null) return new ArrayList<>();
        }
        return connectionCache;
    }

    private void updateCache(){
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof BlockBaseEnergyCable)) {
            connectionCache = null;
            return;
        }
        if (!isExtracting()) {
            connectionCache = null;
            return;
        }

        Map<DirectionalPosition, Integer> connections = new HashMap<>();

        Map<BlockPos, Integer> queue = new HashMap<>();
        List<BlockPos> travelPositions = new ArrayList<>();

        addToQueue(level, worldPosition, queue, travelPositions, connections, 1);

        while (queue.size() > 0) {
            Map.Entry<BlockPos, Integer> blockPosIntegerEntry = queue.entrySet().stream().findAny().get();
            addToQueue(level, blockPosIntegerEntry.getKey(), queue, travelPositions, connections, blockPosIntegerEntry.getValue());
            travelPositions.add(blockPosIntegerEntry.getKey());
            queue.remove(blockPosIntegerEntry.getKey());
        }

        connectionCache = connections.entrySet().stream().map(entry -> new Connection(entry.getKey().getPos(), entry.getKey().getDirection(), entry.getValue())).collect(Collectors.toList());
    }

    public void addToQueue(World world, BlockPos position, Map<BlockPos, Integer> queue, List<BlockPos> travelPositions, Map<DirectionalPosition, Integer> insertPositions, int distance) {
        Block block = world.getBlockState(position).getBlock();
        if (!(block instanceof BlockBaseEnergyCable)) {
            return;
        }
        BlockBaseEnergyCable cable = (BlockBaseEnergyCable) block;
        for (Direction direction : Direction.values()) {
            if (cable.isConnected(world, position, direction)) {
                BlockPos p = position.relative(direction);
                DirectionalPosition dp = new DirectionalPosition(p, direction.getOpposite());
                if (canInsert(position, direction)) {
                    if (!insertPositions.containsKey(dp)) {
                        insertPositions.put(dp, distance);
                    } else {
                        if (insertPositions.get(dp) > distance) {
                            insertPositions.put(dp, distance);
                        }
                    }
                } else {
                    if (!travelPositions.contains(p) && !queue.containsKey(p)) {
                        queue.put(p, distance + 1);
                    }
                }
            }
        }
    }

    public boolean canInsert(BlockPos pos, Direction direction) {
        TileEntity te = level.getBlockEntity(pos);
        if (te instanceof TileEntityEnergyCable) {
            TileEntityEnergyCable cable = (TileEntityEnergyCable) te;
            if (cable.isExtracting(direction)) {
                return false;
            }
        }

        TileEntity tileEntity = level.getBlockEntity(pos.relative(direction));
        if (tileEntity == null) return false;
        if (tileEntity instanceof TileEntityEnergyCable) return false;
        return getab.getcanInsert(tileEntity, direction.getOpposite());
    }



    public boolean isExtracting(Direction side) {
        return extractingSides[side.get3DDataValue()];
    }
    public boolean isExtracting() {
        for (boolean extract : extractingSides) {
            if (extract) {
                return true;
            }
        }
        return false;
    }

    public void setExtracting(Direction side, boolean extract) {
        extractingSides[side.get3DDataValue()] = extract;
        setChanged();
    }
    public boolean isDisconnected(Direction side) {
        return disconnectedSides[side.get3DDataValue()];
    }
    public void setDisconnected(Direction side, boolean disconnected) {
        disconnectedSides[side.get3DDataValue()] = disconnected;
        setChanged();
    }

    public static class Connection {
        private final BlockPos pos;
        private final Direction direction;
        private final int distance;
        public Connection(BlockPos pos, Direction direction, int distance) {
            this.pos = pos;
            this.direction = direction;
            this.distance = distance;
        }
        public BlockPos getPos(){
            return pos;
        }
        public Direction getDirection(){
            return direction;
        }
        public int getDistance(){
            return distance;
        }
        @Override
        public String toString(){
            return "Connection{" + "pos=" + pos + ", direction=" + direction + ", distance=" + distance + "}";
        }

    }

    public abstract static class getab {
        public boolean getcanInsert(TileEntity tileEntity, Direction direction) {
            return canInsert(tileEntity, direction);
        }
        public abstract boolean canInsert(TileEntity tileEntity, Direction direction);

    }
    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getBlockState(), pkt.getTag());
    }

    @Override
    public void tick() {
        if (invalidateCountdown > 0) {
            invalidateCountdown --;
            if (invalidateCountdown <= 0) {
                connectionCache = null;
            }
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
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
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
