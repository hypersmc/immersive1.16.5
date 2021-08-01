package com.jumpwatch.tit.Blocks;

import com.jumpwatch.tit.Items.ItemWrench;
import com.jumpwatch.tit.Tileentity.TileEntityBaseCable;
import com.jumpwatch.tit.Utils.IItemBlock;
import com.jumpwatch.tit.Utils.Pair;
import com.jumpwatch.tit.Utils.Triple;
import com.jumpwatch.tit.Utils.VoxelUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.EntitySelectionContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


public abstract class BlockBaseCable extends Block implements IItemBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    static final BooleanProperty has_data = BooleanProperty.create("has_data");
    public static final BooleanProperty down = BooleanProperty.create("down");
    public static final BooleanProperty up = BooleanProperty.create("up");
    public static final BooleanProperty north = BooleanProperty.create("north");
    public static final BooleanProperty south = BooleanProperty.create("south");
    public static final BooleanProperty west = BooleanProperty.create("west");
    public static final BooleanProperty east = BooleanProperty.create("east");
    //Voxels
    public static final VoxelShape SHAPE_CORE = VoxelShapes.or(Block.box(5, 5, 5, 11, 11, 11));
    public static final VoxelShape SHAPE_UP = VoxelShapes.or(Block.box(6, 11, 6, 10, 16, 10));
    public static final VoxelShape SHAPE_DOWN = VoxelShapes.or(Block.box(6, 0, 6, 10, 5, 10));
    public static final VoxelShape SHAPE_NORTH = VoxelShapes.or(Block.box(6, 6, 1, 10, 10, 5));
    public static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(Block.box(6, 6, 11, 10, 10, 15));
    public static final VoxelShape SHAPE_WEST = VoxelShapes.or(Block.box(1, 6, 6, 5, 10, 10));
    public static final VoxelShape SHAPE_EAST = VoxelShapes.or(Block.box(11, 6, 6, 15, 10, 10));

    public static final VoxelShape SHAPE_EXTRACT_UP = VoxelUtils.combine(SHAPE_UP, Block.box(5, 15, 5, 11, 16, 11));
    public static final VoxelShape SHAPE_EXTRACT_DOWN = VoxelUtils.combine(SHAPE_DOWN, Block.box(5, 0, 5, 11, 1, 11));
    public static final VoxelShape SHAPE_EXTRACT_NORTH = VoxelUtils.combine(SHAPE_NORTH, Block.box(5, 5, 0, 11, 11, 1));
    public static final VoxelShape SHAPE_EXTRACT_SOUTH = VoxelUtils.combine(SHAPE_SOUTH, Block.box(5, 5, 15, 11, 11, 16));
    public static final VoxelShape SHAPE_EXTRACT_EAST = VoxelUtils.combine(SHAPE_EAST, Block.box(15, 5, 5, 16, 11, 11));
    public static final VoxelShape SHAPE_EXTRACT_WEST = VoxelUtils.combine(SHAPE_WEST,  Block.box(0, 5, 5, 1, 11, 11));

    protected BlockBaseCable() {
        super(Block.Properties.of(Material.METAL).strength(0.5F).harvestLevel(3));
        registerDefaultState(stateDefinition.any().setValue(has_data, false).setValue(up, false).setValue(down, false).setValue(north, false).setValue(south, false).setValue(west, false).setValue(east, false));
    }
    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)).setRegistryName(getRegistryName());
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.BLOCK;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        Direction side = getSelection(state, worldIn, pos, player).getKey();
        if (side != null) {
            return onCableSideActivated(state, worldIn, pos, player, handIn, hit, side);
        } else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    public ActionResultType onWrenchClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, Direction side) {
        if (side != null) {
            if (worldIn.getBlockState(pos.relative(side)).getBlock() != this) {
                boolean extracting = isExtracting(worldIn, pos, side);
                if (extracting) {
                    LOGGER.info("Setting to be disconnected");
                    setExtracting(worldIn, pos, side, false);
                    setDisconnected(worldIn, pos, side, true);
                } else {
                    LOGGER.info("Setting to be connected and extracting");
                    setExtracting(worldIn, pos, side, true);
                    setDisconnected(worldIn, pos, side, false);
                }
            } else {
                LOGGER.info("Setting to be disconnected");
                setDisconnected(worldIn, pos, side, true);
            }
        } else {
            // Core
            side = hit.getDirection();
            if (worldIn.getBlockState(pos.relative(side)).getBlock() != this) {
                setExtracting(worldIn, pos, side, false);
                if (isAbleToConnect(worldIn, pos, side)) {
                    setDisconnected(worldIn, pos, side, false);
                }
            } else {
                setDisconnected(worldIn, pos, side, false);
                setDisconnected(worldIn, pos.relative(side), side.getOpposite(), false);
            }
        }
        TileEntityBaseCable.markCablesDirty(worldIn, pos);
        return ActionResultType.SUCCESS;
    }

    public VoxelShape getSelectionShape(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        Pair<Direction, VoxelShape> selection = getSelection(state, world, pos, player);

        if (selection.getKey() == null) {
            return getShape(world, pos, state, true);
        }

        return selection.getValue();
    }

    private static final List<Triple<VoxelShape, BooleanProperty, Direction>> SHAPES = Arrays.asList(
            new Triple<>(SHAPE_NORTH, north, Direction.NORTH),
            new Triple<>(SHAPE_SOUTH, south, Direction.SOUTH),
            new Triple<>(SHAPE_WEST, west, Direction.WEST),
            new Triple<>(SHAPE_EAST, east, Direction.EAST),
            new Triple<>(SHAPE_UP, up, Direction.UP),
            new Triple<>(SHAPE_DOWN, down, Direction.DOWN)
    );

    private static final List<Pair<VoxelShape, Direction>> EXTRACT_SHAPES = Arrays.asList(
            new Pair<>(SHAPE_EXTRACT_NORTH, Direction.NORTH),
            new Pair<>(SHAPE_EXTRACT_SOUTH, Direction.SOUTH),
            new Pair<>(SHAPE_EXTRACT_WEST, Direction.WEST),
            new Pair<>(SHAPE_EXTRACT_EAST, Direction.EAST),
            new Pair<>(SHAPE_EXTRACT_UP, Direction.UP),
            new Pair<>(SHAPE_EXTRACT_DOWN, Direction.DOWN)
    );

    public Pair<Direction, VoxelShape> getSelection(BlockState state, IBlockReader blockReader, BlockPos pos, PlayerEntity player) {
        Vector3d start = player.getEyePosition(1F);
        Vector3d end = start.add(player.getLookAngle().normalize().scale(getBlockReachDistance(player)));

        Direction direction = null;
        VoxelShape selection = null;
        double shortest = Double.MAX_VALUE;

        double d = checkShape(state, blockReader, pos, start, end, SHAPE_CORE, null);
        if (d < shortest) {
            shortest = d;
        }

        if (!(blockReader instanceof IWorldReader)) {
            return new Pair<>(direction, selection);
        }

        TileEntityBaseCable pipe = getTileEntity((IWorldReader) blockReader, pos);

        for (int i = 0; i < Direction.values().length; i++) {
            Pair<VoxelShape, Direction> extract = EXTRACT_SHAPES.get(i);
            Triple<VoxelShape, BooleanProperty, Direction> shape = SHAPES.get(i);
            if (pipe != null && pipe.isExtracting(extract.getValue())) {
                d = checkShape(state, blockReader, pos, start, end, extract.getKey(), pipe, extract.getValue());
                if (d < shortest) {
                    shortest = d;
                    direction = extract.getValue();
                    selection = extract.getKey();
                }
            } else {
                d = checkShape(state, blockReader, pos, start, end, shape.getValue1(), shape.getValue2());
                if (d < shortest) {
                    shortest = d;
                    direction = shape.getValue3();
                    selection = shape.getValue1();
                }
            }
        }
        return new Pair<>(direction, selection);
    }

    public float getBlockReachDistance(PlayerEntity player) {
        float distance = (float) player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        return player.isCreative() ? distance : distance - 0.5F;
    }

    private double checkShape(BlockState state, IBlockReader world, BlockPos pos, Vector3d start, Vector3d end, VoxelShape shape, BooleanProperty direction) {
        if (direction != null && !state.getValue(direction)) {
            return Double.MAX_VALUE;
        }
        BlockRayTraceResult blockRayTraceResult = world.clipWithInteractionOverride(start, end, pos, shape, state);
        if (blockRayTraceResult == null) {
            return Double.MAX_VALUE;
        }
        return blockRayTraceResult.getLocation().distanceTo(start);
    }

    private double checkShape(BlockState state, IBlockReader world, BlockPos pos, Vector3d start, Vector3d end, VoxelShape shape, @Nullable TileEntityBaseCable cable, Direction side) {
        if (cable != null && !cable.isExtracting(side)) {
            return Double.MAX_VALUE;
        }
        BlockRayTraceResult blockRayTraceResult = world.clipWithInteractionOverride(start, end, pos, shape, state);
        if (blockRayTraceResult == null) {
            return Double.MAX_VALUE;
        }
        return blockRayTraceResult.getLocation().distanceTo(start);
    }


    public ActionResultType onCableSideActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, Direction direction) {
        return super.use(state, worldIn, pos, player, handIn, hit);
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


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getState(context.getLevel(), context.getClickedPos(), null);
    }

    public ActionResultType onCableSideForceActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, @Nullable Direction side){

        return onWrenchClicked(state, world, pos, player, hand, hit, side);

    }
    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) {
            if (!newState.getValue(has_data)) {
                worldIn.removeBlockEntity(pos);
            }
        } else {
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos pos1, boolean b) {
        super.neighborChanged(state, world, pos, block, pos1, b);
        BlockState newState = getState(world, pos, state);
        if (!state.getProperties().stream().allMatch(property -> state.getValue(property).equals(newState.getValue(property)))){
            world.setBlockAndUpdate(pos, newState);
            TileEntityBaseCable.markCablesDirty(world, pos);
        }
    }

    private BlockState getState(World world, BlockPos pos, @Nullable BlockState oldState) {

        boolean hasData = false;
        if (oldState != null && oldState.getBlock() == this) {
            hasData = oldState.getValue(has_data);
        }
        return defaultBlockState()
                .setValue(up, isConnected(world,pos,Direction.UP))
                .setValue(down, isConnected(world,pos,Direction.DOWN))
                .setValue(north, isConnected(world,pos,Direction.NORTH))
                .setValue(south, isConnected(world,pos,Direction.SOUTH))
                .setValue(east, isConnected(world,pos,Direction.EAST))
                .setValue(west, isConnected(world,pos,Direction.WEST))
                .setValue(has_data, hasData);
    }

    public boolean isExtracting(IWorldReader world, BlockPos pos, Direction side) {
        TileEntityBaseCable cable = getTileEntity(world, pos);
        if (cable == null) return false;
        return cable.isExtracting(side);
    }
    public boolean isDisconnected(IWorldReader world, BlockPos pos, Direction side) {
        TileEntityBaseCable cable = getTileEntity(world, pos);
        if (cable == null) return false;
        return cable.isDisconnected(side);
    }

    public void setHasData(World world, BlockPos pos, boolean hasData) {
        BlockState blockState = world.getBlockState(pos);
        world.setBlockAndUpdate(pos, blockState.setValue(has_data, hasData));
    }

    public void setExtracting(World world, BlockPos pos, Direction side, boolean extracting) {
        TileEntityBaseCable cable = getTileEntity(world, pos);
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
            if (!cable.hasReasonToStay()){
                setHasData(world, pos, false);
            }
        }
        BlockState blockState = world.getBlockState(pos);
        BooleanProperty sideProperty = getProperty(side);
        boolean connected = blockState.getValue(sideProperty);
        world.setBlockAndUpdate(pos, blockState.setValue(sideProperty, !connected));
        world.setBlockAndUpdate(pos, blockState.setValue(sideProperty, connected));
    }
    public void setDisconnected(World world, BlockPos pos, Direction side, boolean disconnected) {
        TileEntityBaseCable cable = getTileEntity(world, pos);
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
            if (!cable.hasReasonToStay()) {
                setHasData(world,pos,false);
            }
            world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(getProperty(side), !disconnected));
        }
    }




    public VoxelShape getShape(IBlockReader blockReader, BlockPos pos, BlockState state, boolean advanced) {
        TileEntityBaseCable cable = null;
        if (advanced && blockReader instanceof IWorldReader) {
            cable = getTileEntity((IWorldReader) blockReader, pos);
        }
        VoxelShape shape = SHAPE_CORE;
        if (state.getValue(up)) {
            if (cable != null && cable.isExtracting(Direction.UP)){
                shape = VoxelUtils.combine(shape, SHAPE_EXTRACT_UP);
            }else {
                shape = VoxelShapes.or(shape, SHAPE_UP);
            }
        }
        if (state.getValue(down)) {
            if (cable != null && cable.isExtracting(Direction.DOWN)){
                shape = VoxelUtils.combine(shape, SHAPE_EXTRACT_DOWN);
            }else {
                shape = VoxelShapes.or(shape, SHAPE_DOWN);
            }
        }
        if (state.getValue(north)) {
            if (cable != null && cable.isExtracting(Direction.NORTH)){
                shape = VoxelUtils.combine(shape, SHAPE_EXTRACT_NORTH);
            }else {
                shape = VoxelShapes.or(shape, SHAPE_NORTH);
            }
        }
        if (state.getValue(south)) {
            if (cable != null && cable.isExtracting(Direction.SOUTH)){
                shape = VoxelUtils.combine(shape, SHAPE_EXTRACT_SOUTH);
            }else {
                shape = VoxelShapes.or(shape, SHAPE_SOUTH);
            }
        }
        if (state.getValue(west)) {
            if (cable != null && cable.isExtracting(Direction.WEST)){
                shape = VoxelUtils.combine(shape, SHAPE_EXTRACT_WEST);
            }else {
                shape = VoxelShapes.or(shape, SHAPE_WEST);
            }
        }
        if (state.getValue(east)) {
            if (cable != null && cable.isExtracting(Direction.EAST)){
                shape = VoxelUtils.combine(shape, SHAPE_EXTRACT_EAST);
            }else {
                shape = VoxelShapes.or(shape, SHAPE_EAST);
            }
        }
        return shape;
    }
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (context instanceof EntitySelectionContext) {
            EntitySelectionContext ctx = (EntitySelectionContext) context;
            if (ctx.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) ctx.getEntity();
                if (player.level.isClientSide) {
                    return getSelectionShape(state, worldIn, pos, player);
                }
            }
        }
        return getShape(worldIn, pos, state, true);
    }

    public TileEntityBaseCable getTileEntity(IWorldReader world, BlockPos pos) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityBaseCable) {
            return (TileEntityBaseCable) te;
        }
        return null;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (!state.getValue(has_data)) {
            return createTileEntity();
        } else {
            return null;
        }
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return !state.getValue(has_data);
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
        TileEntityBaseCable pipe = getTileEntity(world, pos);
        TileEntityBaseCable other = getTileEntity(world, pos.relative(facing));

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
