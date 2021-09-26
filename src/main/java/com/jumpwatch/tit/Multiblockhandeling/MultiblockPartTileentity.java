package com.jumpwatch.tit.Multiblockhandeling;


import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.Lazy;

public abstract class MultiblockPartTileentity<T extends MultiblockPartTileentity> extends ITBaseTileEntity {

    public boolean formed = false;
    public BlockPos posInMultiblock = BlockPos.ZERO;
    public BlockPos offsetToMaster = BlockPos.ZERO;
    protected final ITTemplateMultiblock multiblockInstance;
    public long onlyLocalDissassembly = -1;
    protected final Lazy<Vector3i> structureDimensions;
    protected final boolean hasRedstoneControl;
    protected boolean redstoneControlInverted = false;


    public MultiblockPartTileentity(ITTemplateMultiblock multiblockInstance, TileEntityType<?> type, boolean hasRSControl) {
        super(type);
        this.multiblockInstance = multiblockInstance;
        this.structureDimensions = Lazy.of(() -> multiblockInstance.getSize(level));
        this.hasRedstoneControl = hasRSControl;
    }

    public boolean mirrorFacingOnPlacement(LivingEntity placer)
    {
        return false;
    }

    public boolean canHammerRotate(Direction side, Vector3i hit, LivingEntity entity)
    {
        return false;
    }

    public boolean canRotate(Direction axis)
    {
        return false;
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putBoolean("formed", formed);
        tag.put("posInMultiblock", (INBT) posInMultiblock);
        tag.put("offset", (INBT) offsetToMaster);
        tag.putBoolean("redstoneControlInverted", redstoneControlInverted);
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        formed = nbt.getBoolean("formed");
        posInMultiblock = (BlockPos) nbt.get("posInMultiblock");
        offsetToMaster = (BlockPos) nbt.get("offset");
        redstoneControlInverted = nbt.getBoolean("redstoneControlInverted");
        super.load(state, nbt);
    }
}
