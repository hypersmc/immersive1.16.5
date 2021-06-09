package com.jumpwatch.tit;

import com.jumpwatch.tit.Blocks.BlockBaseCable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockEvents {

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent.RightClickBlock event) {

        onPipeClick(event);
    }
    private void onPipeClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getWorld().getBlockState(event.getPos());
        if (!(state.getBlock() instanceof BlockBaseCable)) return;

        BlockBaseCable cable = (BlockBaseCable) state.getBlock();

        Direction side = cable.getSelection(state, event.getWorld(), event.getPos(), event.getPlayer()).getKey();
        ActionResultType result = cable.onCableSideForceActivated(state, event.getWorld(), event.getPos(), event.getPlayer(), event.getHand(), event.getHitVec(), side);
        if (result.consumesAction()) {
            event.setUseItem(Event.Result.ALLOW);
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }
}
