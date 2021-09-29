package com.jumpwatch.tit.Multiblockhandeling.generic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static com.jumpwatch.tit.Multiblockhandeling.generic.ConnectedTextureStates.*;
public class MultiblockBlock<ControllerType extends MultiblockController<ControllerType, TileType, BlockType>, TileType extends MultiblockTile<ControllerType, TileType, BlockType>, BlockType extends MultiblockBlock<ControllerType, TileType, BlockType>> extends DirectionalBlock {

    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");

    public MultiblockBlock(Properties properties) {
        super(properties);
        BlockState defaultState = this.getBlock().defaultBlockState();
        if(usesAssemblyState()) {
            defaultState = defaultState.setValue(ASSEMBLED, false);
        }
        if (connectedTexture()) {
            defaultState = defaultState.setValue(TOP_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(BOTTOM_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(NORTH_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(SOUTH_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(EAST_CONNECTED_PROPERTY, false);
            defaultState = defaultState.setValue(WEST_CONNECTED_PROPERTY, false);
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
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {

        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    protected boolean connectToBlock(Block block){
        return block == this;
    }
}