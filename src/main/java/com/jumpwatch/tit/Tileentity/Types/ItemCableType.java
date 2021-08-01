package com.jumpwatch.tit.Tileentity.Types;

import com.jumpwatch.tit.Tileentity.TileEntityBaseCable;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Utils.Filter;
import com.jumpwatch.tit.Utils.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCableType extends CableTypes<Item> {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ItemCableType INSTANCE = new ItemCableType();
    @Override
    public String getKey() {
        return "item";
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
            IItemHandler itemHandler = getItemHandler(tileEntity, tileEntity.getBlockPos().relative(side), side.getOpposite());
            if (itemHandler == null) {
                continue;
            }

            List<TileEntityBaseCable.Connection> connections = tileEntity.getSortedConnections(side, this);

            insertOrdered(tileEntity, side, connections, itemHandler);
        }
    }

    @Override
    public boolean canInsert(TileEntity tileEntity, Direction direction) {
        return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).isPresent();
    }

    @Override
    public String getTranslationKey() {
        return "tooltip.cable.itemtip";
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    protected void insertOrdered(TileEntityCableLogic tileEntity, Direction side, List<TileEntityBaseCable.Connection> connections, IItemHandler itemHandler) {
        int itemsToTransfer = getRate();

        ArrayList<ItemStack> nonFittingItems = new ArrayList<>();

        connectionLoop:
        for (TileEntityBaseCable.Connection connection : connections) {
            nonFittingItems.clear();
            IItemHandler destination = getItemHandler(tileEntity, connection.getPos(), connection.getDirection());
            if (destination == null) {
                continue;
            }
            if (isFull(destination)) {
                continue;
            }
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemsToTransfer <= 0) {
                    break connectionLoop;
                }
                ItemStack simulatedExtract = itemHandler.extractItem(i, itemsToTransfer, true);
                if (simulatedExtract.isEmpty()) {
                    continue;
                }
                if (nonFittingItems.stream().anyMatch(stack -> ItemUtils.isStackable(stack, simulatedExtract))) {
                    continue;
                }

                ItemStack stack = ItemHandlerHelper.insertItem(destination, simulatedExtract, false);
                int insertedAmount = simulatedExtract.getCount() - stack.getCount();
                if (insertedAmount <= 0) {
                    nonFittingItems.add(simulatedExtract);
                }
                itemsToTransfer -= insertedAmount;
                itemHandler.extractItem(i, insertedAmount, false);
            }
        }
    }

    private boolean isFull(IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            if (stackInSlot.getCount() < itemHandler.getSlotLimit(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean canInsert(TileEntityBaseCable.Connection connection, ItemStack stack, List<Filter<?>> filters) {
        for (Filter<Item> filter : filters.stream().map(filter -> (Filter<Item>) filter).filter(Filter::isInvert).filter(f -> matchesConnection(connection, f)).collect(Collectors.toList())) {
            if (matches(filter, stack)) {
                return false;
            }
        }
        List<Filter<Item>> collect = filters.stream().map(filter -> (Filter<Item>) filter).filter(f -> !f.isInvert()).filter(f -> matchesConnection(connection, f)).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return true;
        }
        for (Filter<Item> filter : collect) {
            if (matches(filter, stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(Filter<Item> filter, ItemStack stack) {
        CompoundNBT metadata = filter.getMetadata();
        if (metadata == null) {
            return filter.getTag() == null || stack.getItem().is(filter.getTag());
        }
        if (filter.isExactMetadata()) {
            if (deepExactCompare(metadata, stack.getTag())) {
                return filter.getTag() == null || stack.getItem().is(filter.getTag());
            } else {
                return false;
            }
        } else {
            CompoundNBT stackNBT = stack.getTag();
            if (stackNBT == null) {
                return metadata.size() <= 0;
            }
            if (!deepFuzzyCompare(metadata, stackNBT)) {
                return false;
            }
            return filter.getTag() == null || stack.getItem().is(filter.getTag());
        }
    }

    private boolean hasNotInserted(boolean[] inventoriesFull) {
        for (boolean b : inventoriesFull) {
            if (!b) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private IItemHandler getItemHandler(TileEntityCableLogic tileEntity, BlockPos pos, Direction direction) {
        TileEntity te = tileEntity.getLevel().getBlockEntity(pos);
        if (te == null) {
            return null;
        }
        return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).orElse(null);
    }

    public int getSpeed() {
        return 1;
    }
    public int getRate() {
        return 32;
    }

}
