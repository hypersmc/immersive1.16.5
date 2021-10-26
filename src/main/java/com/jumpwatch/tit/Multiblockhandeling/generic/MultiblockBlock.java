package com.jumpwatch.tit.Multiblockhandeling.generic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static com.jumpwatch.tit.Multiblockhandeling.generic.ConnectedTextureStates.*;
import net.minecraft.block.AbstractBlock.Properties;

public class MultiblockBlock<ControllerType extends MultiblockController<ControllerType, TileType, BlockType>, TileType extends MultiblockTile<ControllerType, TileType, BlockType>, BlockType extends MultiblockBlock<ControllerType, TileType, BlockType>> extends DirectionalBlock implements IWaterLoggable {

    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public MultiblockBlock(Properties properties) {
        super(properties);
        BlockState defaultState = this.getBlock().defaultBlockState();
        if(usesAssemblyState()) {
            defaultState = defaultState.setValue(ASSEMBLED, false);
            defaultState = defaultState.setValue(WATERLOGGED, false);
        }
        if (connectedTexture()) {
            defaultState = defaultState.setValue(TOP_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(BOTTOM_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(NORTH_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(SOUTH_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(EAST_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(WEST_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(WATERLOGGED, false);
        }
        this.registerDefaultState(defaultState);
    }

    @Override
    public final boolean hasTileEntity(BlockState state) {
        return true;
    }

    public boolean connectedTexture() {
        return false;
    }

    public boolean usesAssemblyState(){
        return true;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof MultiblockTile) {
            ActionResultType tileResult = ((MultiblockTile) te).onBlockActivated(player, handIn);
            if (tileResult != ActionResultType.PASS) {
                return tileResult;
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }


    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(WATERLOGGED);
        if(usesAssemblyState()) {
            builder.add(ASSEMBLED);
        }
        if (connectedTexture()) {
            builder.add(TOP_CONNECTED_PROPERTY);
            builder.add(BOTTOM_CONNECTED_PROPERTY);
            builder.add(NORTH_CONNECTED_PROPERTY);
            builder.add(SOUTH_CONNECTED_PROPERTY);
            builder.add(EAST_CONNECTED_PROPERTY);
            builder.add(WEST_CONNECTED_PROPERTY);
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (connectedTexture()) {
            updateConnectedTextureState(worldIn, pos, state);
        }
    }


    @Override
    public void setPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (connectedTexture()) {
            updateConnectedTextureState(worldIn, pos, this.defaultBlockState());
        }
    }

    private void updateConnectedTextureState(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        state = state.setValue(TOP_CONNECTED_PROPERTY, connectToBlock(worldIn.getBlockState(pos.relative(Direction.UP)).getBlock()));
        state = state.setValue(BOTTOM_CONNECTED_PROPERTY, connectToBlock(worldIn.getBlockState(pos.relative(Direction.DOWN)).getBlock()));
        state = state.setValue(NORTH_CONNECTED_PROPERTY, connectToBlock(worldIn.getBlockState(pos.relative(Direction.NORTH)).getBlock()));
        state = state.setValue(SOUTH_CONNECTED_PROPERTY, connectToBlock(worldIn.getBlockState(pos.relative(Direction.SOUTH)).getBlock()));
        state = state.setValue(EAST_CONNECTED_PROPERTY, connectToBlock(worldIn.getBlockState(pos.relative(Direction.EAST)).getBlock()));
        state = state.setValue(WEST_CONNECTED_PROPERTY, connectToBlock(worldIn.getBlockState(pos.relative(Direction.WEST)).getBlock()));
        worldIn.setBlock(pos, state, 2);
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }
        return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    protected boolean connectToBlock(Block block){
        return block == this;
    }
}