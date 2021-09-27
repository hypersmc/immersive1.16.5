package com.jumpwatch.tit.Multiblockhandeling.generic;

public interface IOnDisassemblyTile {
    /**
     * Called when a multiblock is disassembled
     * not called when a multiblock is paused
     * called after a controller's onDisassembled and blockstates are updated
     */
    void onDisassembly();
}