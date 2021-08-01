package com.jumpwatch.tit.Tileentity.Types;

import com.jumpwatch.tit.Tileentity.TileEntityBaseCable;
import com.jumpwatch.tit.Tileentity.TileEntityCableLogic;
import com.jumpwatch.tit.Utils.DirectionalPosition;
import com.jumpwatch.tit.Utils.Filter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public abstract class CableTypes<T> {
    public abstract String getKey();
    public abstract void tick(TileEntityCableLogic tileEntity);
    public abstract boolean canInsert(TileEntity tileEntity, Direction direction);
    public abstract String getTranslationKey();
    public abstract ItemStack getIcon();
    public boolean matchesConnection(TileEntityBaseCable.Connection connection, Filter<T> filter) {
        if (filter.getDestination() == null) {
            return true;
        }
        return filter.getDestination().equals(new DirectionalPosition(connection.getPos(), connection.getDirection()));
    }
    public boolean deepExactCompare(INBT meta, INBT item) {
        if (meta instanceof CompoundNBT) {
            if (!(item instanceof CompoundNBT)) {
                return false;
            }
            CompoundNBT c = (CompoundNBT) meta;
            CompoundNBT i = (CompoundNBT) item;
            Set<String> allKeys = new HashSet<>();
            allKeys.addAll(c.getAllKeys());
            allKeys.addAll(i.getAllKeys());
            for (String key : allKeys) {
                if (c.contains(key)) {
                    if (i.contains(key)) {
                        INBT nbt = c.get(key);
                        if (!deepExactCompare(nbt, i.get(key))) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } else if (meta instanceof ListNBT) {
            ListNBT l = (ListNBT) meta;
            if (!(item instanceof ListNBT)) {
                return false;
            }
            ListNBT il = (ListNBT) item;
            if (!l.stream().allMatch(inbt -> il.stream().anyMatch(inbt1 -> deepExactCompare(inbt, inbt1)))) {
                return false;
            }
            if (!il.stream().allMatch(inbt -> l.stream().anyMatch(inbt1 -> deepExactCompare(inbt, inbt1)))) {
                return false;
            }
            return true;
        } else {
            return meta != null && meta.equals(item);
        }
    }

    public boolean deepFuzzyCompare(INBT meta, INBT item) {
        if (meta instanceof CompoundNBT) {
            if (!(item instanceof CompoundNBT)) {
                return false;
            }
            CompoundNBT c = (CompoundNBT) meta;
            CompoundNBT i = (CompoundNBT) item;
            for (String key : c.getAllKeys()) {
                INBT nbt = c.get(key);
                if (i.contains(key, nbt.getId())) {
                    if (!deepFuzzyCompare(nbt, i.get(key))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } else if (meta instanceof ListNBT) {
            ListNBT l = (ListNBT) meta;
            if (!(item instanceof ListNBT)) {
                return false;
            }
            ListNBT il = (ListNBT) item;
            return l.stream().allMatch(inbt -> il.stream().anyMatch(inbt1 -> deepFuzzyCompare(inbt, inbt1)));
        } else {
            return meta != null && meta.equals(item);
        }
    }

    public static int getConnectionsNotFullCount(boolean[] connections) {
        int count = 0;
        for (boolean connection : connections) {
            if (!connection) {
                count++;
            }
        }
        return count;
    }
}
