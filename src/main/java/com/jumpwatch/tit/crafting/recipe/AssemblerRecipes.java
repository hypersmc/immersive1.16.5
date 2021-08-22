package com.jumpwatch.tit.crafting.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AssemblerRecipes {
    private static final List<AssemblerRecipe> recipes = new ArrayList<>();

    public static void AssemblerRecipes(){

    }
    public static AssemblerRecipe findRecipe(ItemStack slot0, ItemStack slot1, ItemStack slot2, ItemStack slot3, ItemStack slot4, ItemStack slot5, ItemStack slot6, ItemStack slot7, ItemStack slot8){
        if (slot0.isEmpty()  || slot1.isEmpty() || slot2.isEmpty() || slot3.isEmpty() || slot4.isEmpty() || slot5.isEmpty() || slot6.isEmpty() || slot7.isEmpty() || slot8.isEmpty()) return null;
        for (AssemblerRecipe recipe : recipes) {
            if ((stackEqualExact(recipe.input, slot0)) && (stackEqualExact(recipe.input2, slot1)) && (stackEqualExact(recipe.input3, slot2)) && (stackEqualExact(recipe.input4, slot3)) && (stackEqualExact(recipe.input5, slot4)) && (stackEqualExact(recipe.input6, slot5)) && (stackEqualExact(recipe.input7, slot6)) && (stackEqualExact(recipe.input8, slot7)) && (stackEqualExact(recipe.input9, slot8)) ) {
                return recipe;
            }
        }
        return null;
    }

    private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }

    public static class AssemblerRecipe{
        private final ItemStack input;
        private final ItemStack input2;
        private final ItemStack input3;
        private final ItemStack input4;
        private final ItemStack input5;
        private final ItemStack input6;
        private final ItemStack input7;
        private final ItemStack input8;
        private final ItemStack input9;
        private final ItemStack output;
        private final float experience;

        public AssemblerRecipe(ItemStack slot0, ItemStack slot1, ItemStack slot2, ItemStack slot3, ItemStack slot4, ItemStack slot5, ItemStack slot6, ItemStack slot7, ItemStack slot8, ItemStack output, float experience){
            this.input = slot0;
            this.input2 = slot1;
            this.input3 = slot2;
            this.input4 = slot3;
            this.input5 = slot4;
            this.input6 = slot5;
            this.input7 = slot6;
            this.input8 = slot7;
            this.input9 = slot8;
            this.output = output;
            this.experience = experience;
        }
        public ItemStack getOutput(){
            return output.copy();
        }

    }
}
