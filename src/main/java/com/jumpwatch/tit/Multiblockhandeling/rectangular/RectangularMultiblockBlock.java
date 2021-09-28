package com.jumpwatch.tit.Multiblockhandeling.rectangular;

import com.jumpwatch.tit.Multiblockhandeling.generic.MultiblockBlock;
import com.jumpwatch.tit.Utils.BlockStates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nonnull;
import static com.jumpwatch.tit.Multiblockhandeling.rectangular.AxisPosition.*;

public abstract class RectangularMultiblockBlock<ControllerType extends RectangularMultiblockController<ControllerType, TileType, BlockType>, TileType extends RectangularMultiblockTile<ControllerType, TileType, BlockType>, BlockType extends RectangularMultiblockBlock<ControllerType, TileType, BlockType>> extends MultiblockBlock<ControllerType, TileType, BlockType> {

    public RectangularMultiblockBlock(@Nonnull Properties properties) {
        super(properties);
        if (usesAxisPositions()) {
            registerDefaultState(defaultBlockState().setValue(X_AXIS_POSITION, MIDDLE));
            registerDefaultState(defaultBlockState().setValue(Y_AXIS_POSITION, MIDDLE));
            registerDefaultState(defaultBlockState().setValue(Z_AXIS_POSITION, MIDDLE));
        }
        if (usesFaceDirection()){
            registerDefaultState(defaultBlockState().setValue(BlockStates.FACING, Direction.UP));
        }
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if (usesAxisPositions()) {
            builder.add(X_AXIS_POSITION);
            builder.add(Y_AXIS_POSITION);
            builder.add(Z_AXIS_POSITION);
        }
        if(usesFaceDirection()){
            builder.add(BlockStates.FACING);
        }
    }


    public boolean usesAxisPositions() {
        return false;
    }

    public boolean usesFaceDirection(){
        return false;
    }

    public abstract boolean isGoodForInterior();

    public abstract boolean isGoodForExterior();

    public abstract boolean isGoodForFrame();

    public boolean isGoodForCorner(){
        return isGoodForFrame();
    }
}