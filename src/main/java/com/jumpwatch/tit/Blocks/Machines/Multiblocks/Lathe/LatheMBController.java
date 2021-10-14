package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Base.LatheBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Base.LatheBaseTile;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Tiles.LatheControllerTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.ValidationError;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockController;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class LatheMBController extends RectangularMultiblockController<LatheMBController, LatheBaseTile, LatheBaseBlock> {
    private static final Logger LOGGER = LogManager.getLogger();
    private String name = "Lathe";
    private final Set<LatheControllerTile> controller = new HashSet<>();

    public LatheMBController(@Nonnull World world) {
        super(world, tile -> tile instanceof LatheBaseTile, block -> block instanceof LatheBaseBlock);
        minSize.set(3,3,4);
        maxSize.set(3,3,4);
        setAssemblyValidator(LatheMBController::validate);
    }
    private boolean validate() {
        if (controller.isEmpty()){
            throw new ValidationError(new TranslationTextComponent(
                    "multiblock.error.theinventorstech.no.controller",
                    new TranslationTextComponent(name)));

        }
//        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
//        long tick = theinventorstech.tickNumber();
//        Util.chunkCachedBlockStateIteration(minCoord(), maxCoord(), world, (block, pos) -> {
//            if (block.getBlock() instanceof MinerScaffoldingBlock) {
//                mutableBlockPos.set(pos.x, pos.y, pos.z);
//                if (!blocks.containsPos(mutableBlockPos)) {
//                    throw new ValidationError(new TranslationTextComponent("multiblock.error.theinventorstech.dangling_internal_part", pos.x, pos.y, pos.z));
//                }
//            }
//        });
        return true;
    }

    @Override
    protected void onPartPlaced(@Nonnull LatheBaseTile placed) {
        onPartAttached(placed);
    }

    @Override
    protected synchronized void onPartAttached(@Nonnull LatheBaseTile toAttach) {

    }

    @Override
    protected void onPartBroken(@Nonnull LatheBaseTile broken) {
        onPartDetached(broken);
    }

    @Override
    protected synchronized void onPartDetached(@Nonnull LatheBaseTile toDetach) {
        super.onPartDetached(toDetach);
    }

    boolean updateBlockStates = false;
    public void UpdateBlockStates(){

    }

    @Override
    protected void onUnpaused() {
        onAssembled();
    }
    private boolean forceDirty = false;

    @Override
    public synchronized void tick() {
        if (updateBlockStates) {
            updateBlockStates = false;
            UpdateBlockStates();
        }
        long totalPowerRequested = 0;
        if (theinventorstech.tickNumber() % 2 == 0 ||forceDirty) {
            forceDirty = false;
            markDirty();
        }
    }
}
