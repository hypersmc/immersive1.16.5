package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Lathe.Base.LatheBaseTile;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LatheControllerTile extends LatheBaseTile implements INamedContainerProvider {
    public static final TileSupplier SUPPLIER = LatheControllerTile::new;
    public static TileEntityType<?> TYPE;

    public LatheControllerTile(){
        super(TYPE);
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull PlayerEntity player, @Nonnull Hand handIn) {
        return super.onBlockActivated(player, handIn);
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }
}
