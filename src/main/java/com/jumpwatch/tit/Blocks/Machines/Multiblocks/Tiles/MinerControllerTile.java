package com.jumpwatch.tit.Blocks.Machines.Multiblocks.Tiles;

import com.jumpwatch.tit.Blocks.Machines.Multiblocks.Base.MinerBaseTile;
import com.jumpwatch.tit.Multiblockhandeling.generic.MultiblockBlock;
import com.jumpwatch.tit.Utils.TileSupplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MinerControllerTile extends MinerBaseTile implements INamedContainerProvider {
    public static TileEntityType<?> TYPE;
    public static final TileSupplier SUPPLIER = MinerControllerTile::new;

    public MinerControllerTile(){
        super(TYPE);
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull PlayerEntity player, @Nonnull Hand handIn) {
        if (player.isCrouching() && handIn == Hand.MAIN_HAND && player.getMainHandItem().getItem() == Items.STICK) {
            if (controller != null) {
                //controller.toggleActive();
            }
            return ActionResultType.SUCCESS;
        }

        if (handIn == Hand.MAIN_HAND) {
            assert level != null;
            if (level.getBlockState(getBlockPos()).getValue(MultiblockBlock.ASSEMBLED)) {
                if (!level.isClientSide) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, this, this.getBlockPos());
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(player, handIn);
    }


    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.tit.miner.controller");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }
}
