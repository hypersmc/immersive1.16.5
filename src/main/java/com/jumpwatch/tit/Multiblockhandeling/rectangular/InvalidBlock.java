package com.jumpwatch.tit.Multiblockhandeling.rectangular;

import com.jumpwatch.tit.Multiblockhandeling.generic.ValidationError;
import com.jumpwatch.tit.Utils.org.joml.Vector3i;
import net.minecraft.block.Block;
import net.minecraft.util.text.TranslationTextComponent;

@SuppressWarnings("unused")
public class InvalidBlock extends ValidationError {

    public InvalidBlock() {
        super();
    }

    public InvalidBlock(String s) {
        super(s);
    }

    public InvalidBlock(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBlock(Throwable cause) {
        super(cause);
    }

    public InvalidBlock(Block block, Vector3i worldPosition, String multiblockPosition) {
        super(new TranslationTextComponent(
                "multiblock.error.theinventorstech.invalid_block." + multiblockPosition,
                new TranslationTextComponent(block.getDescriptionId()),
                "(x: " + worldPosition.x + "; y: " + worldPosition.y + "; z: " + worldPosition.z + ")")
        );
    }
}