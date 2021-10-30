package com.jumpwatch.tit.Screen;

import com.jumpwatch.tit.Containers.maceratorBlockContainer;
import com.jumpwatch.tit.theinventorstech;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class maceratorBlockScreen extends ContainerScreen<maceratorBlockContainer> {
    private ResourceLocation GUI = new ResourceLocation(theinventorstech.MOD_ID + ":textures/gui/maceratorgui.png");

    public maceratorBlockScreen(maceratorBlockContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight + 25);
        int k = this.getEnergyStoredScaled(75);
        this.blit(matrixStack, this.leftPos + 153, this.topPos + 13, 180, 33, 16, 75 - k);
//        this.render(this.leftPos + 152, this.topPos + 7, 176, 32, 16, 76 - k);
    }
    private int getEnergyStoredScaled(int pixels) {
        int i = this.menu.getEnergy();
        int j = this.menu.getEnergylimit();
        return i != 0 && j != 0 ? i * pixels / j : 0;
    }

    @Override
    public void renderToolTip(MatrixStack p_238654_1_, List<? extends IReorderingProcessor> p_238654_2_, int p_238654_3_, int p_238654_4_, FontRenderer font) {
        super.renderToolTip(p_238654_1_, p_238654_2_, p_238654_3_, p_238654_4_, font);
    }
}
