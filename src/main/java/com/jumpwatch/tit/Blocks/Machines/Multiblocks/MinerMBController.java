package com.jumpwatch.tit.Blocks.Machines.Multiblocks;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseBlock;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.MinerControllerTile;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.MinerOutputTile;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles.MinerPowerTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.ValidationError;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockController;
import com.jumpwatch.tit.Utils.Util;
import com.jumpwatch.tit.Utils.org.joml.Vector3i;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class MinerMBController extends RectangularMultiblockController<MinerMBController, MinerBaseTile, MinerBaseBlock> {

    public MinerMBController(@Nonnull World world) {
        super(world, tile -> tile instanceof MinerBaseTile, block -> block instanceof MinerBaseBlock);
        minSize.set(3);
        maxSize.set(3);

        setAssemblyValidator(genericController -> {
//            if (controller.isEmpty()){
//                throw new ValidationError("multiblock.error.theinventorstech.no.controller");
//            }
//            if (powerPorts.isEmpty()){
//                throw new ValidationError("multiblock.error.theinventorstech.no.powerport");
//            }
//            if (itemPort.isEmpty()){
//                throw new ValidationError("multiblock.error.theinventorstech.no.itemport");
//            }
            BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
            long tick = theinventorstech.tickNumber();
            return true;
        });
    }
    private final Set<MinerControllerTile> controller = new HashSet<>();
    private final Set<MinerPowerTile> powerPorts = new HashSet<>();
    private final Set<MinerOutputTile> itemPort = new HashSet<>();

//    @Override
//    protected void onPartPlaced(@Nonnull MinerBaseTile placed) {
//        onPartPlaced(placed);
//    }


    @Override
    protected void onPartPlaced(@Nonnull MinerBaseTile placed) {
        super.onPartPlaced(placed);
    }

    @Override
    protected void onPartAttached(@Nonnull MinerBaseTile tile) {
        if (tile instanceof MinerControllerTile) {
            controller.add((MinerControllerTile) tile);
        }
        if (tile instanceof MinerPowerTile) {
            powerPorts.add((MinerPowerTile) tile);
        }
        if (tile instanceof MinerOutputTile) {
            itemPort.add((MinerOutputTile) tile);
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
        }
        if (tile instanceof MinerPowerTile) {
            powerPorts.remove((MinerPowerTile) tile);
        }
        if (tile instanceof MinerOutputTile) {
            itemPort.remove((MinerOutputTile) tile);
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
//            setChanged();
        }
    }
}
