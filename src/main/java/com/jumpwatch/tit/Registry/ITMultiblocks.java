package com.jumpwatch.tit.Registry;

import com.google.common.collect.ImmutableList;
import com.jumpwatch.tit.Blocks.Machines.Multiblocks.MinerMultiblock;
import com.jumpwatch.tit.Multiblockhandeling.BlockMatcher;
import com.jumpwatch.tit.Multiblockhandeling.BlockMatcher.*;
import com.jumpwatch.tit.Multiblockhandeling.ITTemplateMultiblock;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.state.Property;
import net.minecraft.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;

public class ITMultiblocks {
    private static final Logger LOGGER = LogManager.getLogger();

    public static ITTemplateMultiblock MINER;
    public static void init(){
        BlockMatcher.addPredicate((expected, found, world, pos) -> expected==found? Result.allow(1): Result.deny(1));
        List<Property<Boolean>> sideProperties = ImmutableList.of(
                FourWayBlock.NORTH, FourWayBlock.EAST, FourWayBlock.SOUTH, FourWayBlock.WEST
        );
        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
            if(expected.getBlock() instanceof FourWayBlock&&expected.getBlock()==found.getBlock())
                for(Property<Boolean> side : sideProperties)
                    if(!expected.getValue(side))
                        found = found.setValue(side, false);
            return found;
        });

        ImmutableList.Builder<Tag<Block>> genericTagsBuilder = ImmutableList.builder();

        //genericTagsBuilder.add()

        List<Tag<Block>> genericTags = genericTagsBuilder.build();
        BlockMatcher.addPredicate((expected, found, world, pos) -> {
            if(expected.getBlock()!=found.getBlock())
                for(Tag<Block> t : genericTags)
                    if(expected.is(t)&&found.is(t))
                        return Result.allow(2);
            return Result.DEFAULT;
        });
        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
            if(expected.getBlock()== Blocks.HOPPER&&found.getBlock()==Blocks.HOPPER)
                return found.setValue(HopperBlock.FACING, expected.getValue(HopperBlock.FACING));
            return found;
        });
        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
            // Un-waterlog if the expected state is dry, but the found one is not
            if(expected.hasProperty(WATERLOGGED)&&found.hasProperty(WATERLOGGED)
                    &&!expected.getValue(WATERLOGGED)&&found.getValue(WATERLOGGED))
                return found.setValue(WATERLOGGED, false);
            else
                return found;
        });

        //Init of IT multiblocks
        MINER = new MinerMultiblock();
        LOGGER.info("Registering: " + MINER);
    }
}
