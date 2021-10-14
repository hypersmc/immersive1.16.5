package com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Blocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.CoreDrill.Tiles.MinerOutputTile;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MineItemBlock extends MinerBaseBlock {
    public static MineItemBlock INSTANCE;
    public MineItemBlock(Properties properties){
        super();
        //registerDefaultState(defaultBlockState().setValue(PORT_DIRECTION_ENUM_PROPERTY, INLET));
    }
    public enum PortDirection implements IStringSerializable {
        INLET,
        OUTLET;

        public static final EnumProperty<PortDirection> PORT_DIRECTION_ENUM_PROPERTY = EnumProperty.create("portdirection", PortDirection.class);

        @Override
        public String getSerializedName() {
            return toString().toLowerCase();
        }
    }

    @Override
    public boolean isGoodForFrame() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MinerOutputTile();
    }
}
