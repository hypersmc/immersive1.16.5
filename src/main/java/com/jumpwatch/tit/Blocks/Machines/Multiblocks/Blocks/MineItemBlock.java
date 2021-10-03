package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;

import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock.PortDirection.INLET;
import static com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MineItemBlock.PortDirection.PORT_DIRECTION_ENUM_PROPERTY;

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
}
