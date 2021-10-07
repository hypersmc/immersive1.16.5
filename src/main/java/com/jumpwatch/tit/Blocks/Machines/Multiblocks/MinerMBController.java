package com.jumpwatch.tit.Blocks.Machines.Multiblocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Blocks.MinerScaffoldingBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.*;
import com.jumpwatch.tit.Multiblockhandeling.generic.ValidationError;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockController;
import com.jumpwatch.tit.Utils.Util;
import com.jumpwatch.tit.Utils.org.joml.Vector3i;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class MinerMBController extends RectangularMultiblockController<MinerMBController, MinerBaseTile, MinerBaseBlock> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<MinerControllerTile> controller = new HashSet<>();
    private final Set<MinerDrillCoreTile> drillcore = new HashSet<>();
    private final Set<MinerDrillDrillTile> drilldrill = new HashSet<>();
    private final Set<MinerPowerTile> powerPorts = new HashSet<>();
    private final Set<MinerOutputTile> itemPort = new HashSet<>();

    public MinerMBController(World world) {
        super(world, tile -> tile instanceof MinerBaseTile, block -> block instanceof MinerBaseBlock);
        minSize.set(3,3,3);
        maxSize.set(3,3,3);

        setAssemblyValidator(MinerMBController::validate);

    }

    private boolean validate() {
        if (controller.isEmpty()){
            throw new ValidationError("multiblock.error.theinventorstech.no.controller");
        }
        if (powerPorts.isEmpty()){
            throw new ValidationError("multiblock.error.theinventorstech.no.powerport");
        }
        if (itemPort.isEmpty()){
            throw new ValidationError("multiblock.error.theinventorstech.no.itemport");
        }
        if (drillcore.isEmpty()){
            throw new ValidationError("multiblock.error.theinventorstech.no.drillcore");
        }
        if (drilldrill.isEmpty()){
            throw new ValidationError("multiblock.error.theinventorstech.no.drilldrill");
        }
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        long tick = theinventorstech.tickNumber();
        Util.chunkCachedBlockStateIteration(minCoord(), maxCoord(), world, (block, pos) -> {
            if (block.getBlock() instanceof MinerScaffoldingBlock) {
                mutableBlockPos.set(pos.x, pos.y, pos.z);
                if (!blocks.containsPos(mutableBlockPos)) {
                    throw new ValidationError(new TranslationTextComponent("multiblock.error.theinventorstech.dangling_internal_part", pos.x, pos.y, pos.z));
                }
            }
        });
        return true;
    }




    @Override
    protected void onPartPlaced(@Nonnull MinerBaseTile placed) {
        onPartAttached(placed);
    }

    @Override
    protected synchronized void onPartAttached(@Nonnull MinerBaseTile tile) {
        if (tile instanceof MinerControllerTile) {
            controller.add((MinerControllerTile) tile);
            LOGGER.info("Found: " + tile);
        }
        if (tile instanceof MinerPowerTile) {
            powerPorts.add((MinerPowerTile) tile);
            LOGGER.info("Found: " + tile);
        }
        if (tile instanceof MinerOutputTile) {
            itemPort.add((MinerOutputTile) tile);
            LOGGER.info("Found: " + tile);
        }
        if (tile instanceof MinerDrillCoreTile) {
            drillcore.add((MinerDrillCoreTile) tile);
            LOGGER.info("Found: " + tile);
        }
        if (tile instanceof MinerDrillDrillTile) {
            drilldrill.add((MinerDrillDrillTile) tile);
            LOGGER.info("Found: " + tile);
        }
    }

    @Override
    protected void onPartBroken(@Nonnull MinerBaseTile broken) {
        onPartDetached(broken);
    }

    @Override
    protected synchronized void onPartDetached(@Nonnull MinerBaseTile tile) {
        if (tile instanceof MinerControllerTile) {
            controller.remove((MinerControllerTile) tile);
            LOGGER.info("Removed: " + tile);
        }
        if (tile instanceof MinerPowerTile) {
            powerPorts.remove((MinerPowerTile) tile);
            LOGGER.info("Removed: " + tile);
        }
        if (tile instanceof MinerOutputTile) {
            itemPort.remove((MinerOutputTile) tile);
            LOGGER.info("Removed: " + tile);
        }
        if (tile instanceof MinerDrillDrillTile) {
            drilldrill.remove((MinerDrillDrillTile) tile);
            LOGGER.info("Removed: " + tile);
        }
        if (tile instanceof MinerDrillCoreTile) {
            drillcore.remove((MinerDrillCoreTile) tile);
            LOGGER.info("Removed: " + tile);
        }
    }
    boolean updateBlockStates = false;
    public void UpdateBlockStates(){
        controller.forEach(controllers -> {
            world.setBlockAndUpdate(controllers.getBlockPos(), controllers.getBlockState());
            controllers.setChanged();
        });
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

        if (theinventorstech.tickNumber() % 2 == 0 || forceDirty) {
            forceDirty = false;
            markDirty();
        }
    }
}
