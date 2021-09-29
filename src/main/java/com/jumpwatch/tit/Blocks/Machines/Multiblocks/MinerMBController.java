package com.jumpwatch.tit.Blocks.Machines.Multiblocks;

import com.jumpwatch.tit.Multiblockhandeling.generic.Validator;
import com.jumpwatch.tit.Multiblockhandeling.rectangular.RectangularMultiblockController;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MinerMBController extends RectangularMultiblockController {
    public MinerMBController(@Nonnull World world, @Nonnull Validator tileTypeValidator, @Nonnull Validator blockTypeValidator) {
        super(world, tileTypeValidator, blockTypeValidator);
    }

}
