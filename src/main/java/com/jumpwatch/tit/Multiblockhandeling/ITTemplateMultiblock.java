package com.jumpwatch.tit.Multiblockhandeling;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ITTemplateMultiblock extends TemplateMultiblock{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Supplier<BlockState> baseState;
    public ITTemplateMultiblock(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, Supplier<BlockState> baseState){
        super(loc, masterFromOrigin, triggerFromOrigin, size, ImmutableMap.of());
        this.baseState = baseState;

    }
    @Override
    protected void replaceStructureBlock(Template.BlockInfo info, World world, BlockPos actualPos, boolean mirrored, Direction clickDirection, Vector3i offsetFromMaster) {
        BlockState state = baseState.get();
//        if(!offsetFromMaster.equals(Vector3i.ZERO))
//            state = state.setValue(, true);
        world.setBlockAndUpdate(actualPos, state);
        TileEntity curr = world.getBlockEntity(actualPos);
       if(curr instanceof MultiblockPartTileentity)
        {
            MultiblockPartTileentity tile = (MultiblockPartTileentity)curr;
            tile.formed = true;
            tile.offsetToMaster = new BlockPos(offsetFromMaster);
            tile.posInMultiblock = info.pos;
//            if(state.hasProperty(IEProperties.MIRRORED))
//                tile.setMirrored(mirrored);
//            tile.setFacing(transformDirection(clickDirection.getOpposite()));
//            tile.setChanged();
            world.blockEvent(actualPos, world.getBlockState(actualPos).getBlock(), 255, 0);
        }
        else
            LOGGER.error("Expected MB TE at {} during placement", actualPos);
    }
    public Direction transformDirection(Direction original)
    {
        return original;
    }

    public Direction untransformDirection(Direction transformed)
    {
        return transformed;
    }

    public BlockPos multiblockToModelPos(BlockPos posInMultiblock)
    {
        return posInMultiblock.subtract(masterFromOrigin);
    }

    @Override
    public Vector3i getSize(@Nullable World world)
    {
        return size;
    }

    @Nonnull
    @Override
    protected Template getTemplate(@Nullable World world)
    {
        Template result = super.getTemplate(world);
        Preconditions.checkState(
                result.getSize().equals(size),
                "Wrong template size for multiblock %s, template size: %s",
                getTemplateLocation(), result.getSize()
        );
        return result;
    }

    @Override
    protected void prepareBlockForDisassembly(World world, BlockPos pos)
    {
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof MultiblockPartTileentity)
            ((MultiblockPartTileentity<?>)te).formed = false;
        else if (te != null)
            LOGGER.error("Expected multiblock TE at {}, got {}", pos, te);
    }
}
