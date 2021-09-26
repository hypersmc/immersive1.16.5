package com.jumpwatch.tit.Multiblockhandeling;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MultiblockHandler
{
    private static final Direction[] DIRECTIONS = Direction.values();

    static ArrayList<IMultiblock> multiblocks = new ArrayList<>();
    static Map<ResourceLocation, IMultiblock> byUniqueName = new HashMap<>();

    public static void registerMultiblock(IMultiblock multiblock)
    {
        multiblocks.add(multiblock);
        byUniqueName.put(multiblock.getUniqueName(), multiblock);
    }

    public static ArrayList<IMultiblock> getMultiblocks()
    {
        return multiblocks;
    }

    @Nullable
    public static IMultiblock getByUniqueName(ResourceLocation name)
    {
        return byUniqueName.get(name);
    }

    public interface IMultiblock
    {
        /**
         * @return name of the Multiblock. This is used for the interdiction NBT system on the hammer, so this name /must/ be unique.
         */
        ResourceLocation getUniqueName();

        /**
         * Check whether the given block can be used to trigger the structure creation of the multiblock.<br>
         * Basically, a less resource-intensive preliminary check to avoid checking every structure.
         */
        boolean isBlockTrigger(BlockState state, Direction side, @Nullable World world);

        /**
         * This method checks the structure and sets the new one.
         *
         * @return if the structure was valid and transformed
         */
        boolean createStructure(World world, BlockPos pos, Direction side, PlayerEntity player);

        /**
         * TODO
         *
         * @param world
         * @return
         */
        List<BlockInfo> getStructure(@Nullable World world);

        /**
         * An array of ItemStacks that summarizes the total amount of materials needed for the structure. Will be rendered in the Engineer's Manual
         *
         * @return
         */
        @OnlyIn(Dist.CLIENT)
        ItemStack[] getTotalMaterials();

        /**
         * Use this to overwrite the rendering of a Multiblock's Component
         */
        @OnlyIn(Dist.CLIENT)
        boolean overwriteBlockRender(BlockState state, int iterator);

        /**
         * returns the scale modifier to be applied when rendering the structure in the IE manual
         */
        float getManualScale();

        /**
         * returns true to add a button that will switch between the assembly of multiblocks and the finished render
         */
        @OnlyIn(Dist.CLIENT)
        boolean canRenderFormedStructure();

        /**
         * use this function to render the complete multiblock
         *
         * @param transform
         * @param buffer
         */
        @OnlyIn(Dist.CLIENT)
        void renderFormedStructure(MatrixStack transform, IRenderTypeBuffer buffer);

        Vector3i getSize(@Nullable World world);

        void disassemble(World world, BlockPos startPos, boolean mirrored, Direction clickDirectionAtCreation);

        BlockPos getTriggerOffset();
    }

    public static MultiblockFormEvent postMultiblockFormationEvent(PlayerEntity player, IMultiblock multiblock, BlockPos clickedBlock, ItemStack hammer)
    {
        MultiblockFormEvent event = new MultiblockFormEvent(player, multiblock, clickedBlock, hammer);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    /**
     * This event is fired BEFORE the multiblock is attempted to be formed.<br>
     * No checks of the structure have been made. The event simply exists to cancel the formation of the multiblock before it ever happens.
     */
    @Cancelable
    public static class MultiblockFormEvent extends PlayerEvent
    {
        private final IMultiblock multiblock;
        private final BlockPos clickedBlock;
        private final ItemStack hammer;

        public MultiblockFormEvent(PlayerEntity player, IMultiblock multiblock, BlockPos clickedBlock, ItemStack hammer)
        {
            super(player);
            this.multiblock = multiblock;
            this.clickedBlock = clickedBlock;
            this.hammer = hammer;
        }

        public IMultiblock getMultiblock()
        {
            return multiblock;
        }

        public BlockPos getClickedBlock()
        {
            return clickedBlock;
        }

        public ItemStack getHammer()
        {
            return hammer;
        }
    }
}