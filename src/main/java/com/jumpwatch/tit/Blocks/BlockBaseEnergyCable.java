package com.jumpwatch.tit.Blocks;

import com.jumpwatch.tit.Tileentity.TileEntityEnergyCable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

public abstract class BlockBaseEnergyCable extends Block {
    static final BooleanProperty has_data = BooleanProperty.create("has_data");
    public static final BooleanProperty down = BooleanProperty.create("down");
    public static final BooleanProperty up = BooleanProperty.create("up");
    public static final BooleanProperty north = BooleanProperty.create("north");
    public static final BooleanProperty south = BooleanProperty.create("south");
    public static final BooleanProperty west = BooleanProperty.create("west");
    public static final BooleanProperty east = BooleanProperty.create("east");
    // .setValue(HAS_DATA, false)
    public BlockBaseEnergyCable(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(stateDefinition.any().setValue(has_data, false).setValue(up, false).setValue(down, false).setValue(north, false).setValue(south, false).setValue(west, false).setValue(east, false));
    }
    public BooleanProperty getProperty(Direction side) {
        switch (side) {
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case EAST:
                return east;
            case WEST:
                return west;
            case UP:
                return up;
            case DOWN:
            default:
                return down;
        }
    }



    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(up, down, north, south, west, east, has_data);
    }

    //Voxels
    static final VoxelShape SHAPE_CORE = VoxelShapes.or(Block.box(5, 5, 5, 11, 11, 11));
    static final VoxelShape SHAPE_UP = VoxelShapes.or(Block.box(6, 11, 6, 10, 16, 10), Block.box(5, 15, 5, 11, 16, 11));
    static final VoxelShape SHAPE_DOWN = VoxelShapes.or(Block.box(6, 0, 6, 10, 5, 10), Block.box(5, 0, 5, 11, 1, 11));
    static final VoxelShape SHAPE_NORTH = VoxelShapes.or(Block.box(6, 6, 1, 10, 10, 5), Block.box(5, 5, 0, 11, 11, 1));
    static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(Block.box(6, 6, 11, 10, 10, 15), Block.box(5, 5, 15, 11, 11, 16));
    static final VoxelShape SHAPE_EAST = VoxelShapes.or(Block.box(1, 6, 6, 5, 10, 10), Block.box(0, 5, 5, 1, 11, 11));
    static final VoxelShape SHAPE_WEST = VoxelShapes.or(Block.box(11, 6, 6, 15, 10, 10), Block.box(15, 5, 5, 16, 11, 11));
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getState(context.getLevel(), context.getClickedPos(), null);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos pos1, boolean b) {
        super.neighborChanged(state, world, pos, block, pos1, b);
        BlockState newState = getState(world, pos, state);
        if (!state.getProperties().stream().allMatch(property -> state.getValue(property).equals(newState.getValue(property)))){
            world.setBlockAndUpdate(pos, newState);
        }
    }

    private BlockState getState(World world, BlockPos pos, @Nullable BlockState oldState) {
        boolean hasdata = false;
        return defaultBlockState()
                .setValue(up, isConnected(world,pos,Direction.UP))
                .setValue(down, isConnected(world,pos,Direction.DOWN))
                .setValue(north, isConnected(world,pos,Direction.NORTH))
                .setValue(south, isConnected(world,pos,Direction.SOUTH))
                .setValue(east, isConnected(world,pos,Direction.EAST))
                .setValue(west, isConnected(world,pos,Direction.WEST))
                .setValue(has_data, hasdata);
    }

    public boolean isExtracting(IWorldReader world, BlockPos pos, Direction side) {
        TileEntityEnergyCable cable = getTileEntity(world, pos);
        if (cable == null) return false;
        return cable.isExtracting(side);
    }
    public boolean isDisconnected(IWorldReader world, BlockPos pos, Direction side) {
        TileEntityEnergyCable cable = getTileEntity(world, pos);
        if (cable == null) return false;
        return cable.isDisconnected(side);
    }

    public void setHasData(World world, BlockPos pos, boolean hasData) {
        BlockState blockState = world.getBlockState(pos);
        world.setBlockAndUpdate(pos, blockState.setValue(has_data, hasData));
    }

    public void setExtracting(World world, BlockPos pos, Direction side, boolean extracting) {
        TileEntityEnergyCable cable = getTileEntity(world, pos);
        if (cable == null) {
            if (extracting) {
                setHasData(world, pos, true);
                cable = getTileEntity(world, pos);
                if (cable != null) {
                    cable.setExtracting(side, extracting);
                }
            }
        } else {
            cable.setExtracting(side, extracting);
            BlockState blockState = world.getBlockState(pos);
            BooleanProperty sideProperty = getProperty(side);
            boolean connected = blockState.getValue(sideProperty);
            world.setBlockAndUpdate(pos, blockState.setValue(sideProperty, !connected));
            world.setBlockAndUpdate(pos, blockState.setValue(sideProperty, connected));
        }
    }
    public void setDisconnected(World world, BlockPos pos, Direction side, boolean disconnected) {
        TileEntityEnergyCable cable = getTileEntity(world, pos);
        if (cable == null) {
            if (disconnected) {
                setHasData(world, pos, true);
                cable = getTileEntity(world, pos);
                if (cable != null) {
                    cable.setDisconnected(side, disconnected);
                    world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(getProperty(side), false));
                }
            }
        } else {
            cable.setDisconnected(side, disconnected);
            world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(getProperty(side), !disconnected));
        }
    }




    public VoxelShape getShape(IBlockReader blockReader, BlockPos pos, BlockState state, boolean advanced) {
        TileEntityEnergyCable cable = null;
        if (advanced && blockReader instanceof IWorldReader) {
            cable = getTileEntity((IWorldReader) blockReader, pos);
        }
        VoxelShape shape = SHAPE_CORE;
        if (state.getValue(up)) {
            shape = VoxelShapes.or(shape, SHAPE_UP);
        }
        if (state.getValue(down)) {
            shape = VoxelShapes.or(shape, SHAPE_DOWN);
        }
        if (state.getValue(north)) {
            shape = VoxelShapes.or(shape, SHAPE_NORTH);
        }
        if (state.getValue(south)) {
            shape = VoxelShapes.or(shape, SHAPE_SOUTH);
        }
        if (state.getValue(west)) {
            shape = VoxelShapes.or(shape, SHAPE_WEST);
        }
        if (state.getValue(east)) {
            shape = VoxelShapes.or(shape, SHAPE_EAST);
        }
        return shape;
    }
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(worldIn, pos, state, true);
    }

    public TileEntityEnergyCable getTileEntity(IWorldReader world, BlockPos pos) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityEnergyCable) {
            return (TileEntityEnergyCable) te;
        }
        return null;
    }



    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getValue(has_data)) {
            return createTileEntity();
        }else{
            return null;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(has_data);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return getShape(worldIn, pos, state, false);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return getShape(reader, pos, state, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(worldIn, pos, state, false);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return getShape(reader, pos, state, false);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return getShape(worldIn, pos, state, false);
    }


    public boolean isConnected(IWorldReader world, BlockPos pos, Direction facing) {
        TileEntityEnergyCable pipe = getTileEntity(world, pos);
        TileEntityEnergyCable other = getTileEntity(world, pos.relative(facing));

        if (!isAbleToConnect(world, pos, facing)) {
            return false;
        }
        boolean canSelfConnect = pipe == null || !pipe.isDisconnected(facing);
        if (!canSelfConnect) {
            return false;
        }
        boolean canSideConnect = other == null || !other.isDisconnected(facing.getOpposite());
        return canSideConnect;
    }
    public boolean isAbleToConnect(IWorldReader world, BlockPos pos, Direction facing) {
        return isCable(world, pos, facing) || canConnectTo(world, pos, facing);
    }
    public abstract boolean canConnectTo(IWorldReader world, BlockPos pos, Direction facing);
    abstract TileEntity createTileEntity();
    public abstract boolean isCable(IWorldReader world, BlockPos pos, Direction facing);




}
