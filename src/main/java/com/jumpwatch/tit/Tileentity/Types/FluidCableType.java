package com.jumpwatch.tit.Tileentity.Types;

import com.jumpwatch.tit.Tileentity.TileEntityBaseCable;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Utils.Filter;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class FluidCableType extends CableTypes<Fluid> {
    public static final FluidCableType INSTANCE = new FluidCableType();
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String getKey() {
        return "fluid";
    }

    @Override
    public void tick(TileEntityCableLogic tileEntity) {
        for (Direction side : Direction.values()) {
            if (tileEntity.isExtracting(side)) {
                continue;
            }
            if (!tileEntity.shouldWork(side, this)) {
                continue;
            }
            //
            IFluidHandler fluidHandler = getFluidHandler(tileEntity, tileEntity.getBlockPos().relative(side), side.getOpposite());
            if (fluidHandler == null) {
                continue;
            }

            List<TileEntityBaseCable.Connection> connections = tileEntity.getSortedConnections(side, this);
            //LOGGER.info("is extracting and filling");
            insertOrdered(tileEntity, side, connections, fluidHandler);
        }
    }

    @Override
    public boolean canInsert(TileEntity tileEntity, Direction direction) {
        return tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).isPresent();
    }

    @Override
    public String getTranslationKey() {
        return "tooltip.cables.fluid";
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }
    protected void insertOrdered(TileEntityCableLogic tileEntity, Direction side, List<TileEntityBaseCable.Connection> connections, IFluidHandler fluidHandler) {
        int mbToTransfer = getRate();

        connectionLoop:
        for (TileEntityBaseCable.Connection connection : connections) {
            IFluidHandler destination = getFluidHandler(tileEntity, connection.getPos(), connection.getDirection());
            if (destination == null) {
                continue;
            }


            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                if (mbToTransfer <= 0) {
                    break connectionLoop;
                }
                FluidStack fluidInTank = fluidHandler.getFluidInTank(i);
                FluidStack simulatedExtract = fluidHandler.drain(new FluidStack(fluidInTank.getFluid(), mbToTransfer, fluidInTank.getTag()), IFluidHandler.FluidAction.SIMULATE);
                if (simulatedExtract.isEmpty()) {
                    break connectionLoop;
                }

                FluidUtil.tryFluidTransfer(destination, fluidHandler, simulatedExtract, true);
            }
        }
    }



    @Nullable
    private IFluidHandler getFluidHandler(TileEntityCableLogic tileEntity, BlockPos pos, Direction direction) {
        TileEntity te = tileEntity.getLevel().getBlockEntity(pos);
        if (te == null) {
            return null;
        }
        return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);
    }
    public int getRate(){
        return 512;
    }
}
