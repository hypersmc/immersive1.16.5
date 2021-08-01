package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Blocks.BlockBaseCable;
import com.jumpwatch.tit.Utils.DirectionalPosition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TileEntityBaseCable extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();
    @Nullable
    protected List<Connection> connectionCache;
    protected boolean[] extractingSides;
    protected boolean[] disconnectedSides;
    private int invalidateCountdown;
    private boolean isFluid = false;
    private boolean isEnergy = false;
    private boolean isItem = false;

    public TileEntityBaseCable(TileEntityType<?> tileEntityTypeIn, boolean isItem, boolean isFluid, boolean isEnergy ) {
        super (tileEntityTypeIn);
        extractingSides = new boolean[Direction.values().length];
        disconnectedSides = new boolean[Direction.values().length];
        this.isEnergy = isEnergy;
        this.isFluid = isFluid;
        this.isItem = isItem;

    }

    public boolean isFluid(){
        return isFluid;
    }
    public boolean isEnergy(){
        return isEnergy;
    }
    public boolean isItem(){
        return isItem;
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
        if (!(blockState.getBlock() instanceof BlockBaseCable)) {
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

    public boolean hasReasonToStay() {
        if (isExtracting()) {
            return true;
        }
        for (boolean disconnected : disconnectedSides) {
            if (disconnected) {
                return true;
            }
        }
        return false;
    }

    public static void markCablesDirty(World world, BlockPos pos) {
        List<BlockPos> travelPositions = new ArrayList<>();
        LinkedList<BlockPos> queue = new LinkedList<>();
        Block block = world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockBaseCable)) {
            return;
        }
        BlockBaseCable baseCable = (BlockBaseCable) block;
        TileEntityBaseCable cableTe = baseCable.getTileEntity(world, pos);
        if (cableTe != null) {
            for (Direction side : Direction.values()) {
                if (cableTe.isExtracting(side)) {
                    if (baseCable.canConnectTo(world,pos,side)) {
                        cableTe.setExtracting(side, false);
                        if (!cableTe.hasReasonToStay()) {
                            baseCable.setHasData(world, pos, false);
                        }
                    }
                }
            }
        }
        travelPositions.add(pos);
        addToDirtyList(world,pos,baseCable,travelPositions,queue);
        while (queue.size() > 0) {
            BlockPos blockPos = queue.removeFirst();
            block = world.getBlockState(blockPos).getBlock();
            if (block instanceof BlockBaseCable) {
                addToDirtyList(world, blockPos, (BlockBaseCable) block, travelPositions, queue);
            }
        }
        for (BlockPos p : travelPositions) {
            TileEntity te = world.getBlockEntity(p);
            if (!(te instanceof TileEntityBaseCable)) continue;
            TileEntityBaseCable cable = (TileEntityBaseCable) te;
            cable.connectionCache = null;
        }
    }

    private static void addToDirtyList(World world, BlockPos pos, BlockBaseCable cableBlock, List<BlockPos> travelPositions, LinkedList<BlockPos> queue) {
        for (Direction direction : Direction.values()) {
            if (cableBlock.isConnected(world, pos, direction)) {
                BlockPos p = pos.relative(direction);
                if (!travelPositions.contains(p) && !queue.contains(p)) {
                    travelPositions.add(p);
                    queue.add(p);
                }
            }
        }

    }

    public void addToQueue(World world, BlockPos position, Map<BlockPos, Integer> queue, List<BlockPos> travelPositions, Map<DirectionalPosition, Integer> insertPositions, int distance) {
        Block block = world.getBlockState(position).getBlock();
        if (!(block instanceof BlockBaseCable)) {
            return;
        }
        BlockBaseCable cable = (BlockBaseCable) block;
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
        if (te instanceof TileEntityBaseCable) {
            TileEntityBaseCable cable = (TileEntityBaseCable) te;
            if (cable.isExtracting(direction)) {
                return false;
            }
        }

        TileEntity tileEntity = level.getBlockEntity(pos.relative(direction));
        if (tileEntity == null) return false;
        if (tileEntity instanceof TileEntityBaseCable) return false;
        return canInsert(tileEntity, direction.getOpposite());
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


    public abstract boolean canInsert(TileEntity tileEntity, Direction direction);
    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getBlockState(), pkt.getTag());
    }
    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
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
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        extractingSides = new boolean[Direction.values().length];
        ListNBT extractingList = compound.getList("ExtractingSides", Constants.NBT.TAG_BYTE);
        if (extractingList.size() >= extractingSides.length) {
            for (int i = 0; i < extractingSides.length; i++) {
                ByteNBT b = (ByteNBT) extractingList.get(i);
                extractingSides[i] = b.getAsByte() != 0;
            }
        }

        disconnectedSides = new boolean[Direction.values().length];
        ListNBT disconnectedList = compound.getList("DisconnectedSides", Constants.NBT.TAG_BYTE);
        if (disconnectedList.size() >= disconnectedSides.length) {
            for (int i = 0; i < disconnectedSides.length; i++) {
                ByteNBT b = (ByteNBT) disconnectedList.get(i);
                disconnectedSides[i] = b.getAsByte() != 0;
            }
        }
        invalidateCountdown = 5;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        ListNBT extractingList = new ListNBT();
        for (boolean extractingSide : extractingSides) {
            extractingList.add(ByteNBT.valueOf(extractingSide));
        }
        compound.put("ExtractingSides", extractingList);

        ListNBT disconnectedList = new ListNBT();
        for (boolean disconnected : disconnectedSides) {
            disconnectedList.add(ByteNBT.valueOf(disconnected));
        }
        compound.put("DisconnectedSides", disconnectedList);
        return super.save(compound);
    }


}
