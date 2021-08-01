package com.jumpwatch.tit.crafting.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.List;

public class CrusherRecipes {
    private static final List<CrusherRecipe> recipes = new ArrayList<>();


    public static void CrusherRecipes(){
        recipes.add(new CrusherRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), 0.0F));
        recipes.add(new CrusherRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Items.GOLD_INGOT), 0.0f));
    }
    public static CrusherRecipe findRecipe(ItemStack input) {
        if (input.isEmpty()) return null;

        for (CrusherRecipe recipe : recipes) {
            if (stackEqualExact(recipe.input, input)) {
                return recipe;
            }
        }
        return null;
    }



    /**
     * Checks item, NBT, and meta if the item is not damageable
     */
    private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }





    public static class CrusherRecipe {
        private final ItemStack input;
        private final ItemStack output;
        private final float experience;

        public CrusherRecipe(ItemStack input, ItemStack output, float experience) {
            this.input = input;
            this.output = output;
            this.experience = experience;
        }

        public ItemStack getInput() {
            return input.copy();
        }

        public ItemStack getOutput() {
            return output.copy();
        }

        public float getExperience() {

            return experience;
        }
    }
}