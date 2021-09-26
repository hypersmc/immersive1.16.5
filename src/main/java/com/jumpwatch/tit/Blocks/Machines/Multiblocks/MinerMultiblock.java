package com.jumpwatch.tit.Blocks.Machines.Multiblocks;

import com.jumpwatch.tit.Multiblockhandeling.ITTemplateMultiblock;
import com.jumpwatch.tit.Registry.BlockRegistry;
import com.jumpwatch.tit.Utils.ClientUtils;
import com.jumpwatch.tit.theinventorstech;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class MinerMultiblock extends ITTemplateMultiblock {
    public MinerMultiblock() {
        super(new ResourceLocation(theinventorstech.MOD_ID, "multiblocks/miner"), new BlockPos(2, 1, 2), new BlockPos(2, 0, 4), new BlockPos(5, 5, 5), () -> BlockRegistry.Multiblocks.miner.defaultBlockState());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderFormedStructure() {
        return true;
    }
    @OnlyIn(Dist.CLIENT)
    private static ItemStack renderStack;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderFormedStructure(MatrixStack transform, IRenderTypeBuffer buffer) {
        if(renderStack==null)
            renderStack = new ItemStack(BlockRegistry.Multiblocks.miner);
        transform.translate(2.5, 2.25, 2.25);
        transform.mulPose(new Quaternion(0, 45, 0, true));
        transform.mulPose(new Quaternion(-20, 0, 0, true));
        transform.scale(6.5F, 6.5F, 6.5F);

        ClientUtils.mc().getItemRenderer().renderStatic(
                renderStack,
                ItemCameraTransforms.TransformType.GUI,
                0xf000f0,
                OverlayTexture.NO_OVERLAY,
                transform, buffer
        );
    }

    @Override
    public float getManualScale() {
        return 12;
    }
}
